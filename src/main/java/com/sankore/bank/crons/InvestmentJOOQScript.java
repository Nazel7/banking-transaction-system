package com.sankore.bank.crons;

import com.sankore.bank.Tables;
import com.sankore.bank.entities.builder.InvestmentMapper;
import com.sankore.bank.entities.builder.UserMapper;
import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.InvestmentPlan;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.repositories.InvestmentRepo;
import com.sankore.bank.tables.records.CustomersRecord;
import com.sankore.bank.tables.records.InvestmentModelRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentJOOQScript implements SchdeduleJob {

    private final DSLContext dslContext;

    @Value("${page.custom-size}")
    private int customSize;

    @Scheduled(cron = "${spring.application.investment-scheduler}")
    @Override
    public void run() {

        this.runInvestmentPlanScript(TranxStatus.OPEN.name(), InvestmentPlan.BRONZE.name());
        this.runInvestmentPlanScript(TranxStatus.OPEN.name(), InvestmentPlan.SILVER.name());
        this.runInvestmentPlanScript(TranxStatus.OPEN.name(), InvestmentPlan.GOLD.name());

    }

    private void runInvestmentPlanScript(String status, String invPlan) {

        try {

            final List<InvestmentModelRecord> investmentModelRecords =
                    dslContext.selectFrom(Tables.INVESTMENT_MODEL)
                            .where(Tables.INVESTMENT_MODEL.STATUS.eq(status)
                                    .and(Tables.INVESTMENT_MODEL.PLAN.eq(invPlan)))
                            .fetchInto(Tables.INVESTMENT_MODEL);

            if (investmentModelRecords.size() < 1) {
                return;
            }
            log.info("::: About to process Investment Daily profit.....");

            // Map list of InvestmentRecord into Model
            List<InvestmentModel> investmentModelList = new ArrayList<>();
            for (InvestmentModelRecord record: investmentModelRecords) {
                CustomersRecord customersRecord = dslContext.fetchOne(Tables.CUSTOMERS,
                        Tables.CUSTOMERS.ID.eq(record.getId()));
                assert customersRecord != null;
                UserModel userModel = UserMapper.mapRecordToModel(customersRecord);
                final InvestmentModel investmentModel =
                        InvestmentMapper.mapRecordToModel(record, userModel);
                investmentModelList.add(investmentModel);
            }

            PagedListHolder<InvestmentModel> pagedListHolder =
                    new PagedListHolder<>(investmentModelList);
            pagedListHolder.setPageSize(customSize);

            log.info("::: About to process open investment from DB...");
            for (int i = 0; i <= pagedListHolder.getPageCount(); i++) {
                pagedListHolder.setPage(i);
                System.out.println("::: PageList: " + pagedListHolder.getPageList());

                for (InvestmentModel investmentModel : pagedListHolder.getPageList()) {

                    if (investmentModel.getEndDate().getTime() > (System.currentTimeMillis())) {
                        // Calculate Daily Interest
                        final LocalDate currentDate = LocalDate.now();
                        long daysInMonth = currentDate.lengthOfMonth();
                        MathContext context = new MathContext(6);
                        InvestmentPlan plan = InvestmentPlan.getInvestmentPlan(investmentModel.getPlan());
                        double intrRatio = plan.getIntRateMonth() / 100;
                        final BigDecimal intrAmountPlanRatio = new BigDecimal(intrRatio, context);
                        final BigDecimal montlyIntrForPlan =
                                intrAmountPlanRatio.multiply(investmentModel.getInvestedAmount());

                        // Get DailyInterest Amount
                        final BigDecimal dailyInterest =
                                montlyIntrForPlan.divide(new BigDecimal(daysInMonth), context);
                        InvestmentModel accruedIntModel = investmentModel.doAccruedInterest(dailyInterest);

                        if (accruedIntModel == null) {
                            log.info("Daily Profit Accrued error...");
                            throw new RuntimeException("Daily Profit Accrued error. Date: " + new Date());
                        }
                       log.info("::: Daily Profit: [{}]", dailyInterest);
                        log.info("::: New AccruedBalance: [{}]", accruedIntModel.getAccruedBalance());
                        int investmentProcessResponse =
                                dslContext.update(Tables.INVESTMENT_MODEL)
                                        .set(Tables.INVESTMENT_MODEL.ACCRUED_BALANCE, accruedIntModel.getAccruedBalance())
                                        .set(Tables.INVESTMENT_MODEL.UPDATED_AT, LocalDateTime.now())
                                        .where(Tables.INVESTMENT_MODEL.ID.eq(accruedIntModel.getId()))
                                        .execute();

                        log.info("::: Daily investment successful with response: [{}]", investmentProcessResponse);

                    }

                }
            }

                }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
