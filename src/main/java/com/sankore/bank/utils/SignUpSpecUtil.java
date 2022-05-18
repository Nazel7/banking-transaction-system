package com.sankore.bank.utils;

import com.sankore.bank.dtos.request.UserDto;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignUpSpecUtil {

    public static boolean isSatisfied(UserDto userDto) {
        try{

            Objects.requireNonNull(userDto.getFirstName());
            Objects.requireNonNull(userDto.getLastName());
            Objects.requireNonNull(userDto.getAddress());
            Objects.requireNonNull(userDto.getPassword());
            Objects.requireNonNull(userDto.getVerificationCode());
            Objects.requireNonNull(userDto.getPhone());
            if (!userDto.getVerifiedPhone() || userDto.getPhone() == null) {
                return false;
            }

            log.info("::: Satisfied requestBody: [{}] :::", userDto);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Unsatisfied requestBody [{}]:::", userDto);
            return false;
        }

    }
}
