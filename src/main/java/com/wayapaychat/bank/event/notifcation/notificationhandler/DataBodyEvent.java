package com.wayapaychat.bank.event.notifcation.notificationhandler;

import com.wayapaychat.bank.entity.DataBody;

import org.springframework.context.ApplicationEvent;

import lombok.ToString;

@ToString
public class DataBodyEvent extends ApplicationEvent {

    private DataBody mDataBody;


    public DataBodyEvent(Object source, DataBody dataBody) {
        super(source);
        this.mDataBody= dataBody;
    }

    public DataBody getDataBody() {
        return mDataBody;
    }
}
