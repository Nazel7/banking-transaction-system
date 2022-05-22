package com.sankore.bank.crons;

import com.sankore.bank.Tables;
import com.sankore.bank.entities.builder.InvestmentMapper;
import com.sankore.bank.entities.builder.UserMapper;
import com.sankore.bank.entities.models.InvestmentModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.enums.TransType;
import com.sankore.bank.enums.TranxStatus;
import com.sankore.bank.event.notifcation.DataInfo;
import com.sankore.bank.event.notifcation.NotificationLog;
import com.sankore.bank.event.notifcation.NotificationLogEvent;
import com.sankore.bank.repositories.InvestmentRepo;
import com.sankore.bank.tables.records.CustomersRecord;
import com.sankore.bank.tables.records.InvestmentModelRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.DatagramSocket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentJOOQActivationScript implements SchdeduleJob {

    private final DSLContext dslContext;
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

            final List<InvestmentModelRecord> investmentModelRecords =
                    dslContext.selectFrom(Tables.INVESTMENT_MODEL)
                            .where(Tables.INVESTMENT_MODEL.STATUS.eq(TranxStatus.PENDING.name()))
                            .fetchInto(Tables.INVESTMENT_MODEL);

            if (investmentModelRecords.size() < 1) {
                return;
            }
            log.info("::: About to process Investment Activation.....");

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
                    log.info("::: About to ACTIVATE Investment Account");
                    if (investmentModel.getStartDate().getTime() <= System.currentTimeMillis()) {
                        int invProcessResponse = dslContext.update(Tables.INVESTMENT_MODEL)
                                .set(Tables.INVESTMENT_MODEL.STATUS, TranxStatus.OPEN.name())
                                .set(Tables.INVESTMENT_MODEL.UPDATED_AT, LocalDateTime.now())
                                .where(Tables.INVESTMENT_MODEL.ID.eq(investmentModel.getId()))
                                .execute();
                        log.info("::: Investment Actived successfully, response: [{}]", invProcessResponse);
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

            final List<InvestmentModelRecord> investmentModelRecords =
                    dslContext.selectFrom(Tables.INVESTMENT_MODEL)
                            .fetchInto(Tables.INVESTMENT_MODEL);

            if (investmentModelRecords.size() < 1) {
                return;
            }
            log.info("::: About to process Investment Activation.....");

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
                    log.info("::: About to DEACTIVATE Investment Account");
                    if (investmentModel.getEndDate().getTime() > System.currentTimeMillis()) {
                        int invProcessResponse = dslContext.update(Tables.INVESTMENT_MODEL)
                                .set(Tables.INVESTMENT_MODEL.STATUS, TranxStatus.CLOSE.name())
                                .set(Tables.INVESTMENT_MODEL.UPDATED_AT, LocalDateTime.now())
                                .where(Tables.INVESTMENT_MODEL.ID.eq(investmentModel.getId()))
                                .execute();
                        log.info("::: Investment DEATIVATION successful, response: [{}]", invProcessResponse);
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
