package com.sankore.bank.dtos.request;

import lombok.Data;

@Data
public class OriginatorKyc {

    private String email;
    private String phoneNum;
    private String iban;
    private String bankCode;
    private String name;
}
