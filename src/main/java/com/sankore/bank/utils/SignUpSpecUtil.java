package com.sankore.bank.utils;

import com.sankore.bank.dtos.request.SignUpDto;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignUpSpecUtil {

    public static boolean isSatisfied(SignUpDto signUpDto) {
        try{

            Objects.requireNonNull(signUpDto.getFirstName());
            Objects.requireNonNull(signUpDto.getLastName());
            Objects.requireNonNull(signUpDto.getAddress());
            Objects.requireNonNull(signUpDto.getPassword());
            Objects.requireNonNull(signUpDto.getVerificationCode());
            Objects.requireNonNull(signUpDto.getPhone());
            Objects.requireNonNull(signUpDto.getEmail());
            if (!signUpDto.getVerifiedPhone()) {
                return false;
            }

            log.info("::: Satisfied requestBody: [{}] :::", signUpDto);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Unsatisfied requestBody [{}]:::", signUpDto);
            return false;
        }

    }
}
