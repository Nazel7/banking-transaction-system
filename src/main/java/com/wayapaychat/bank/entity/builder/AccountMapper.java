package com.wayapaychat.bank.entity.builder;


import com.wayapaychat.bank.entity.models.AccountModel;
import com.wayapaychat.bank.dtos.response.Account;

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
