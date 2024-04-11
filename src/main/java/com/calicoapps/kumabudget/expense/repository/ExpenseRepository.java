package com.calicoapps.kumabudget.expense.repository;

import com.calicoapps.kumabudget.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
