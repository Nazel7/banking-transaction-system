package com.sankore.bank.services;

import com.sankore.bank.auth.util.JwtUtil;
import com.sankore.bank.configs.TranxMessageConfig;
import com.sankore.bank.contants.ChannelConsts;
import com.sankore.bank.dtos.request.*;
import com.sankore.bank.dtos.response.*;
import com.sankore.bank.entities.builder.AccountMapper;
import com.sankore.bank.entities.builder.InvestmentMapper;
import com.sankore.bank.entities.builder.TransactionMapper;
import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.entities.models.TransactionModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.TransType;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.event.notifcation.DataInfo;
import com.sankore.bank.event.notifcation.NotificationLog;
import com.sankore.bank.event.notifcation.NotificationLogEvent;
import com.sankore.bank.event.notifcation.Receipient;
import com.sankore.bank.repositories.AccountRepo;
import com.sankore.bank.repositories.InvestmentRepo;
import com.sankore.bank.repositories.TransactionRepo;
import com.sankore.bank.repositories.UserRepo;
import com.sankore.bank.utils.BaseUtil;
import com.sankore.bank.utils.TierLevelSpecUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionService {

    private final TransactionRepo mTransactionRepo;
    private final AccountRepo mAccountRepo;
    private final UserRepo mUserRepo;
    private final InvestmentRepo mInvestmentRepo;
    private final ApplicationEventPublisher mEventPublisher;
    private final JwtUtil jwtUtil;

    private final TranxMessageConfig mMessageConfig;

    public Transaction doFundTransfer(TransferDto transferDto, HttpServletRequest request)
            throws TransferNotValidException {
        log.info("::: In tranferFund.....");

        try {

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
            System.out.println("Sender " + sender);
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
            if (!sender.getVerificationCode().equals(transferDto.getVerificationCode())) {
                log.error("Transfer verificationCode failed.....");
                throw new IllegalAccessException("Access denied for Invalid verificationCode");
            }

            AccountModel debitedAccount =
                    debitAccount.withdraw(transferDto.getAmount());

            if (debitedAccount == null) {
                log.error("::: Transfer failed insufficient balance.");
                throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
            }

            final AccountModel creditedAccount = creditAccount.deposit(transferDto.getAmount());

            mAccountRepo.save(debitedAccount);
            mAccountRepo.save(creditedAccount);

            log.info("::: Account has been debited with iban: [{}]", debitedAccount.getIban());
            log.info("::: Account has been credited with iban: [{}] ", creditedAccount.getIban());
            log.info(mMessageConfig.getTransfer_successful());
            transactionModel.setStatus(TranxStatus.SUCCESSFUL.name());
            TransactionModel savedTransaction = mTransactionRepo.save(transactionModel);
            log.info("::: FundTransfer LogModel audited successfully");

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

        }catch (Exception ex) {
            ex.printStackTrace();
            throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
        }

    }


    public Account doFundAccount(TopupDto topupDto, HttpServletRequest request) throws  TransferNotValidException {
        log.info("::: In fundAccount.....");

        try {

            String token = request.getHeader("Authorization");
            System.out.println(token);
            if (token.contains("Bearer")) {
                token = token.split(" ")[1];
            }
            NotificationLog notificationModel = new NotificationLog();
            DataInfo data = new DataInfo();
            boolean isRequestValid = BaseUtil.isRequestSatisfied(topupDto);
            final TransactionModel transactionModel = TransactionMapper.mapToModel(topupDto, token);

            if (!isRequestValid) {
                log.error("::: TopUp request error with payload: [{}]", topupDto);
                throw new IllegalArgumentException("TopUp request error with payload");
            }

            final AccountModel creditAccount =
                    mAccountRepo.findAccountModelByIban(topupDto.getIban());

            log.info("::: About to validate Account owner.....");
            String userName = jwtUtil.extractUsername(token);
            System.out.println("JwtUser: " + userName);
            UserModel userModel = mUserRepo.findUserModelByEmail(userName);
            if (userModel == null) {
                log.error("::: Account broken, Invalid Account access");
                throw new IllegalAccessException("Account broken, Invalid Account access");
            }

            AccountModel topedAccount = creditAccount.deposit(topupDto.getAmount());
            mAccountRepo.save(topedAccount);

            transactionModel.setStatus(TranxStatus.SUCCESSFUL.name());
            TransactionModel savedLogModel = mTransactionRepo.save(transactionModel);
            log.info("::: FundAccount LogModel audited successfully with paylaod: [{}]",savedLogModel);

            String notificationMessage =
                    String.format("Your account %s has been credit with sum of [%s%s] only ",
                            creditAccount.getIban(),
                            creditAccount.getCurrency(),
                            topupDto.getAmount());
            data.setMessage(notificationMessage);
            notificationModel.setData(data);
            notificationModel.setInitiator(topedAccount.getUserModel().getFirstName().concat(" ")
                    .concat(topedAccount.getUserModel().getLastName()));
            notificationModel.setEventType(TransType.DEPOSIT.name());
            notificationModel.setChannelCode(ChannelConsts.VENDOR_CHANNEL);
            notificationModel.setTranxDate(new Date());
            notificationModel.setTranxRef(topupDto.getTranxRef());

            final NotificationLogEvent eventLog = new NotificationLogEvent(this, notificationModel);
            mEventPublisher.publishEvent(eventLog);
            log.info("::: notification sent successfully, data: [{}]",
                    eventLog.getNotificationLog());

            return AccountMapper.mapToDomain(topedAccount, TranxStatus.SUCCESSFUL.name());


        }catch (Exception ex) {
            ex.printStackTrace();
            throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
        }


    }


    public Account doFundWithdrawal(WithrawalDto withrawalDto, HttpServletRequest request) throws TransferNotValidException {
        log.info("::: In doFundWithdrawal.....");

        try {

            String token = request.getHeader("Authorization");
            if (token.contains("Bearer")) {
                token = token.split(" ")[1];
            }
            NotificationLog notificationLog = new NotificationLog();
            DataInfo data = new DataInfo();
            boolean isRequestValid = BaseUtil.isRequestSatisfied(withrawalDto);
            if (!isRequestValid) {
                log.error("::: FundWithdrawal request error with payload: [{}]", withrawalDto);
                throw new IllegalArgumentException("TopUp request error with payload");
            }

            AccountModel accountModel = mAccountRepo.findAccountModelByIban(withrawalDto.getIban());

            log.info("::: About to validate Account owner.....");
            String userName = jwtUtil.extractUsername(token);
            UserModel userModel = mUserRepo.findUserModelByEmail(userName);
            if (userModel == null) {
                log.error("::: Account broken, Invalid Account access");
                throw new IllegalAccessException("Account broken, Invalid Account access");
            }

            // Verify transaction token is valid
            if (!accountModel.getUserModel().getVerificationCode().equals(withrawalDto.getVerificationCode())) {
                log.error("::: VerificationCode error");
                throw new IllegalArgumentException("VerificationCode error");
            }

            // Very if account is active not close or debit freeze
            boolean isAccountStatusVerified =
                    BaseUtil.verifyAccount(accountModel);
            log.info("::: Account Status verified: [{}] :::", isAccountStatusVerified);

            // Check if user meet tierLevel specification
            boolean isTierLevelSatisfied =
                    TierLevelSpecUtil.validate(accountModel.getUserModel(), withrawalDto.getAmount());
            log.info(":::  Account tierLeveL satisfied: [{}] :::", isTierLevelSatisfied);

            if (!isAccountStatusVerified) {
                log.info("::: Transaction failed");
                throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
            }

            final TransactionModel transactionModel = TransactionMapper.mapToModel(withrawalDto, token);
            if (!userModel.getVerificationCode().equals(withrawalDto.getVerificationCode())) {
                log.error("::: Account broken, Invalid access...");
                throw  new IllegalAccessException("Account broken, Invalid access");
            }

            final AccountModel debitedAccount = accountModel.withdraw(withrawalDto.getAmount());

            if (debitedAccount == null) {
                log.error("::: Transfer failed insufficient balance.");
                throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
            }

            AccountModel debitedAccountSaved = mAccountRepo.save(debitedAccount);
            log.info("::: Account debit updated successfully with payload`; [{}]", debitedAccountSaved);

            transactionModel.setStatus(TranxStatus.SUCCESSFUL.name());
            TransactionModel savedLogModel = mTransactionRepo.save(transactionModel);
            log.info("::: FundWithdrawal LogModel audited successfully with paylaod: [{}]",savedLogModel);

            String notificationMessage =
                    String.format("Your account %s has been debited with sum of [%s%s] only ",
                            debitedAccount.getIban(),
                            debitedAccount.getCurrency(),
                            withrawalDto.getAmount());
            data.setMessage(notificationMessage);
            notificationLog.setData(data);
            notificationLog.setInitiator(debitedAccountSaved.getUserModel().getFirstName().concat(" ")
                    .concat(debitedAccountSaved.getUserModel().getLastName()));
            notificationLog.setEventType(TransType.WITHDRAWAL.name());
            notificationLog.setChannelCode(ChannelConsts.VENDOR_CHANNEL);
            notificationLog.setTranxDate(new Date());
            notificationLog.setTranxRef(withrawalDto.getTranxRef());

            final NotificationLogEvent
                    notificationLogEvent = new NotificationLogEvent(this, notificationLog);
            mEventPublisher.publishEvent(notificationLogEvent);
            log.info("::: notification sent successfully, data: [{}]",
                    notificationLogEvent.getNotificationLog());

            return AccountMapper.mapToDomain(debitedAccount, TranxStatus.SUCCESSFUL.name());


        }catch (Exception ex) {
            ex.printStackTrace();
            throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
        }

    }

    public Account doLiquidateAccount(LiquidateDto liquidateDto, HttpServletRequest request) throws TransferNotValidException {

        try {

            String token = request.getHeader("Authorization");
            if (token.contains("Bearer")) {
                token = token.split(" ")[1];
            }

            NotificationLog notificationLog = new NotificationLog();
            DataInfo data = new DataInfo();
            boolean isRequestValid = BaseUtil.isRequestSatisfied(liquidateDto);
            if (!isRequestValid) {
                log.error("::: AccountLiquidation request error with payload: [{}]", liquidateDto);
                throw new IllegalArgumentException("TopUp request error with payload");
            }

            AccountModel accountModel = mAccountRepo.findAccountModelByIban(liquidateDto.getIban());

            log.info("::: About to validate Account owner.....");
            String userName = jwtUtil.extractUsername(token);
            System.out.println("UserName: " + userName);
            UserModel userModel = mUserRepo.findUserModelByEmail(userName);
            if (userModel == null) {
                log.error("::: Account broken, Invalid Account access");
                throw new IllegalAccessException("Account broken, Invalid Account access");
            }

            // Very if account is active not close or debit freeze
            boolean isAccountStatusVerified = BaseUtil.verifyAccount(accountModel);
            log.info("::: Account Status verified: [{}] :::", isAccountStatusVerified);

            if (!isAccountStatusVerified) {
                log.info("::: Transaction failed");
                throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
            }

            final TransactionModel transactionModel = TransactionMapper.mapToModel(liquidateDto, token);
            if (!userModel.getVerificationCode().equals(liquidateDto.getVerificationCode())) {
                log.error("::: Account broken, Invalid access...");
                throw  new IllegalAccessException("Account broken, Invalid access");
            }

            final AccountModel debitedAccount = accountModel.withdraw(accountModel.getBalance());

            if (debitedAccount == null) {
                log.error("::: AccountLiquidation withdrawal failed, insufficient balance.");
                throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
            }

            debitedAccount.setIsLiquidated(true);
            AccountModel debitedAccountSaved = mAccountRepo.save(debitedAccount);
            log.info("::: Account debit updated successfully with payload`; [{}]", debitedAccountSaved);

            transactionModel.setStatus(TranxStatus.SUCCESSFUL.name());
            TransactionModel savedLogModel = mTransactionRepo.save(transactionModel);
            log.info("::: FundWithdrawal LogModel audited successfully with paylaod: [{}]",savedLogModel);

            String notificationMessage =
                    String.format("Your account %s has been liquidated with account total sum of [%s%s] only ",
                            debitedAccount.getIban(),
                            debitedAccount.getCurrency(),
                            accountModel.getBalance());
            data.setMessage(notificationMessage);
            notificationLog.setData(data);
            notificationLog.setInitiator(debitedAccountSaved.getUserModel().getFirstName().concat(" ")
                    .concat(debitedAccountSaved.getUserModel().getLastName()));
            notificationLog.setEventType(TransType.WITHDRAWAL.name());
            notificationLog.setChannelCode(ChannelConsts.VENDOR_CHANNEL);
            notificationLog.setTranxDate(new Date());
            notificationLog.setTranxRef(liquidateDto.getTranxRef());

            final NotificationLogEvent
                    notificationLogEvent = new NotificationLogEvent(this, notificationLog);
            mEventPublisher.publishEvent(notificationLogEvent);
            log.info("::: notification sent successfully, data: [{}]",
                    notificationLogEvent.getNotificationLog());

            return AccountMapper.mapToDomain(debitedAccount, TranxStatus.SUCCESSFUL.name());


        }catch (Exception ex) {
            ex.printStackTrace();
            throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
        }

    }

    public Investment doInvestment(InvestmentmentDto dto, HttpServletRequest request) throws TransferNotValidException {
        log.info("::: In doInvestment.....");

        try {

            String token = request.getHeader("Authorization");
            if (token.contains("Bearer")) {
                token = token.split(" ")[1];
            }

            NotificationLog notificationLog = new NotificationLog();
            DataInfo data = new DataInfo();
            boolean isInvementPayloadValid = BaseUtil.isRequestSatisfied(dto);
            if (!isInvementPayloadValid) {
                log.error("::: RequestPayload error with data: [{}]", dto);
                throw new IllegalArgumentException("RequestPayload error");
            }
            // Check if investment Type already Opened
            final InvestmentModel openInvestemnt =
                    mInvestmentRepo.getInvestmentModelByStatusAndPlan(TranxStatus.OPEN.name(), dto.getPlan());
            if (openInvestemnt != null) {
                log.error("::: Your investment is currently Open on this Plan, please try order plans . Thank you");
                throw new RuntimeException("Your investment is currently Open on this Plan, please try order plans ");
            }

            String userName = jwtUtil.extractUsername(token);
            AccountModel accountModel = mAccountRepo.findAccountModelByIban(dto.getIban());
            if (!userName.equals(accountModel.getUserModel().getEmail())) {
                log.info("::: Account broken, UnAuthorized account access");
                throw new IllegalAccessException("Account broken, UnAuthorized account access");
            }
            accountModel = accountModel.withdraw(dto.getAmount());
            if (accountModel ==  null || !dto.getBankCode().equals(accountModel.getBankCode())) {
                log.error("::: Insufficient Balance for Investment");
                throw new IllegalArgumentException("Insufficient Balance for Investment");
            }

            final InvestmentModel investmentModel = InvestmentMapper.mapDtoToModel(dto);
            final InvestmentModel invExistOnPending =
                    mInvestmentRepo.getInvestmentModelByStatusAndPlan(TranxStatus.PENDING.name(), dto.getPlan());
            if (invExistOnPending != null) {
                log.error(":::Investment exist but not yet active");
                throw new IllegalArgumentException("Investment of type exist but not yet active");
            }

            final InvestmentModel investedAmountModel = investmentModel.invest(dto.getAmount(), dto.getPlan(),
                    dto.getTranxRef());

            if (investedAmountModel == null) {
                log.error("::: Unable to process Investment. Please try again later");
                throw new IllegalArgumentException("Error occur while try to invest");

            }
            investedAmountModel.setUserModelInv(accountModel.getUserModel());
            InvestmentModel investedModel = mInvestmentRepo.save(investedAmountModel);
            log.info("::: Investemnt is successful with payload: [{}]", investedModel);

            String notificationMessage =
                    String.format("Your Investment from your account: [%s] is successful with Amount: [%s%s] only ",
                            accountModel.getIban(),
                            accountModel.getCurrency(),
                            dto.getAmount());

            data.setMessage(notificationMessage);
            notificationLog.setData(data);
            notificationLog.setInitiator(accountModel.getUserModel().getFirstName().concat(" ")
                    .concat(accountModel.getUserModel().getLastName()));
            notificationLog.setEventType(TransType.WITHDRAWAL.name());
            notificationLog.setChannelCode(ChannelConsts.VENDOR_CHANNEL);
            notificationLog.setTranxDate(investedModel.getCreatedAt());
            notificationLog.setTranxRef(investedModel.getInvestmentRefNo());

            final NotificationLogEvent
                    notificationLogEvent = new NotificationLogEvent(this, notificationLog);
            mEventPublisher.publishEvent(notificationLogEvent);
            log.info("::: notification sent successfully, data: [{}]",
                    notificationLogEvent.getNotificationLog());

            log.info("::: Investment is successful....");
            return InvestmentMapper.mapModelToDto(investedModel);

        }catch (Exception ex) {
            ex.printStackTrace();
            throw new TransferNotValidException(mMessageConfig.getTranfer_fail());
        }

    }

    //TODO: TopUp OPEN Investment
    //TODO: Withdraw ACCRUED_INTEREST on Investment

}
