package com.sankore.bank.event.notifcation;

import org.springframework.context.ApplicationEvent;

import lombok.ToString;

@ToString
public class NotificationLogEvent extends ApplicationEvent {

    private NotificationLog mNotificationLog;


    public NotificationLogEvent(Object source, NotificationLog notificationLog) {
        super(source);
        this.mNotificationLog = notificationLog;
    }

    public NotificationLog getNotificationLog() {
        return mNotificationLog;
    }
}
