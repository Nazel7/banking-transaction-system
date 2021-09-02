package com.wayapaychat.bank.services;

import com.wayapaychat.bank.entity.models.AccountModel;
import com.wayapaychat.bank.entity.models.TransactionModel;
import com.wayapaychat.bank.entity.models.UserModel;
import com.wayapaychat.bank.config.*;
import com.wayapaychat.bank.enums.TransType;
import com.wayapaychat.bank.entity.builder.AccountMapper;
import com.wayapaychat.bank.entity.builder.TransactionMapper;
import com.wayapaychat.bank.entity.models.NotificationLog;
import com.wayapaychat.bank.event.notifcation.DataInfo;
import com.wayapaychat.bank.event.notifcation.Receipient;
import com.wayapaychat.bank.event.notifcation.NotificationLogEvent;
import com.wayapaychat.bank.repository.AccountRepo;
import com.wayapaychat.bank.repository.TransactionRepo;
import com.wayapaychat.bank.repository.UserRepo;
import com.wayapaychat.bank.utils.TierLevelSpecUtil;
import com.wayapaychat.bank.utils.TransferSpecUtil;
import com.wayapaychat.bank.dtos.response.Account;
import com.wayapaychat.bank.dtos.response.Transaction;
import com.wayapaychat.bank.dtos.request.TopupDto;
import com.wayapaychat.bank.dtos.request.TransactionDto;
import com.wayapaychat.bank.dtos.response.TransferNotValidException;
import com.wayapaychat.bank.dtos.response.UserNotFoundException;
import com.wayapaychat.bank.utils.AccountVerificationUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionService {

    private final TransactionRepo mTransactionRepo;
    private final AccountRepo mAccountRepo;
    private final UserRepo mUserRepo;
    private final ApplicationEventPublisher mEventPublisher;

    private final TranxMessageConfig mMessageConfig;

    @Async
    public Transaction tranferFund(TransactionDto transactionDto)
            throws AccountException, TransferNotValidException, UserNotFoundException,
                   NotActiveException {

        DataInfo data = new DataInfo();
        Receipient receipient = new Receipient();
        List<Receipient> receipients = new ArrayList<>();
        NotificationLog notificationLog = new NotificationLog();

        Optional<UserModel> userModel = mUserRepo.findById(transactionDto.getUserId());
        final AccountModel debitAccount =
                mAccountRepo.findAccountModelByIban(transactionDto.getDebitAccountNo());

        if (!userModel.isPresent()) {

            throw new UserNotFoundException("User not found ");
        }
        if (!userModel.get().equals(debitAccount.getUserModel())) {

            throw new AccountNotFoundException("User account not valid");
        }

        final AccountModel creditAccount =
                mAccountRepo.findAccountModelByIban(transactionDto.getBenefAccountNo());

        final UserModel sender = debitAccount.getUserModel();
        final UserModel receiver = creditAccount.getUserModel();

        // Very if account is active not close or debit freeze
        boolean isAccountStatusVerified =
                AccountVerificationUtil.verifyAccount(debitAccount, creditAccount);

        // Check if user meet tierLevel specification
        boolean isSenderTierLevelSatisfied =
                TierLevelSpecUtil.validate(sender, transactionDto.getAmount());
        boolean isReceiverTierLevelSatisfied =
                TierLevelSpecUtil.validate(receiver, transactionDto.getAmount());

        // Verify if transaction request body is satisfied
        boolean isTranferRequestSpecified = TransferSpecUtil.isSatisfied(transactionDto);

        if (!isAccountStatusVerified || !isSenderTierLevelSatisfied || !isReceiverTierLevelSatisfied
                || !isTranferRequestSpecified) {

            throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
        }

        TransactionModel transactionModel = TransactionMapper.mapToModel(transactionDto);

        AccountModel debitedAccount =
                debitAccount.withdraw(transactionDto.getAmount());

        if (debitedAccount != null) {
            AccountModel creditedAccount =
                    creditAccount.deposit(transactionDto.getAmount());

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
                                  transactionDto.getAmount());
            data.setMessage(notificationMessage);
            data.setRecipients(receipients);
            notificationLog.setData(data);
            notificationLog.setEventType(TransType.TRANSFER.name());
            String name= userModel.get().getFirstName().concat(" ").concat(userModel.get().getLastName());
            notificationLog.setInitiator(name);

//           ResponseEntity<DataBody> dataBodyResponseEntity= mNotification.sendNotification(dataBody);
            NotificationLogEvent
                    notificationLogEvent = new NotificationLogEvent(this, notificationLog);
            mEventPublisher.publishEvent(notificationLogEvent);
           log.info("::: notification sent to receipient: [{}] DB locator :::",
                    notificationLogEvent.getDataBody());

            return TransactionMapper.mapToDomain(savedTransaction);

        }

        throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
    }

    // for Testing purpose
    public Account fundAccount(TopupDto topupDto) throws AccountNotFoundException {
        final AccountModel creditAccount =
                mAccountRepo.findAccountModelByIban(topupDto.getIban());
        AccountModel topedAccount = creditAccount.deposit(topupDto.getAmount());
        log.info("Account with iban: [{}] topped up", topedAccount.getIban());
        mAccountRepo.save(creditAccount);
        return AccountMapper.mapToDomain(topedAccount);
    }
}
