package com.sankore.bank.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WithrawalDto {

    private BigDecimal amount;
    private String iban;
    private String tranxRef;
    private String channelCode;
    private String tranxType;
    private String currency;
    private String tranxNaration;
    private String verificationCode;
}
