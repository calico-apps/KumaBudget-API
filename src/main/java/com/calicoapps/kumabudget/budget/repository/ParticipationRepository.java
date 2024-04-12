package com.calicoapps.kumabudget.budget.repository;

import com.calicoapps.kumabudget.budget.entity.Participation;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findByPerson(Person person);

    @Query("SELECT DISTINCT p FROM Participation p WHERE p.person IN :persons")
    List<Participation> findByPersonsInList(@Param("persons") List<Person> persons);

}
