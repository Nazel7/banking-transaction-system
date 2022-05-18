package com.sankore.bank.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
