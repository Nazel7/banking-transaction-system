package com.sankore.bank.dtos.response;

import com.sankore.bank.entities.models.TransactionModel;
import com.sankore.bank.entities.models.UserModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {

    private UUID id;
    private BigDecimal balance;
    private String iban;
    private Date createdAt;
    private Date updatedAt;
    private Set<TransactionModel> transactionHistories;
    private UserModel userModel;
    private String currency;
    private String status;

}