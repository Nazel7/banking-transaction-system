package com.sankore.bank.crons;

import org.springframework.scheduling.annotation.Scheduled;

public class InvestmentActivationScript implements SchdeduleJob {

    @Scheduled(cron = "${spring.application.investment-activate-scheduler}")
    @Override
    public void run() {


    }
}
