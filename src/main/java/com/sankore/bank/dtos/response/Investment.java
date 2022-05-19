package com.sankore.bank.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sankore.bank.entities.models.UserModel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @Setter(AccessLevel.NONE)
    @Column(name = "account_iban")
    private String iban;

    private Double intRateMonth;
    private Double intRateYear;
    private String bvn;
    private String firName;
    private String lastName;
    private String middleName;
    private String bankCode;
    private String plan;
    private String investmentRefNo;
    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JoinColumn
    private UserModel userModelInv;

    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private String updatedAt;
}
