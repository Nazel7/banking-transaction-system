package com.wayapaychat.bank.usecases.dtos.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TopupDto {

    private BigDecimal amount;
    private String iban;
}
