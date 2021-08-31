package com.wayapaychat.bank.verifications;

import com.wayapaychat.bank.entity.AccountModel;
import com.wayapaychat.bank.enums.AccountStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountVerification {

    public static Boolean verifyAccount(AccountModel debitAccount, AccountModel creditAccount) {

        try {
            if (debitAccount.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name()) &&
                    creditAccount.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.name())) {

                log.info("::: Account with iban: [{}] is active to receive fund",
                         debitAccount.getIban());
                return true;
            }

            log.error("::: Account with iban: [{}] not verified :::", debitAccount.getIban());
            return false;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.error("::: Account with iban: [{}] not verified :::", debitAccount.getIban());

            return false;
        }

    }
}
