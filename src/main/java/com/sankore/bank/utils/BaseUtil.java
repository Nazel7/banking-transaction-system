package com.sankore.bank.utils;

import com.sankore.bank.dtos.request.SignUpDto;
import com.sankore.bank.dtos.request.TransferDto;
import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.AccountStatus;
import com.sankore.bank.enums.Currency;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Objects;

@Slf4j
public class BaseUtil {

    public static AccountModel generateAccountNumber(UserModel userModel) {

        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(num.length);
            sb.append(num[n]);
        }

        String iban = sb.toString();

        AccountModel accountModel = new AccountModel(iban,
                Currency.NGN.name(),
                AccountStatus.ACTIVE.name());

        log.info("::: Account with Iban: [{}] created for user with email: [{}]",
                accountModel.getIban(),
                userModel.getEmail());

        return accountModel;

    }

    public static boolean isSignUpSatisfied(SignUpDto signUpDto) {
        try{

            Objects.requireNonNull(signUpDto.getFirstName());
            Objects.requireNonNull(signUpDto.getLastName());
            Objects.requireNonNull(signUpDto.getAddress());
            Objects.requireNonNull(signUpDto.getPassword());
            Objects.requireNonNull(signUpDto.getVerificationCode());
            Objects.requireNonNull(signUpDto.getPhone());
            Objects.requireNonNull(signUpDto.getEmail());
            if (!signUpDto.getVerifiedPhone()) {
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

    public static boolean isTransferSatisfied(TransferDto transaction) {
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
