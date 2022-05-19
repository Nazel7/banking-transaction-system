package com.sankore.bank.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sankore.bank.entities.models.AccountModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
