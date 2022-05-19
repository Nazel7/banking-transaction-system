package com.sankore.bank.utils;

import com.sankore.bank.contants.ChannelConsts;
import com.sankore.bank.dtos.request.*;
import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.AccountStatus;
import com.sankore.bank.enums.AccountType;
import com.sankore.bank.enums.Currency;
import com.sankore.bank.enums.InvestmentPlan;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Objects;

@Slf4j
public class BaseUtil {

    public static AccountModel generateAccountNumber(UserModel userModel, String accounType) {
        log.info("::: In generateAccountNumber.....");
        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(num.length);
            sb.append(num[n]);
        }

        String iban = sb.toString();
        log.info("::: Iban: [{}]", iban);
        AccountType accountType = AccountType.getAccountType(accounType);
        log.info("::: AccountType: [{}]", accountType);

        AccountModel accountModel = new AccountModel(iban, Currency.NGN.name(),
                AccountStatus.ACTIVE.name(), accounType);
        accountModel.setAccountType(accountType.name());
        accountModel.setBvn(userModel.getBvn());

        log.info("::: Account with Iban: [{}] created for user with email: [{}]",
                accountModel.getIban(),
                userModel.getEmail());

        return accountModel;

    }

    public static boolean isRequestSatisfied(SignUpDto signUpDto) {
        log.info("::: In SignUp payload validation.....");
        try {

            Objects.requireNonNull(signUpDto.getFirstName());
            Objects.requireNonNull(signUpDto.getLastName());
            Objects.requireNonNull(signUpDto.getAddress());
            Objects.requireNonNull(signUpDto.getPassword());
            Objects.requireNonNull(signUpDto.getVerificationCode());
            Objects.requireNonNull(signUpDto.getPhone());
            Objects.requireNonNull(signUpDto.getEmail());
            Objects.requireNonNull(signUpDto.getAccountType());
            Objects.requireNonNull(signUpDto.getVerifiedEmail());
            Objects.requireNonNull(signUpDto.getVerifiedHomeAddress());
            Objects.requireNonNull(signUpDto.getVerifiedBvn());
            Objects.requireNonNull(signUpDto.getVerifiedPhone());
            boolean isEmailValid = TransactionObjFormatter.isEmailMatch(signUpDto.getEmail());
            boolean isPhoneNumValid = TransactionObjFormatter.isMatchNigerianPhoneNum(signUpDto.getPhone());
            if (!isEmailValid || !isPhoneNumValid) {
                log.error("::: Email| PhoneNum not valid");
                return false;
            }
            if (!signUpDto.getVerifiedPhone()) {
                log.error("::: Phone not verified");
                return false;
            }

            log.info("::: Satisfied requestBody: [{}] :::", signUpDto);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Unsatisfied requestBody [{}]:::", signUpDto);
            return false;
        }

    }

    public static boolean isRequestSatisfied(TransferDto transferDto) {
        log.info("::: In Transfer payload validation.....");
        try {

            Objects.requireNonNull(transferDto.getAmount());
            Objects.requireNonNull(transferDto.getBenefAccountNo());
            Objects.requireNonNull(transferDto.getDebitAccountNo());
            Objects.requireNonNull(transferDto.getPaymentReference());
            Objects.requireNonNull(transferDto.getTranxRef());
            Objects.requireNonNull(transferDto.getTranCrncy());
            Objects.requireNonNull(transferDto.getTranType());
            Objects.requireNonNull(transferDto.getChannelCode());
            Objects.requireNonNull(transferDto.getUserId());

            log.info("::: Satisfied requestBody: [{}] :::", transferDto);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Unsatisfied requestBody [{}]:::", transferDto);
            return false;
        }

    }

    public static boolean isRequestSatisfied(TopupDto topupDto) {
        log.info("::: In TopUp payload validation.....");
        try {

            Objects.requireNonNull(topupDto.getAmount());
            Objects.requireNonNull(topupDto.getIban());
            Objects.requireNonNull(topupDto.getChannelCode());
            Objects.requireNonNull(topupDto.getTranxRef());
            Objects.requireNonNull(topupDto.getTranxType());
            if (ChannelConsts.ATM_CHANNEL.equals(topupDto.getChannelCode()) ||
                    ChannelConsts.NIBSS_CHANNEL.equals(topupDto.getChannelCode()) ||
                    ChannelConsts.INTRA_CHANNEL.equals(topupDto.getChannelCode())) {
                Objects.requireNonNull(topupDto.getOriginatorKyc().getBankCode());
                Objects.requireNonNull(topupDto.getOriginatorKyc().getPhoneNum());
                Objects.requireNonNull(topupDto.getOriginatorKyc().getIban());
                Objects.requireNonNull(topupDto.getOriginatorKyc().getName());
            }


            log.info("::: Satisfied requestBody: [{}] :::", topupDto);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Unsatisfied requestBody [{}]:::", topupDto);
            return false;
        }

    }

    public static boolean isRequestSatisfied(WithrawalDto withrawalDto) {
        log.info("::: In Withdrawal payload validation.....");
        try {

            Objects.requireNonNull(withrawalDto.getAmount());
            Objects.requireNonNull(withrawalDto.getIban());
            Objects.requireNonNull(withrawalDto.getChannelCode());
            Objects.requireNonNull(withrawalDto.getTranxRef());
            Objects.requireNonNull(withrawalDto.getTranxType());
            Objects.requireNonNull(withrawalDto.getChannelCode());
            Objects.requireNonNull(withrawalDto.getTranxCrncy());
            Objects.requireNonNull(withrawalDto.getVerificationCode());

            log.info("::: Satisfied requestBody: [{}] :::", withrawalDto);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Unsatisfied requestBody [{}]:::", withrawalDto);
            return false;
        }

    }

    public static boolean isRequestSatisfied(LiquidateDto liquidateDto) {
        log.info("::: In Liquidity payload validation.....");
        try {

            Objects.requireNonNull(liquidateDto.getIban());
            Objects.requireNonNull(liquidateDto.getChannelCode());
            Objects.requireNonNull(liquidateDto.getTranxRef());
            Objects.requireNonNull(liquidateDto.getTranxType());
            Objects.requireNonNull(liquidateDto.getChannelCode());
            Objects.requireNonNull(liquidateDto.getTranxCrncy());
            Objects.requireNonNull(liquidateDto.getVerificationCode());
            if (!liquidateDto.getIsLiquidate() || !liquidateDto.getIsLiquidityApproval()) {
                log.error("::: Liquidity key error.");
                return false;
            }

            log.info("::: Satisfied requestBody: [{}] :::", liquidateDto);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Unsatisfied requestBody [{}]:::", liquidateDto);
            return false;
        }

    }

    private BigDecimal amount;
    private String iban;
    private String firName;
    private String lastName;
    private String middleName;
    private String category;
    private String tranxRef;
    private String startDate;
    private String endDate;

    public static boolean isRequestSatisfied(InvestmentmentDto investmentmentDto) {
        log.info("::: In Investment payload validation.....");
        try {

            Objects.requireNonNull(investmentmentDto.getIban());
            Objects.requireNonNull(investmentmentDto.getAmount());
            Objects.requireNonNull(investmentmentDto.getTranxRef());
            Objects.requireNonNull(investmentmentDto.getPlan());
            Objects.requireNonNull(investmentmentDto.getBankCode());
            Objects.requireNonNull(investmentmentDto.getCurrency());
            Objects.requireNonNull(investmentmentDto.getStartDate());
            Objects.requireNonNull(investmentmentDto.getEndDate());
            Objects.requireNonNull(investmentmentDto.getFirName());
            Objects.requireNonNull(investmentmentDto.getLastName());

            InvestmentPlan investmentPlan = InvestmentPlan.getInvestmentPlan(investmentmentDto.getPlan());
            if (!(investmentPlan.getMinAmount() <= investmentmentDto.getAmount().doubleValue())) {
                log.error("::: Invalid Investment Amount, please try again later......");
                return false;
            }

            log.info("::: Satisfied requestBody: [{}] :::", investmentmentDto);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Unsatisfied requestBody [{}]:::", investmentmentDto);
            return false;
        }

    }


    public static boolean isTransferSatisfied(TransferDto transaction) {
        log.info("::: In Transfer payload validation.....");
        try {

            Objects.requireNonNull(transaction.getAmount());
            Objects.requireNonNull(transaction.getBenefAccountNo());
            Objects.requireNonNull(transaction.getTranType());
            Objects.requireNonNull(transaction.getDebitAccountNo());
            Objects.requireNonNull(transaction.getTranCrncy());
            Objects.requireNonNull(transaction.getPaymentReference());
            Objects.requireNonNull(transaction.getUserId());
            Objects.requireNonNull(transaction.getTranxRef());
            Objects.requireNonNull(transaction.getChannelCode());
            Objects.requireNonNull(transaction.getVerificationCode());


            log.info("::: Transaction requestBody is satisfied with payload: [{}]", transaction);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Transaction requestBody is not satisfied with payload: [{}]", transaction);
            return false;
        }

    }

    public static Boolean verifyAccount(AccountModel debitAccount, AccountModel creditAccount) {

        try {
            if (debitAccount.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name()) &&
                    creditAccount.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name())) {

                log.info("::: Account with iban: [{}] is active to receive fund",
                        creditAccount.getIban());
                return true;
            }

            log.error("::: Account with iban: [{}] not verified :::", creditAccount.getIban());
            return false;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.error("::: Account with iban: [{}] not verified :::", creditAccount.getIban());

            return false;
        }

    }

    public static Boolean verifyAccount(AccountModel creditAccount) {

        try {
            if (creditAccount.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name())) {

                log.info("::: Account is active with iban: [{}] ",
                        creditAccount.getIban());
                return true;
            }

            log.error("::: Account not verified with iban: [{}]", creditAccount.getIban());
            return false;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.error("::: Account not verified with iban: [{}]", creditAccount.getIban());

            return false;
        }

    }

}
