package com.sankore.bank.dtos.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    private String status;
    private final BigDecimal amount;
    private final String benefAccountNo;
    private final String debitAccountNo;
    private final String paymentReference;
    private final String tranCrncy;
    private final String tranNarration;
    private final String tranType;
    private final Long userId;
    private final String tranxRef;
}
