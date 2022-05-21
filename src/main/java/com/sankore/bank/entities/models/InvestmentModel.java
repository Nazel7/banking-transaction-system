package com.sankore.bank.entities.models;

import com.sankore.bank.utils.TransactionObjFormatter;
import lombok.*;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(indexes = {
        @Index(name = "acct_iban_index", columnList = "account_iban"),
        @Index(name = "plan_index", columnList = "plan"),
        @Index(name = "ref_index", columnList = "investmentRefNo"),
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@Access(AccessType.FIELD)
public class InvestmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;

    @Setter(AccessLevel.NONE)
    private BigDecimal investedAmount;

    private BigDecimal accruedBalance;

    @Setter(AccessLevel.NONE)
    @Column(name = "account_iban")
    private String iban;

    private Double intRateMonth;
    private Double intRateYear;
    private String bvn;
    private String currency;
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
    private Date updatedAt;

    @Tolerate
    public InvestmentModel() {
        this.investedAmount = new BigDecimal("0.00");
    }

    public InvestmentModel(BigDecimal investedAmount, BigDecimal accruedBalance,
                           Double intRateMonth, Double intRateYear) {

        this.accruedBalance = accruedBalance;
        this.investedAmount = investedAmount;
        this.intRateMonth = intRateMonth;
        this.intRateYear = intRateYear;

    }

    public InvestmentModel invest(BigDecimal investemntAmount, String plan, String investmentRefNo) {
        if (!investmentRefNo.equals(this.investmentRefNo)) {
            throw new RuntimeException("Error accessing investment, Iban is not owned");
        }
        if (this.investedAmount == null) {
            this.investedAmount = new BigDecimal("0.00");
        }
        this.investedAmount = this.investedAmount.add(investemntAmount, new MathContext(4));
        this.plan = plan;

        return this;
    }

    public InvestmentModel topUpInvestment(BigDecimal topUpAmount, String investmentRefNo) {
        if (!investmentRefNo.equals(this.investmentRefNo)) {
            throw new RuntimeException("Error accessing investment, Iban is not owned");
        }
        this.investedAmount = investedAmount.add(topUpAmount);

        return this;
    }

    public BigDecimal tranferAccruedInterest(BigDecimal interestAmount, String investmentRefNo) {
        if (!investmentRefNo.equals(this.investmentRefNo)) {
            throw new RuntimeException("Error accessing investment, Iban is not owned");
        }
        BigDecimal currentInterestAccrued = this.accruedBalance.subtract(investedAmount);
        if (currentInterestAccrued.compareTo(interestAmount) > 0) {
            this.accruedBalance = this.accruedBalance.subtract(interestAmount);
            return interestAmount;
        } else {
            throw new RuntimeException("Not a valid amount for profit withdrawal");
        }
    }


    public InvestmentModel extendInvestment(String extentionDate) throws ParseException {
        final Date date = TransactionObjFormatter.getDate(extentionDate);
        this.setEndDate(date);

        return this;
    }

    public InvestmentModel doAccruedInterest(BigDecimal dailyAccruedAmount) throws ParseException {

        this.accruedBalance = this.accruedBalance.add(dailyAccruedAmount, new MathContext(4));

        return this;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        InvestmentModel investmentModel = (InvestmentModel) o;
        return Objects.equals(iban, investmentModel.iban);
    }

    @Override
    public int hashCode() {

        return Objects.hash(investedAmount, iban, createdAt);
    }

}
