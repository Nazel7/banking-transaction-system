package com.sankore.bank.entities.builder;


import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.dtos.response.Account;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.tables.records.BankAccountRecord;
import com.sankore.bank.tables.records.CustomersRecord;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class AccountMapper {

    public static Account mapToDomain(AccountModel model, String tranxStatus){

        return Account
                .builder()
                .currency(model.getCurrency())
                .createdAt(model.getCreatedAt())
                .iban(model.getIban())
                .balance(model.getBalance().doubleValue())
                .status(tranxStatus ==  null ? TranxStatus.PENDING.name(): tranxStatus)
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public static AccountModel mapRecordToModel(BankAccountRecord accountRecord, UserModel userModel) {

        return AccountModel
                .builder()
                .id(accountRecord.getId())
                .balance(accountRecord.getBalance())
                .bankCode(accountRecord.getBankCode())
                .iban(accountRecord.getAccountIban())
                .bvn(accountRecord.getBvn())
                .accountType(accountRecord.getAccountType())
                .isLiquidated(accountRecord.getIsLiquidated())
                .isLiquidityApproval(accountRecord.getIsLiquidityApproval())
                .createdAt(Date.from(accountRecord.getCreatedAt().toLocalDate()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .updatedAt(Date.from(accountRecord.getUpdatedAt().toLocalDate()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .currency(accountRecord.getCurrency())
                .status(accountRecord.getStatus())
                .userModel(userModel)
                .accountType(accountRecord.getAccountType())
                .build();
    }

    public static BankAccountRecord mapModelToAcctRecord(AccountModel accountRecord, CustomersRecord userModel) {

        BankAccountRecord record = new BankAccountRecord();
        record.setBalance(accountRecord.getBalance());
        record.setBankCode(accountRecord.getBankCode());
        record.setAccountIban(accountRecord.getIban());
        record.setBvn(accountRecord.getBvn());
        record.setAccountType(accountRecord.getAccountType());
        record.setIsLiquidated(accountRecord.getIsLiquidated());
        record.setIsLiquidityApproval(accountRecord.getIsLiquidityApproval());
        record.setCurrency(accountRecord.getCurrency());
        record.setStatus(accountRecord.getStatus());
        record.setUserModelId(userModel.getId());
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());

        return record;

    }


}
