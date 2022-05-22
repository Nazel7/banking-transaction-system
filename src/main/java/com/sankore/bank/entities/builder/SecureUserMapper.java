package com.sankore.bank.entities.builder;

import com.sankore.bank.entities.models.SecureUserModel;
import com.sankore.bank.tables.records.CustomersRecord;
import com.sankore.bank.tables.records.SecureUserRecord;

public class SecureUserMapper {

    public static SecureUserRecord mapModelToRecord(SecureUserModel model, CustomersRecord userModel) {

        SecureUserRecord record = new SecureUserRecord();
        record.setUsermodelId(userModel.getId());
        record.setPassword(model.getPassword());
        record.setAuthority(model.getAuthority());
        record.setUsername(model.getUsername());

        return record;


    }
}
