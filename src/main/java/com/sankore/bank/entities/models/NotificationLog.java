package com.sankore.bank.entities.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sankore.bank.event.notifcation.DataInfo;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationLog {
    private String id;
    private DataInfo data;
    private String eventType;
    private String initiator;

}
