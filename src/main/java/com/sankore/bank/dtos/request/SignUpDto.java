package com.sankore.bank.dtos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String homeAddress;
    private String accountType;
    private String pin;
    private String bvn;
    private String verificationCode;
    private Boolean verifiedEmail;
    private Boolean verifiedPhone;
    private Boolean verifiedBvn;
    private Boolean verifiedHomeAddress;

}
