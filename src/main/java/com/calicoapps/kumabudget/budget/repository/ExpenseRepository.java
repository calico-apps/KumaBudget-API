package com.calicoapps.kumabudget.budget.repository;

import com.calicoapps.kumabudget.budget.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
