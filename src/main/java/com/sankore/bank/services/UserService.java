package com.sankore.bank.services;

import com.sankore.bank.entities.builder.UserMapper;
import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.entities.models.SecureUserModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.repositories.AccountRepo;
import com.sankore.bank.repositories.SecureUserRepo;
import com.sankore.bank.repositories.UserRepo;
import com.sankore.bank.utils.BaseUtil;
import com.sankore.bank.utils.TierLevelSpecUtil;
import com.sankore.bank.dtos.response.User;
import com.sankore.bank.dtos.request.SignUpDto;
import com.sankore.bank.dtos.request.UserInfoDto;
import com.sankore.bank.dtos.response.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepo mUserRepo;
    private final AccountRepo mAccountRepo;
    private final SecureUserRepo mSecureUserRepo;

    @Value("${spring.application.bank-code}")
    private String bankCode;

    public User registerUser(final SignUpDto signUpDto) throws UserNotFoundException {
        log.info("::: In registerUser.....");

        if (!BaseUtil.isRequestSatisfied(signUpDto)) {

            throw new UserNotFoundException("Unsatisfied request body");
        }
        log.info("::: About to map to model......");
        UserModel userMapped = UserMapper.mapToModel(signUpDto);
        UserModel userModel = upgradeSigningUpUser(userMapped);

        AccountModel accountModel = BaseUtil.generateAccountNumber(userModel, signUpDto.getAccountType());
        accountModel.setUserModel(userModel);
        accountModel.setBankCode(bankCode);
        UserModel userModelSaved = mUserRepo.save(userModel);
        log.info("::: New user with id: [{}] saved to DB :::", userModelSaved.getId());

        AccountModel accountModelSaved = mAccountRepo.save(accountModel);
        log.info("::: New account creation for user with id: [{}] saved to DB :::",
                 accountModelSaved.getId());

        final SecureUserModel secureUserModel = UserMapper.mapToAuth(userModel, signUpDto);
        mSecureUserRepo.save(secureUserModel);

        log.info("::: Login details created successful :::");

        return UserMapper.mapToDomain(userModelSaved);

    }

    public User upgradeUser(final Long useId, final UserInfoDto userInfoDto)
            throws UserNotFoundException {
        log.info("::: In upgradeUser.....");

       UserModel model = findByUserId(useId);

        if (model == null) {

            throw new UserNotFoundException("::: User not found :::");
        }

        if (model.getBvn() == null) {
            model.setBvn(userInfoDto.getBvn());
        }
        if (model.getEmail() == null) {
            model.setEmail(userInfoDto.getEmail());
        }
        if (model.getHomeAddress() == null) {
            model.setHomeAddress(userInfoDto.getHomeAddress());
        }
        if (!model.getVerifiedHomeAddress()) {
            model.setVerifiedHomeAddress(userInfoDto.getVerifiedHomeAddress());
        }
        if (!model.getVerifiedBvn()) {
            model.setVerifiedBvn(userInfoDto.getVerifiedBvn());
        }
        if (!model.getVerifiedEmail()) {
            model.setVerifiedEmail(userInfoDto.getVerifiedEmail());
        }

        final String level = TierLevelSpecUtil.getLevel(model);

        model.setTierLevel(level);

        model = mUserRepo.save(model);
        log.info("::: User with id: [{}} upgraded :::", model.getId());
        return UserMapper.mapToDomain(model);

    }

    private UserModel upgradeSigningUpUser(UserModel model)
            throws UserNotFoundException {

        if (model == null) {

            throw new UserNotFoundException("::: User not found :::");
        }

        String level = TierLevelSpecUtil.getLevel(model);
       model.setTierLevel(level);

        log.info("::: User with id: [{}} upgraded :::", model.getId());
        return model;

    }


    private UserModel findByUserId(Long userId) throws UserNotFoundException {

        Optional<UserModel> modelOptional = mUserRepo.findById(userId);

        if (!modelOptional.isPresent()) {

            throw new UserNotFoundException("User with Id: " + userId + " not found");
        }
        log.info("::: User with id: [{}] found", userId);
        return modelOptional.get();

    }

}
