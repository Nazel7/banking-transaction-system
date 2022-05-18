package com.sankore.bank.entities.builder;


import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.dtos.response.Account;

public class AccountMapper {

    public static Account mapToDomain(AccountModel model){

        return Account
                .builder()
                .currency(model.getCurrency())
                .createdAt(model.getCreatedAt())
                .iban(model.getIban())
                .balance(model.getBalance())
                .status(model.getStatus())
                .updatedAt(model.getUpdatedAt())
                .build();
    }
}
