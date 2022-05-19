package com.sankore.bank.entities.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.experimental.Tolerate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

@Entity
@Builder
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Access(AccessType.FIELD)
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private BigDecimal amount;
    private String benefAccountNo;
    private String debitAccountNo;
    private String paymentReference;
    private String tranCrncy;
    private String channelCode;
    private String tranNarration;
    private String tranType;
    private String tranxRef;
    private String userToken;
    private Long userId;
    private Boolean isLiquidate;
    private Boolean liquidityApproval;

    @CreationTimestamp
    private Date performedAt;

    @UpdateTimestamp
    private Date updatedAt;

    @Tolerate
    public TransactionModel(){

    }

}
