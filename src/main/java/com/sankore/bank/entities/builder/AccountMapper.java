package com.sankore.bank.entities.builder;


import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.dtos.response.Account;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.tables.records.BankAccountRecord;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class AccountMapper {

    public static Account mapToDomain(AccountModel model, String tranxStatus){

        return Account
                .builder()
                .currency(model.getCurrency())
                .createdAt(model.getCreatedAt())
                .iban(model.getIban())
                .balance(model.getBalance().doubleValue())
                .status(tranxStatus ==  null ? TranxStatus.PENDING.name(): tranxStatus)
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public static AccountModel mapRecordToModel(BankAccountRecord accountRecord) {

        @Id
        @Basic(optional = false)
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @Setter(AccessLevel.NONE)
        private BigDecimal balance;

        private String bankCode;

        @Setter(AccessLevel.NONE)
        @Column(name = "account_iban")
        private String iban;

        private String bvn;

        private String accountType;

        private Boolean isLiquidated;

        private Boolean isLiquidityApproval;

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


    }
}
