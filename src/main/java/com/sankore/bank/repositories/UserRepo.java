package com.sankore.bank.repositories;


import com.sankore.bank.entities.models.UserModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserModel, Long> {

    UserModel findUserModelByEmail(String email);

    UserModel findUserModelById(Long id);
}
