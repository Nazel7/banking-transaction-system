package com.sankore.bank.repositories;

import com.sankore.bank.entities.models.InvestmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentRepo extends JpaRepository<InvestmentModel, Long> {

    InvestmentModel getInvestmentModelByStatusAndPlan(String status, String plan);

    Page<InvestmentModel> findByStatusAndPlan(String status, String plan, Pageable pageable);

    Page<InvestmentModel> findByStatus(String status, Pageable pageable);

}
