package com.sankore.bank.dtos.request;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class TransferDto {

    private BigDecimal amount;
    private String benefAccountNo;
    private String debitAccountNo;
    private String paymentReference;
    private String tranCrncy;
    private String tranNarration;
    private String tranType;
    private String tranxRef;
    private String channelCode;
    private Long userId;
    private String verificationCode;

    @Tolerate
    public TransferDto(){

    }
}
