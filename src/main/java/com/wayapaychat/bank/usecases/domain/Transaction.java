package com.wayapaychat.bank.usecases.domain;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {

    private final BigDecimal amount;
    private final String benefAccountNo;
    private final String debitAccountNo;
    private final String paymentReference;
    private final String tranCrncy;
    private final String tranNarration;
    private final String tranType;
    private final Long userId;
}
