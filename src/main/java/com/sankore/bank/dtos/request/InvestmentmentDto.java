package com.sankore.bank.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestmentmentDto {

    private BigDecimal amount;
    private String iban;
    private String firstName;
    private String lastName;
    private String middleName;
    private String bankCode;
    private String currency;
    private String plan;
    private String tranxRef;
    private String startDate;
    private String endDate;
}
