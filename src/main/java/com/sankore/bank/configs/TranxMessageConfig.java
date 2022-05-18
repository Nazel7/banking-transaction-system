package com.sankore.bank.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("transaction")
public class TranxMessageConfig {

    private String tranfer_fail;
    private String transfer_successful;
}
