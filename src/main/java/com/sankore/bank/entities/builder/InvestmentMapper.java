package com.sankore.bank.entities.builder;

import com.sankore.bank.dtos.request.InvestmentmentDto;
import com.sankore.bank.dtos.response.Investment;
import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.enums.InvestmentPlan;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.utils.TransactionObjFormatter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
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




}
