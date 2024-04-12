package com.calicoapps.kumabudget.budget.repository;

import com.calicoapps.kumabudget.budget.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
