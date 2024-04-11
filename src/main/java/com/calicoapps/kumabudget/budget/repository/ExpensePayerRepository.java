package com.calicoapps.kumabudget.budget.repository;

import com.calicoapps.kumabudget.budget.entity.ExpensePayer;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpensePayerRepository extends JpaRepository<ExpensePayer, Long> {

    List<ExpensePayer> findByPerson(Person person);

    @Query("SELECT DISTINCT ep FROM ExpensePayer ep WHERE ep.person IN :persons")
    List<ExpensePayer> findByPersonsInList(@Param("persons") List<Person> persons);

}
