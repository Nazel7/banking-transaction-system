package com.wayapaychat.bank.repository;

import com.wayapaychat.bank.entity.models.SecureUserModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecureUserRepo extends JpaRepository<SecureUserModel, Long> {

    SecureUserModel getSecureUserByUsername(String name);


}

