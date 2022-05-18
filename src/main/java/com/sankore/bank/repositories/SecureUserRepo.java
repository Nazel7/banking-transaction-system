package com.sankore.bank.repositories;

import com.sankore.bank.entities.models.SecureUserModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecureUserRepo extends JpaRepository<SecureUserModel, Long> {

    SecureUserModel getSecureUserByUsername(String name);


}

