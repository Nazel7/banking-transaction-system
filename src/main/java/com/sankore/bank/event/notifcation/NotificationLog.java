package com.sankore.bank.event.notifcation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sankore.bank.dtos.request.OriginatorKyc;
import com.sankore.bank.event.notifcation.DataInfo;

import lombok.Data;
import org.aspectj.weaver.ast.Or;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationLog {

    private String id;
    private DataInfo data;
    private String eventType;
    private String initiator;
    private OriginatorKyc originatorKyc;
    private String tranxRef;
    private String channelCode;
    private Date tranxDate;

}
