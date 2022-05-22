package com.sankore.bank.services;

import com.sankore.bank.Tables;
import com.sankore.bank.dtos.request.SignUpDto;
import com.sankore.bank.dtos.request.UserInfoDto;
import com.sankore.bank.dtos.response.User;
import com.sankore.bank.dtos.response.UserNotFoundException;
import com.sankore.bank.entities.builder.AccountMapper;
import com.sankore.bank.entities.builder.SecureUserMapper;
import com.sankore.bank.entities.builder.UserMapper;
import com.sankore.bank.entities.models.AccountModel;
import com.sankore.bank.entities.models.SecureUserModel;
import com.sankore.bank.entities.models.UserModel;
import com.sankore.bank.repositories.UserRepo;
import com.sankore.bank.tables.records.BankAccountRecord;
import com.sankore.bank.tables.records.CustomersRecord;
import com.sankore.bank.tables.records.SecureUserRecord;
import com.sankore.bank.utils.BaseUtil;
import com.sankore.bank.utils.TierLevelSpecUtil;
import com.sankore.bank.utils.TransactionObjFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserJOOQService {

    private final UserRepo mUserRepo;
    private final DSLContext dslContext;

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
        CustomersRecord customersRecord = UserMapper.mapModelToCustRecord(userModel);

        customersRecord.setId(TransactionObjFormatter.generateRandomNumVal(15));
        CustomersRecord savedCustomerRecord = dslContext.insertInto(Tables.CUSTOMERS)
                .set(customersRecord)
                .returning()
                .fetchOne();
        if (savedCustomerRecord == null) {
            log.error("Signup error.");
            throw new IllegalArgumentException("Signup error");
        }
        log.info("::: New user with id: [{}] saved to DB :::", savedCustomerRecord.getId());
        AccountModel accountModel = BaseUtil.generateAccountNumber(userModel, signUpDto.getAccountType());
        accountModel.setUserModel(userModel);
        accountModel.setBankCode(bankCode);

        BankAccountRecord accountRecord = AccountMapper.mapModelToAcctRecord(accountModel, customersRecord);
        accountRecord.setId(UUID.randomUUID());
        BankAccountRecord savedBankAccountRecord = dslContext.insertInto(Tables.BANK_ACCOUNT)
                .set(accountRecord)
                .returning()
                .fetchOne();
        if (savedBankAccountRecord == null) {
            log.error("Signup error.");
            throw new IllegalArgumentException("Signup error");
        }
        log.info("::: New account creation for user with id: [{}] saved to DB :::",
                savedBankAccountRecord.getId());

        final SecureUserModel secureUserModel = UserMapper.mapToAuth(userModel, signUpDto);
        SecureUserRecord secureUserRecord = SecureUserMapper.mapModelToRecord(secureUserModel, customersRecord);
        secureUserRecord.setId(TransactionObjFormatter.generateRandomNumVal(15));
        SecureUserRecord savedSecureRecord = dslContext.insertInto(Tables.SECURE_USER)
                .set(secureUserRecord)
                .returning()
                .fetchOne();
        if (savedSecureRecord == null) {
            log.error("Signup error.");
            throw new IllegalArgumentException("Signup error");
        }
        log.info("::: New account creation for user with id: [{}] saved to DB :::",
                savedBankAccountRecord.getId());

        return UserMapper.mapToDomain(userModel);

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

        int record = dslContext.update(Tables.CUSTOMERS)
                .set(Tables.CUSTOMERS.TIER_LEVEL, model.getTierLevel())
                .where(Tables.CUSTOMERS.ID.eq(model.getId()))
                .execute();
        log.info("::: User with id: [{}} upgradedResponse :::", record);
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

        CustomersRecord record = dslContext.fetchOne(Tables.CUSTOMERS, Tables.CUSTOMERS.ID.eq(userId));

        if (record == null) {

            throw new UserNotFoundException("User with Id: " + userId + " not found");
        }
        log.info("::: User with id: [{}] found", userId);

        return UserMapper.mapRecordToModel(record);

    }

}
