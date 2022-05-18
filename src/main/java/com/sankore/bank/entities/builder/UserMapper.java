package com.sankore.bank.entities.builder;

import com.sankore.bank.enums.TierLevel;
import com.sankore.bank.entities.models.SecureUserModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.dtos.response.User;
import com.sankore.bank.dtos.response.LogginResponse;
import com.sankore.bank.dtos.request.SignUpDto;
import com.sankore.bank.dtos.request.UserInfoDto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

    public static UserModel mapToModel(SignUpDto signUpDto){

        return UserModel
                .builder()
                .address(signUpDto.getAddress())
                .email(signUpDto.getEmail())
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .tierLevel(TierLevel.LEVEL_ONE.name())
                .verifiedHomeAddress(signUpDto.getVerifiedHomeAddress())
                .verifiedBvn(signUpDto.getVerifiedBvn())
                .verifiedEmail(signUpDto.getVerifiedEmail())
                .verificationCode(signUpDto.getVerificationCode())
                .bvn(signUpDto.getBvn())
                .phone(signUpDto.getPhone())
                .build();
    }

    public static UserModel mapToModel(UserInfoDto userInfoDto, String tierLevel){

        return UserModel
                .builder()
                .bvn(userInfoDto.getBvn())
                .tierLevel(tierLevel)
                .verifiedBvn(userInfoDto.getVerifiedBvn())
                .verifiedEmail(userInfoDto.getVerifiedEmail())
                .verifiedPhone(userInfoDto.getVerifiedPhone())
                .verifiedBvn(userInfoDto.getVerifiedBvn())
                .verifiedHomeAddress(userInfoDto.getVerifiedHomeAddress())
                .build();
    }

    public static User mapToDomain(UserModel userModel){

        return User
                .builder()
                .id(userModel.getId())
                .address(userModel.getAddress())
                .account(userModel.getAccount())
                .email(userModel.getEmail())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .phone(userModel.getPhone())
                .homeAddress(userModel.getHomeAddress())
                .tierLevel(userModel.getTierLevel())
                .verificationCode(userModel.getVerificationCode())
                .verifiedBvn(userModel.getVerifiedBvn())
                .verifiedHomeAddress(userModel.getVerifiedHomeAddress())
                .verifiedEmail(userModel.getVerifiedEmail())
                .verifiedPhone(userModel.getVerifiedPhone())
                .build();
    }

    public static LogginResponse mapToDto(UserModel userModel, String token){

        return LogginResponse
                .builder()
                .id(userModel.getId())
                .address(userModel.getAddress())
                .email(userModel.getEmail())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .token(token)
                .phone(userModel.getPhone())
                .build();

    }

    public static SecureUserModel mapToAuth(UserModel userModel, SignUpDto signUpDto){
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
        String passcode= encoder.encode(signUpDto.getPassword());

        return SecureUserModel
                .builder()
                .authority("ROLE_CUSTOMER")
                .userModel(userModel)
                .username(userModel.getEmail())
                .password(passcode)
                .build();

    }
}
