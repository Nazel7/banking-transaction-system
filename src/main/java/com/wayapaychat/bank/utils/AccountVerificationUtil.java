package com.wayapaychat.bank.utils;

import com.wayapaychat.bank.entity.models.AccountModel;
import com.wayapaychat.bank.enums.AccountStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountVerificationUtil {

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
}
