package com.sankore.bank.crons;

import org.springframework.scheduling.annotation.Scheduled;

public class InvestmentScript implements SchdeduleJob{

    @Scheduled(cron = "* * * * * *")
    @Override
    public void run() {

    }
}
