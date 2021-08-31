package com.wayapaychat.bank.repository;


import com.wayapaychat.bank.entity.UserModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserModel, Long> {

    UserModel findUserModelByEmail(String email);

    UserModel findUserModelById(Long id);
}
