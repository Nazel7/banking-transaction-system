package com.wayapaychat.bank.mappers;


import com.wayapaychat.bank.entity.AccountModel;
import com.wayapaychat.bank.usecases.domain.Account;

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
