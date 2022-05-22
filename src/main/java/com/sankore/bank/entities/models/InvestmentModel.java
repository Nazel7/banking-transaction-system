package com.sankore.bank.entities.models;

import com.sankore.bank.utils.TransactionObjFormatter;
import lombok.*;
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
    @Column(unique = true, length = 30)
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

    public InvestmentModel(BigDecimal investedAmount, BigDecimal accruedBalance,
                           Double intRateMonth, Double intRateYear) {

        this.accruedBalance = accruedBalance;
        this.investedAmount = investedAmount;
        this.intRateMonth = intRateMonth;
        this.intRateYear = intRateYear;

    }

    public InvestmentModel() {

    }

    public InvestmentModel invest(BigDecimal investemntAmount, String plan, String investmentRefNo) {
        if (!investmentRefNo.equals(this.investmentRefNo)) {
            throw new RuntimeException("Error accessing investment, Iban is not owned");
        }
        if (this.investedAmount == null) {
            this.investedAmount = new BigDecimal("0.00");
        }
        this.investedAmount = this.investedAmount.add(investemntAmount);
        this.plan = plan;

        return this;
    }

    // TODO: add to currently open investment
    public InvestmentModel topUpInvestment(BigDecimal topUpAmount, String investmentRefNo) {
        if (!investmentRefNo.equals(this.investmentRefNo)) {
            throw new RuntimeException("Error accessing investment, Iban is not owned");
        }
        this.investedAmount = investedAmount.add(topUpAmount);

        return this;
    }

    //TODO: Transfer accrued Interest to savings account
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

    //TODO: extend Investemt CLOSE date to continue with the currently running investment.
    public InvestmentModel extendInvestment(String extentionDate) throws ParseException {
        Date date = TransactionObjFormatter.getDate(extentionDate);
        if (this.getEndDate().getTime() < date.getTime()) {
            this.setEndDate(date);
            return this;
        }
        else {
            throw new RuntimeException("Not a valid extension Date for investment");
        }
    }

    public InvestmentModel doAccruedInterest(BigDecimal dailyAccruedAmount) {
        System.out.println("DailyInterest in Method: " + dailyAccruedAmount);
        if (this.accruedBalance == null) {
            this.accruedBalance = new BigDecimal("0.00");
        }

        this.accruedBalance = this.accruedBalance.add(dailyAccruedAmount);

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

        return Objects.hash(accruedBalance, iban, createdAt);
    }


}
