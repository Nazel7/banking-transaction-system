package com.sankore.bank.dtos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {

    private String bvn;
    private String homeAddress;
    private String email;
    private String verificationCode;
    private Boolean verifiedEmail;
    private Boolean verifiedPhone;
    private Boolean verifiedBvn;
    private Boolean verifiedHomeAddress;


}
