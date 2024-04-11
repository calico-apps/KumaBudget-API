package com.calicoapps.kumabudget.budgetmodel.repository;

import com.calicoapps.kumabudget.budgetmodel.entity.ExpensePayerModel;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensePayerModelRepository extends JpaRepository<ExpensePayerModel, Long> {

    List<ExpensePayerModel> findByPerson(Person person);

}
