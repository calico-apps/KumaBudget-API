package com.calicoapps.kumabudget.budget.repository;

import com.calicoapps.kumabudget.budget.entity.Income;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query("SELECT i FROM Income i WHERE i.person = :person")
    List<Income> findByPerson(@Param("person") Person person);

    @Query("SELECT DISTINCT i FROM Income i WHERE i.person IN :persons")
    List<Income> findByPersonsInList(@Param("persons") List<Person> persons);

}
