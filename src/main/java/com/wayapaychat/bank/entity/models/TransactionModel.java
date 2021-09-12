package com.wayapaychat.bank.entity.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Tolerate;

@Entity
@Builder
@Table(name = "transactions")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private String benefAccountNo;
    private String debitAccountNo;
    private String paymentReference;
    private String tranCrncy;
    private String tranNarration;
    private String tranType;
    private Long userId;

    @CreationTimestamp
    private Date performedAt;

    @Tolerate
    public TransactionModel(){

    }

}
