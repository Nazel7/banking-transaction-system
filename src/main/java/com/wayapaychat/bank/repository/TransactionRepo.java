package com.wayapaychat.bank.repository;


import com.wayapaychat.bank.entity.TransactionModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<TransactionModel, Long> {

}
