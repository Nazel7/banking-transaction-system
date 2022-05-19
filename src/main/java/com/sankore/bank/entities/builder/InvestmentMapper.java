package com.sankore.bank.entities.builder;

import com.sankore.bank.dtos.request.InvestmentmentDto;
import com.sankore.bank.entities.models.AccountModel;
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

        return InvestmentModel
                .builder()
                .status(TranxStatus.OPEN.name())
                .iban(investmentmentDto.getIban())
                .plan(investmentmentDto.getPlan())
                .intRateMonth(investmentPlan.getIntRateMonth())
                .lastName(investmentmentDto.getLastName())
                .firName(investmentmentDto.getFirName())
                .middleName(investmentmentDto.getMiddleName())
                .investmentRefNo(investmentmentDto.getTranxRef())
                .accruedBalance(investmentmentDto.getAmount())
                .startDate(startDateInv)
                .endDate(endDateInv)
                .build();

    }




}
