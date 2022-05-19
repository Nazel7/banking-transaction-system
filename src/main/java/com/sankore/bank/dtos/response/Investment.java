package com.sankore.bank.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Investment {

    private Long id;
    private String status;
    private BigDecimal investedAmount;
    private BigDecimal accruedBalance;
    private String iban;
    private Double intRateMonth;
    private Double intRateYear;
    private String firName;
    private String lastName;
    private String middleName;
    private String bankCode;
    private String plan;
    private String investmentRefNo;
    private Date startDate;
    private Date endDate;

}
