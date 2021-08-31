package com.wayapaychat.bank.mappers;

import com.wayapaychat.bank.entity.SecureUser;
import com.wayapaychat.bank.entity.UserModel;
import com.wayapaychat.bank.enums.TierLevel;
import com.wayapaychat.bank.usecases.domain.User;
import com.wayapaychat.bank.usecases.dtos.request.LoginDto;
import com.wayapaychat.bank.usecases.dtos.request.UserDto;
import com.wayapaychat.bank.usecases.dtos.request.UserInfoDto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

    public static UserModel mapToModel(UserDto userDto){

        return UserModel
                .builder()
                .address(userDto.getAddress())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .tierLevel(TierLevel.LEVEL_ONE.name())
                .password(userDto.getPassword())
                .bvn(userDto.getBvn())
                .phone(userDto.getPhone())
                .build();
    }

    public static UserModel mapToModel(UserInfoDto userInfoDto){

        return UserModel
                .builder()
                .bvn(userInfoDto.getBvn())
                .phone(userInfoDto.getPhone())
                .tierLevel(TierLevel.LEVEL_ONE.name())
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
                .tierLevel(userModel.getTierLevel())
                .verificationCode(userModel.getVerificationCode())
                .verifiedBvn(userModel.getVerifiedBvn())
                .verifiedEmail(userModel.getVerifiedEmail())
                .verifiedPhone(userModel.getVerifiedPhone())
                .build();
    }

    public static LoginDto mapToDto(UserModel userModel, String token){

        return LoginDto
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

    public static SecureUser mapToAuth(UserModel userModel){
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
        String passcode= encoder.encode(userModel.getPassword());

        return SecureUser
                .builder()
                .authority("ROLE_CUSTOMER")
                .userModel(userModel)
                .username(userModel.getEmail())
                .password(passcode)
                .build();

    }

}
