package com.sankore.bank.event.notifcation;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventHandler {

    private final RabbitTemplate mRabbitTemplate;

    @Value("${spring.rabbitmq.topic}")
    private String EXCHANGE;

    @Value("${spring.rabbitmq.routekey}")
    private String ROUTE_KEY;

    @Async
    @EventListener
    public void publishMessage(NotificationLogEvent notificationLogEvent) {

        final NotificationLog notificationLog = notificationLogEvent.getNotificationLog();
        mRabbitTemplate.convertAndSend(EXCHANGE, ROUTE_KEY, notificationLog);

        log.info("::: Message sent with payload: [{}] :::", notificationLog);

    }
}
