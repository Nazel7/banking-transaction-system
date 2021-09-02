package com.wayapaychat.bank.event.notifcation;

import com.wayapaychat.bank.entity.models.NotificationLog;
import com.wayapaychat.bank.repository.DataBodyRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventHandler {

   private final DataBodyRepo message;

    @Async
    @EventListener
    public void publishMessage(NotificationLogEvent notificationLogEvent){

        final NotificationLog notificationLog = notificationLogEvent.getDataBody();
        NotificationLog savedNotificationLog = message.save(notificationLog);

        log.info("::: Message sent with payload: [{}] :::", savedNotificationLog);

    }
}
