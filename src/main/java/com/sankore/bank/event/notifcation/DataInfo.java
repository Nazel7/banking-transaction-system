package com.sankore.bank.event.notifcation;

import java.util.List;

import lombok.Data;

@Data
public class DataInfo {

    String message;
    List<Receipient> recipients;
}
