package com.wayapaychat.bank.entity.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.security.auth.login.AccountException;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Entity
@Table(name = "bank_account", indexes = {
        @Index(name = "account_iban_index", columnList = "account_iban")
})
@Getter
@Setter
@Access(AccessType.FIELD)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountModel {

    private static final BigDecimal OVER_DRAFT = new BigDecimal(100);

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Setter(AccessLevel.NONE)
    private BigDecimal balance;

    @Setter(AccessLevel.NONE)
    @Column(name = "account_iban")
    private String iban;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @OneToOne
    @JoinColumn
    private UserModel userModel;

    @Setter(AccessLevel.NONE)
    private String currency;

    private String status;

    @Tolerate
    public AccountModel() {

    }

    public AccountModel(String iban, String currency, String status) {

        this.iban = iban;
        this.currency = currency;
        this.status = status;
    }

    public AccountModel deposit(BigDecimal amount) {

        this.balance = balance.add(amount);

        return this;
    }

    public AccountModel withdraw(BigDecimal amount)
            throws AccountException {

        if (balance.add(OVER_DRAFT).compareTo(amount) > 0) {
            this.balance = this.balance.subtract(amount);
        } else {
            throw new AccountException("insufficient account balance !");
        }
        return this;
    }

    public String printStatement() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AccountModel accountModel = (AccountModel) o;
        return Objects.equals(iban, accountModel.iban);
    }

    @Override
    public int hashCode() {

        return Objects.hash(balance, iban, createdAt);
    }

}
