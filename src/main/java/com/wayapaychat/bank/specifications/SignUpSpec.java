package com.wayapaychat.bank.specifications;

import com.wayapaychat.bank.usecases.dtos.request.UserDto;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignUpSpec {

    public static boolean isSatisfied(UserDto userDto) {
        try{

            Objects.requireNonNull(userDto.getFirstName());
            Objects.requireNonNull(userDto.getLastName());
            Objects.requireNonNull(userDto.getAddress());
            Objects.requireNonNull(userDto.getEmail());
            Objects.requireNonNull(userDto.getPassword());

            log.info("::: Satisfied [{}] request body :::", userDto);
            return true;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            log.debug("::: Unsatisfied [{}] request body :::", userDto);
            return false;
        }

    }
}
