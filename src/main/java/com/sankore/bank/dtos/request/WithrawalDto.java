package com.sankore.bank.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithrawalDto {

    private BigDecimal amount;
    private String iban;
    private String tranxRef;
    private String channelCode;
    private String tranxType;
    private String tranxCrncy;
    private String tranxNaration;
    private String verificationCode;
}