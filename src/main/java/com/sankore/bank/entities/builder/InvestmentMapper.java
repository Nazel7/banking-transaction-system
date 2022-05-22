package com.sankore.bank.entities.builder;

import com.sankore.bank.dtos.request.InvestmentmentDto;
import com.sankore.bank.dtos.response.Investment;
import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.InvestmentPlan;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.tables.records.InvestmentModelRecord;
import com.sankore.bank.utils.TransactionObjFormatter;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class InvestmentMapper {

    public static InvestmentModel mapDtoToModel(InvestmentmentDto investmentmentDto) throws ParseException {

        InvestmentPlan investmentPlan = InvestmentPlan.getInvestmentPlan(investmentmentDto.getPlan());
        Date startDateInv = TransactionObjFormatter.getDate(investmentmentDto.getStartDate());
        Date endDateInv = TransactionObjFormatter.getDate(investmentmentDto.getEndDate());
        String invStatus = TranxStatus.PENDING.name();
        if (startDateInv.getTime() <= System.currentTimeMillis()) {
            invStatus = TranxStatus.OPEN.name();
        }
        return InvestmentModel
                .builder()
                .status(invStatus)
                .iban(investmentmentDto.getIban())
                .plan(investmentmentDto.getPlan())
                .intRateMonth(investmentPlan.getIntRateMonth())
                .lastName(investmentmentDto.getLastName())
                .firName(investmentmentDto.getFirstName())
                .middleName(investmentmentDto.getMiddleName())
                .investmentRefNo(investmentmentDto.getTranxRef())
                .accruedBalance(investmentmentDto.getAmount())
                .bankCode(investmentmentDto.getBankCode())
                .intRateYear(investmentPlan.getIntRateMonth() * 12)
                .startDate(startDateInv)
                .currency(investmentmentDto.getCurrency())
                .endDate(endDateInv)
                .build();

    }


    public static Investment mapModelToDto(InvestmentModel model) {

        return Investment
                .builder()
                .id(model.getId())
                .status(model.getStatus())
                .investedAmount(model.getInvestedAmount())
                .accruedBalance(model.getAccruedBalance())
                .iban(model.getIban())
                .intRateMonth(model.getIntRateMonth())
                .intRateYear(model.getIntRateYear())
                .firName(model.getFirName())
                .lastName(model.getLastName())
                .middleName(model.getMiddleName())
                .bankCode(model.getBankCode())
                .plan(model.getPlan())
                .investmentRefNo(model.getInvestmentRefNo())
                .startDate(model.getStartDate())
                .endDate(model.getEndDate())
                .build();
    }

    public static InvestmentModel mapRecordToModel(InvestmentModelRecord record, UserModel userModel) {

        return InvestmentModel
                .builder()
                .id(record.getId())
                .status(record.getStatus())
                .investedAmount(record.getInvestedAmount())
                .accruedBalance(record.getAccruedBalance())
                .iban(record.getAccountIban())
                .intRateMonth(record.getIntRateMonth())
                .intRateYear(record.getIntRateYear())
                .bvn(record.getBvn())
                .currency(record.getCurrency())
                .firName(record.getFirName())
                .lastName(record.getLastName())
                .middleName(record.getMiddleName())
                .bankCode(record.getBankCode())
                .plan(record.getPlan())
                .investmentRefNo(record.getInvestmentRefNo())
                .startDate(Date.from(record.getStartDate().toLocalDate()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .endDate(Date.from(record.getEndDate().toLocalDate()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .userModelInv(userModel)
                .build();

    }




}
