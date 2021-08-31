package com.wayapaychat.bank.services;

import com.wayapaychat.bank.entity.AccountModel;
import com.wayapaychat.bank.entity.SecureUser;
import com.wayapaychat.bank.entity.UserModel;
import com.wayapaychat.bank.enums.AccountStatus;
import com.wayapaychat.bank.enums.Currency;
import com.wayapaychat.bank.enums.TierLevel;
import com.wayapaychat.bank.mappers.UserMapper;
import com.wayapaychat.bank.repository.AccountRepo;
import com.wayapaychat.bank.repository.SecureUserRepo;
import com.wayapaychat.bank.repository.UserRepo;
import com.wayapaychat.bank.specifications.SignUpSpec;
import com.wayapaychat.bank.specifications.TierLevelSpec;
import com.wayapaychat.bank.usecases.domain.User;
import com.wayapaychat.bank.usecases.dtos.request.UserDto;
import com.wayapaychat.bank.usecases.dtos.request.UserInfoDto;
import com.wayapaychat.bank.usecases.dtos.response.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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

    public User registerUser(final UserDto userDto) throws UserNotFoundException {

        if (!SignUpSpec.isSatisfied(userDto)) {

            throw new UserNotFoundException("Unsatisfied request body");

        }

        UserModel userMap = UserMapper.mapToModel(userDto);
        UserModel userModel = upgradeSignUpUser(userMap);

        AccountModel accountModel = createAccount(userModel);
        userModel.setVerifiedEmail(true);
        accountModel.setUserModel(userModel);
        UserModel userModelSaved = mUserRepo.save(userModel);
        log.info("::: New user with id: [{}] saved to DB :::", userModelSaved.getId());

        AccountModel accountModelSaved = mAccountRepo.save(accountModel);
        log.info("::: New account creation for user with id: [{}] saved to DB :::",
                 accountModelSaved.getId());

        SecureUser secureUser = UserMapper.mapToAuth(userModel);
        mSecureUserRepo.save(secureUser);

        log.info("::: Login details created successfull :::");

        return UserMapper.mapToDomain(userModelSaved);

    }

    public User upgradeUser(final Long useId, final UserInfoDto userInfoDto)
            throws UserNotFoundException {

        final UserModel model = findByUserId(useId);

        if (model == null) {

            throw new UserNotFoundException("::: User not found :::");
        }

        if (model.getPhone() == null) {
            model.setPhone(userInfoDto.getPhone());
        }
        if (model.getBvn() == null) {
            model.setBvn(userInfoDto.getBvn());
        }

        String level = TierLevelSpec.getLevel(model);

        if (level.equalsIgnoreCase(TierLevel.LEVEL_TWO.name())) {
            model.setTierLevel(level);
            model.setVerifiedPhone(true);

        } else if (level.equalsIgnoreCase(TierLevel.LEVEL_THREE.name())) {
            model.setTierLevel(level);
            model.setVerifiedBvn(true);

        } else {
            model.setTierLevel(level);
        }

        log.info("::: User with id: [{}} upgraded :::", model.getId());
        return UserMapper.mapToDomain(model);

    }

    private UserModel upgradeSignUpUser(UserModel model)
            throws UserNotFoundException {

        if (model == null) {

            throw new UserNotFoundException("::: User not found :::");
        }

        String level = TierLevelSpec.getLevel(model);
        if (level.equalsIgnoreCase(TierLevel.LEVEL_TWO.name())) {
            model.setTierLevel(level);
            model.setVerifiedPhone(true);

        } else if (level.equalsIgnoreCase(TierLevel.LEVEL_THREE.name())) {
            model.setTierLevel(level);
            model.setVerifiedBvn(true);
            model.setVerifiedPhone(true);

        } else {
            model.setTierLevel(level);
        }

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

    private AccountModel createAccount(UserModel userModel) {

        String[] num = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        SecureRandom random = new SecureRandom();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(num.length);
            sb.append(num[n]);
        }

        String iban = sb.toString();

        AccountModel accountModel = new AccountModel(iban,
                                                     Currency.NGN.name(),
                                                     AccountStatus.ACTIVE.name());

        log.info("::: Account with Iban: [{}] created for user with email: [{}]",
                 accountModel.getIban(),
                 userModel.getEmail());

        return accountModel;

    }

}
