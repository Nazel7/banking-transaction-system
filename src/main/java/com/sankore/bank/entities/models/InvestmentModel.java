package com.sankore.bank.entities.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(indexes = {
        @Index(name = "acct_iban_index", columnList = "iban"),
        @Index(name = "plan_index", columnList = "plan"),
        @Index(name = "ref_index", columnList = "tranxRef"),
})
@Getter
@Setter
@Access(AccessType.FIELD)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvestmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private BigDecimal investedAount;
    private BigDecimal accruedBalance;
    private String iban;
    private Double intRateMonth;
    private Double intRateYear;
    private String bvn;
    private String firName;
    private String lastName;
    private String middleName;
    private String bankCode;
    private String plan;
    private String tranxRef;
    private Date startDate;
    private Date endDate;

    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private String updatedAt;
}
