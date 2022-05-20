package com.sankore.bank.crons;

import com.sankore.bank.entities.models.InvestmentModel;
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

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentActivationScript implements SchdeduleJob {

    private final InvestmentRepo investmentRepo;
    @Value("${page.custom-size}")
    private int customSize;

    @Scheduled(cron = "${spring.application.investment-activate-scheduler}")
    @Override
    public void run() {

        try {
            Pageable pageable = PageRequest.of(0, customSize);
            Page<InvestmentModel> investmentModels =
                    investmentRepo.findByStatus(TranxStatus.PENDING.name(), pageable);

            if (investmentModels.getContent().isEmpty()) {
                return;
            }

            log.info("::: About to process open investment from DB...");
            for (int i = 0; i <= investmentModels.getTotalPages(); i++) {

                pageable = PageRequest.of(i, customSize);
                investmentModels =
                        investmentRepo.findByStatus(TranxStatus.PENDING.name(), pageable);
                for (InvestmentModel investmentModel : investmentModels.getContent()) {
                    log.info("::: About to Activate Investment Account");
                    if (investmentModel.getStartDate().getTime() <= System.currentTimeMillis()) {
                        investmentModel.setStatus(TranxStatus.OPEN.name());
                        investmentModel = investmentRepo.save(investmentModel);

                        log.info("::: Investment Status is Opened successfully with payload: [{}]", investmentModel);
                    }

                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            log.error("Activation not successful.");
        }
    }
}
