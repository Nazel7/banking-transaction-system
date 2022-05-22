package com.sankore.bank.crons;

import com.sankore.bank.Tables;
import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.enums.InvestmentPlan;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.repositories.InvestmentRepo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentJOOQScript implements SchdeduleJob {

    private final InvestmentRepo investmentRepo;
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
            Pageable pageable = PageRequest.of(0, customSize);
            Page<InvestmentModel> investmentModels =
                    investmentRepo.findByStatusAndPlan(status,
                            invPlan, pageable);
            List<InvestmentModelRecord> investmentModelRecords =
                    dslContext.selectFrom(Tables.INVESTMENT_MODEL)
                            .where(Tables.INVESTMENT_MODEL.STATUS.eq(status)
                                    .and(Tables.INVESTMENT_MODEL.PLAN.eq(invPlan)))
                            .fetchInto(Tables.INVESTMENT_MODEL);

            if (investmentModelRecords.size() < 1) {
                return;
            }
            List<InvestmentModel> investmentModelList = new ArrayList<>();
            for (InvestmentModelRecord record: investmentModelRecords) {

            }



            PagedListHolder<InvestmentModel> pagedListHolder =
                    new PagedListHolder<>(investmentModels.getContent());
            pagedListHolder.setPageSize(customSize);

            log.info("::: About to process open investment from DB...");
            for (int i = 0; i <= pagedListHolder.getPageCount(); i++) {
                pagedListHolder.setPage(i);

                for (InvestmentModel investmentModel : pagedListHolder.getPageList()) {

                    if (investmentModel.getEndDate().getTime() > (System.currentTimeMillis())) {
                        // Calculate Daily Interest
                        final LocalDate currentDate = LocalDate.now();
                        long daysInMonth = currentDate.lengthOfMonth();
                        MathContext context = new MathContext(4);
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
                        accruedIntModel = investmentRepo.save(accruedIntModel);

                        log.info("::: Daily accrued interest is successful with payload: [{}]", accruedIntModel);
                    }

                }
            }

                }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
