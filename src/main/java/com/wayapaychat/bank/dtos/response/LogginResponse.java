package com.wayapaychat.bank.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogginResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String token;
    private String phone;
    private String pin;
}
