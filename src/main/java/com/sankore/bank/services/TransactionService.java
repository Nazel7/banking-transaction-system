package com.sankore.bank.services;

import com.sankore.bank.auth.util.JwtUtil;
import com.sankore.bank.configs.TranxMessageConfig;
import com.sankore.bank.dtos.request.TopupDto;
import com.sankore.bank.dtos.request.TransferDto;
import com.sankore.bank.dtos.response.Account;
import com.sankore.bank.dtos.response.Transaction;
import com.sankore.bank.dtos.response.TransferNotValidException;
import com.sankore.bank.dtos.response.UserNotFoundException;
import com.sankore.bank.entities.builder.AccountMapper;
import com.sankore.bank.entities.builder.TransactionMapper;
import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.entities.models.TransactionModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.TransType;
import com.sankore.bank.event.notifcation.DataInfo;
import com.sankore.bank.event.notifcation.NotificationLog;
import com.sankore.bank.event.notifcation.NotificationLogEvent;
import com.sankore.bank.event.notifcation.Receipient;
import com.sankore.bank.repositories.AccountRepo;
import com.sankore.bank.repositories.TransactionRepo;
import com.sankore.bank.repositories.UserRepo;
import com.sankore.bank.utils.BaseUtil;
import com.sankore.bank.utils.TierLevelSpecUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionService {

    private final TransactionRepo mTransactionRepo;
    private final AccountRepo mAccountRepo;
    private final UserRepo mUserRepo;
    private final ApplicationEventPublisher mEventPublisher;
    private final JwtUtil jwtUtil;

    private final TranxMessageConfig mMessageConfig;

    public Transaction tranferFund(TransferDto transferDto, HttpServletRequest request)
            throws AccountException, TransferNotValidException, UserNotFoundException {
        log.info("::: In tranferFund.....");

        String token = request.getHeader("Authorization");
        if (token.contains("Bearer")) {
            token = token.split(" ")[1];
        }

        DataInfo data = new DataInfo();
        Receipient receipient = new Receipient();
        List<Receipient> receipients = new ArrayList<>();
        NotificationLog notificationLog = new NotificationLog();

        boolean isRequestValid = BaseUtil.isRequestSatisfied(transferDto);

        if (!isRequestValid) {
            log.error("::: Invalid Transfer request, please try again later.");
            throw new TransferNotValidException("Invalid Transfer request, please try again later.");
        }

        Optional<UserModel> userModel = mUserRepo.findById(transferDto.getUserId());
        final AccountModel debitAccount =
                mAccountRepo.findAccountModelByIban(transferDto.getDebitAccountNo());

        if (userModel.isEmpty()) {

            log.error("::: User not found with body");
            throw new UserNotFoundException("User not found ");
        }

        if (!userModel.get().equals(debitAccount.getUserModel())) {

            log.error("::: user account not valid, Account: [{}] :::", debitAccount);
            throw new AccountNotFoundException("User account not valid");
        }

        final AccountModel creditAccount =
                mAccountRepo.findAccountModelByIban(transferDto.getBenefAccountNo());

        final UserModel sender = debitAccount.getUserModel();
        final UserModel receiver = creditAccount.getUserModel();

        // Very if account is active not close or debit freeze
        boolean isAccountStatusVerified =
                BaseUtil.verifyAccount(debitAccount, creditAccount);
        log.info("::: Account Status verified: [{}] :::", isAccountStatusVerified);

        // Check if user meet tierLevel specification
        boolean isSenderTierLevelSatisfied =
                TierLevelSpecUtil.validate(sender, transferDto.getAmount());
        log.info("::: Sender meet tierLeveL: [{}] :::", isSenderTierLevelSatisfied);
        boolean isReceiverTierLevelSatisfied =
                TierLevelSpecUtil.validate(receiver, transferDto.getAmount());
        log.info("::: Receiver meet tierLeveL: [{}] :::", isReceiverTierLevelSatisfied);

        // Verify if transaction request body is satisfied
        boolean isTranferRequestSpecified = BaseUtil.isTransferSatisfied(transferDto);
        log.info("::: Transaction request satisfied: [{}] :::", isTranferRequestSpecified);

        if (!isAccountStatusVerified || !isSenderTierLevelSatisfied || !isReceiverTierLevelSatisfied
                || !isTranferRequestSpecified) {
            log.info("::: Transaction failed");
            throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
        }


        final TransactionModel transactionModel = TransactionMapper.mapToModel(transferDto, token);

        AccountModel debitedAccount =
                debitAccount.withdraw(transferDto.getAmount());

        if (debitedAccount == null) {
            log.error("::: Transfer failed insufficient balance.");
            throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
        }

        AccountModel creditedAccount =
                creditAccount.deposit(transferDto.getAmount());

        mAccountRepo.save(debitedAccount);
        mAccountRepo.save(creditedAccount);

        log.info("::: Account with iban: [{}] has been debited ", debitedAccount.getIban());
        log.info("::: Account with iban: [{}] has been credited ", creditedAccount.getIban());
        log.info(mMessageConfig.getTransfer_successful());
        TransactionModel savedTransaction = mTransactionRepo.save(transactionModel);

        receipient.setEmail(creditAccount.getUserModel().getEmail());
        receipient.setTelephone(creditAccount.getUserModel().getPhone());
        receipients.add(receipient);
        String notificationMessage =
                String.format("Your account %s has been credit with sum of [%s%s] only ",
                        creditAccount.getIban(),
                        creditAccount.getCurrency(),
                        transferDto.getAmount());
        data.setMessage(notificationMessage);
        data.setRecipients(receipients);
        notificationLog.setData(data);
        notificationLog.setTranxRef(transferDto.getTranxRef());
        notificationLog.setChannelCode(transferDto.getChannelCode());
        notificationLog.setTranxDate(savedTransaction.getPerformedAt());

        notificationLog.setEventType(TransType.TRANSFER.name());
        String name = userModel.get().getFirstName().concat(" ").concat(userModel.get().getLastName());
        notificationLog.setInitiator(name);

        final NotificationLogEvent
                notificationLogEvent = new NotificationLogEvent(this, notificationLog);
        mEventPublisher.publishEvent(notificationLogEvent);
        log.info("::: notification sent to recipient: [{}] DB locator :::",
                notificationLogEvent.getNotificationLog());

        return TransactionMapper.mapToDomain(savedTransaction);

    }


    public Account fundAccount(TopupDto topupDto, HttpServletRequest request) throws AccountNotFoundException {
        log.info("::: In fundAccount.....");
        String token = request.getHeader("Authorization");
        if (token.contains("Bearer")) {
            token = token.split(" ")[1];
        }
        boolean isRequestValid = BaseUtil.isRequestSatisfied(topupDto);

        if (!isRequestValid) {
            log.error("::: TopUp request error with payload: [{}]", topupDto);
            throw new IllegalArgumentException("TopUp request error with payload");
        }

        String userName =

        final AccountModel creditAccount =
                mAccountRepo.findAccountModelByIban(topupDto.getIban());
        AccountModel topedAccount = creditAccount.deposit(topupDto.getAmount());
        log.info("Account with iban: [{}] topped up", topedAccount.getIban());
        mAccountRepo.save(creditAccount);
        return AccountMapper.mapToDomain(topedAccount);
    }

    // TODO: UPDATE FUND_ACCOUNT  AND TRANSFER_FUND SERVICE
    // TODO: DIRECT_DEBIT
    // TODO: INVESTMENT ACCOUNT

}
