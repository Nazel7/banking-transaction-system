package com.wayapaychat.bank.usecases.domain;

import com.wayapaychat.bank.entity.AccountModel;

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
    private String phone;
    private AccountModel account;
    private String tierLevel;
    private Boolean verifiedEmail;
    private Boolean verifiedPhone;
    private Boolean verifiedBvn;
    private String verificationCode;

}
