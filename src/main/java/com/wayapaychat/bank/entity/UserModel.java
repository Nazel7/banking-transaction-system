package com.wayapaychat.bank.entity;

import com.wayapaychat.bank.usecases.dtos.response.TransferNotValidException;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Builder
@Data
@Entity
@Table(name = "customers")
public class UserModel {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
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
    private SecureUser secureUser;

    @Tolerate
    public UserModel(){

    }
}
