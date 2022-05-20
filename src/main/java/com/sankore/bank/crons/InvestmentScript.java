package com.sankore.bank.crons;

import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.enums.InvestmentPlan;
import com.sankore.bank.enums.TransType;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.repositories.InvestmentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentScript implements SchdeduleJob {

    private final InvestmentRepo investmentRepo;

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

            log.info("::: About to process open investment from DB...");
            for (int i = 0; i <= investmentModels.getTotalPages(); i++) {

                pageable = PageRequest.of(i, customSize);
                investmentModels =
                        investmentRepo.findByStatusAndPlan(status, invPlan, pageable);

                for (InvestmentModel investmentModel : investmentModels.getContent()) {

                    // Calculate Daily Interest
                    final LocalDate currentDate = LocalDate.now();
                    long daysInMonth = currentDate.lengthOfMonth();
                    MathContext context = new MathContext(4);
                    final BigDecimal intrAmountForPlanRatio = new BigDecimal("0.01", context);
                    final BigDecimal montlyIntrForPlan =
                            intrAmountForPlanRatio.multiply(investmentModel.getInvestedAmount());

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

                }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
