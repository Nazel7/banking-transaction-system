package com.sankore.bank.event.notifcation;

import java.util.List;

import com.sankore.bank.dtos.request.OriginatorKyc;
import lombok.Data;

@Data
public class DataInfo {

    private String message;
    private List<Receipient> recipients;
    private OriginatorKyc originatorKyc;

}
