package com.calicoapps.kumabudget.budgetmodel.repository;

import com.calicoapps.kumabudget.budgetmodel.entity.IncomeModel;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeModelRepository extends JpaRepository<IncomeModel, Long> {

    List<IncomeModel> findByPerson(Person person);

}
