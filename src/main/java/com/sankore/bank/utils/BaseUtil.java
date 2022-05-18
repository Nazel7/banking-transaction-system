package com.sankore.bank.utils;

import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.AccountStatus;
import com.sankore.bank.enums.Currency;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

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

}
