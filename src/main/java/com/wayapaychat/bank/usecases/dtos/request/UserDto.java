package com.wayapaychat.bank.usecases.dtos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String pin;
    private String bvn;

}
