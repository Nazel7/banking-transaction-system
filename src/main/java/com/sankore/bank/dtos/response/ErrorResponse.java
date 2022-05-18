package com.sankore.bank.dtos.response;

import lombok.Data;

@Data
public class ErrorResponse {

    private String status;
    private String message;
    private Long timeStamp;
}
