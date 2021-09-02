package com.wayapaychat.bank.event.notifcation;

import com.wayapaychat.bank.entity.models.NotificationLog;

import org.springframework.context.ApplicationEvent;

import lombok.ToString;

@ToString
public class NotificationLogEvent extends ApplicationEvent {

    private NotificationLog mNotificationLog;


    public NotificationLogEvent(Object source, NotificationLog notificationLog) {
        super(source);
        this.mNotificationLog = notificationLog;
    }

    public NotificationLog getDataBody() {
        return mNotificationLog;
    }
}
