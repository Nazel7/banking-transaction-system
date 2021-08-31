package com.wayapaychat.bank.repository;

import com.wayapaychat.bank.entity.SecureUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecureUserRepo extends JpaRepository<SecureUser, Long> {

    SecureUser getSecureUserByUsername(String name);


}

