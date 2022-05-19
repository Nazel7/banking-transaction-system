package com.sankore.bank.repositories;

import com.sankore.bank.entities.models.InvestmentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentRepo extends JpaRepository<InvestmentModel, Long> {
}
