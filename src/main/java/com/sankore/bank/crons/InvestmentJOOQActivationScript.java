package com.sankore.bank.crons;

import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.enums.TransType;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.event.notifcation.DataInfo;
import com.sankore.bank.event.notifcation.NotificationLog;
import com.sankore.bank.event.notifcation.NotificationLogEvent;
import com.sankore.bank.repositories.InvestmentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentJOOQActivationScript implements SchdeduleJob {

    private final InvestmentRepo investmentRepo;
    private final ApplicationEventPublisher mEventPublisher;
    @Value("${page.custom-size}")
    private int customSize;

    @Scheduled(cron = "${spring.application.investment-activate-scheduler}")
    @Override
    public void run() {

        closeInvestment();
        openInvestment();
    }

    private void openInvestment() {
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
                    log.info("::: About to ACTIVATE Investment Account");
                    if (investmentModel.getStartDate().getTime() <= System.currentTimeMillis()) {
                        investmentModel.setStatus(TranxStatus.OPEN.name());
                        investmentModel = investmentRepo.save(investmentModel);
                        NotificationLog notificationLog = new NotificationLog();
                        DataInfo data = new DataInfo();
                        String notificationMessage =
                                String.format("Your Investment from your account: [%s] is successful with Amount: [%s%s] only ",
                                        investmentModel.getIban(),
                                        investmentModel.getCurrency(),
                                        investmentModel.getInvestedAmount());

                        data.setMessage(notificationMessage);
                        notificationLog.setData(data);
                        notificationLog.setInitiator(investmentModel.getFirName().concat(" ")
                                .concat(investmentModel.getLastName()));
                        notificationLog.setEventType(TransType.INVESTMENT.name());
                        notificationLog.setChannelCode(investmentModel.getBankCode());
                        notificationLog.setTranxDate(investmentModel.getCreatedAt());
                        notificationLog.setTranxRef(investmentModel.getInvestmentRefNo());

                        final NotificationLogEvent
                                notificationLogEvent = new NotificationLogEvent(this, notificationLog);
                        mEventPublisher.publishEvent(notificationLogEvent);
                        log.info("::: notification sent successfully for Open investment, data: [{}]",
                                notificationLogEvent.getNotificationLog());

                        log.info("::: Investment Status is Opened successfully with payload: [{}]", investmentModel);
                    }

                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            log.error("Activation not successful.");
        }
    }

    public void closeInvestment() {

        try {
            Pageable pageable = PageRequest.of(0, customSize);
            Page<InvestmentModel> investmentModels =
                    investmentRepo.findAll(pageable);

            if (investmentModels.getContent().isEmpty()) {
                return;
            }

            log.info("::: About to process open investment from DB...");
            for (int i = 0; i <= investmentModels.getTotalPages(); i++) {

                pageable = PageRequest.of(i, customSize);
                investmentModels =
                        investmentRepo.findAll(pageable);
                for (InvestmentModel investmentModel : investmentModels.getContent()) {
                    log.info("::: About to DEACTIVATE Investment Account");
                    if (investmentModel.getEndDate().getTime() > System.currentTimeMillis()) {
                        investmentModel.setStatus(TranxStatus.CLOSE.name());
                        investmentModel = investmentRepo.save(investmentModel);
                        NotificationLog notificationLog = new NotificationLog();
                        DataInfo data = new DataInfo();
                        String notificationMessage =
                                String.format("Your Investment from your account: [%s] is successful with Amount: [%s%s] only ",
                                        investmentModel.getIban(),
                                        investmentModel.getCurrency(),
                                        investmentModel.getInvestedAmount());

                        data.setMessage(notificationMessage);
                        notificationLog.setData(data);
                        notificationLog.setInitiator(investmentModel.getFirName().concat(" ")
                                .concat(investmentModel.getLastName()));
                        notificationLog.setEventType(TransType.INVESTMENT.name());
                        notificationLog.setChannelCode(investmentModel.getBankCode());
                        notificationLog.setTranxDate(investmentModel.getCreatedAt());
                        notificationLog.setTranxRef(investmentModel.getInvestmentRefNo());

                        final NotificationLogEvent
                                notificationLogEvent = new NotificationLogEvent(this, notificationLog);
                        mEventPublisher.publishEvent(notificationLogEvent);
                        log.info("::: notification sent successfully for Open investment, data: [{}]",
                                notificationLogEvent.getNotificationLog());

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
