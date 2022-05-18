package com.sankore.bank.entities.builder;


import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.dtos.response.Account;
import com.sankore.bank.enums.TranxStatus;

public class AccountMapper {

    public static Account mapToDomain(AccountModel model, String tranxStatus){

        return Account
                .builder()
                .currency(model.getCurrency())
                .createdAt(model.getCreatedAt())
                .iban(model.getIban())
                .balance(model.getBalance())
                .status(tranxStatus ==  null ? TranxStatus.PENDING.name(): tranxStatus)
                .updatedAt(model.getUpdatedAt())
                .build();
    }
}
