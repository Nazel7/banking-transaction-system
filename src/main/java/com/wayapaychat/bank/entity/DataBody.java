package com.wayapaychat.bank.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.wayapaychat.bank.event.notifcation.DataInfo;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Entity
@Table
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class DataBody {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "datainfo")
    private DataInfo data;
    private String eventType;
    private String initiator;

}
