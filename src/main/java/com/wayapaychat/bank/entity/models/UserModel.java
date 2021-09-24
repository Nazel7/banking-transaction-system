package com.wayapaychat.bank.entity.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Builder
@Data
@Entity
@Table(name = "customers", indexes = {
        @Index(name = "email_index", columnList = "email"),
        @Index(name = "bvn_index", columnList = "bvn"),
        @Index(name = "phone_index", columnList = "phone"),
        @Index(name = "tier_level_index", columnList = "tierLevel"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String pin;
    private String bvn;
    private String tierLevel;

    @OneToOne(mappedBy = "userModel")
    private AccountModel account;

    private Boolean verifiedEmail;
    private Boolean verifiedPhone;
    private Boolean verifiedBvn;

    @Column(name = ("verification_code"), length = 64)
    private String verificationCode;

    @OneToOne(mappedBy = "userModel")
    private SecureUserModel mSecureUserModel;

    @Tolerate
    public UserModel(){

    }
}
