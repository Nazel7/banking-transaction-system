package com.sankore.bank.entities.builder;

import com.sankore.bank.dtos.request.InvestmentmentDto;
import com.sankore.bank.dtos.response.Account;
import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.enums.TranxStatus;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

public class InvestmentMapper {

    public static InvestmentModel mapDtoToModel(AccountModel accountModel, InvestmentmentDto investmentmentDto) {



        @Setter(AccessLevel.NONE)
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

        @CreationTimestamp
        private Date createdAt;
        @UpdateTimestamp
        private String updatedAt;

        return InvestmentModel
                .builder()
                .status(TranxStatus.OPEN.name())
                .investedAmount(investmentmentDto.getAmount())
                .iban(investmentmentDto.getIban())

                .build();

    }
}
