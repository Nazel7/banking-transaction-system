package com.sankore.bank.dtos.response;

import com.sankore.bank.entities.models.AccountModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String homeAddress;
    private String phone;
    private AccountModel account;
    private String tierLevel;
    private Boolean verifiedEmail;
    private Boolean verifiedPhone;
    private Boolean verifiedHomeAddress;
    private Boolean verifiedBvn;
    private String verificationCode;

}
