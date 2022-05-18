package com.sankore.bank.repositories;


import com.sankore.bank.entities.models.TransactionModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<TransactionModel, Long> {

}
