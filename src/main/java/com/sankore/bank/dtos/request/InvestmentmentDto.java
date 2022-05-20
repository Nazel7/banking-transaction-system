package com.sankore.bank.dtos.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InvestmentmentDto {

    private BigDecimal amount;
    private String iban;
    private String firName;
    private String lastName;
    private String middleName;
    private String bankCode;
    private String currency;
    private String plan;
    private String tranxRef;
    private Date startDate;
    private Date endDate;
}
