package com.calicoapps.kumabudget.budgetmodel.repository;

import com.calicoapps.kumabudget.budgetmodel.entity.ParticipationModel;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationsModelRepository extends JpaRepository<ParticipationModel, Long> {

    List<ParticipationModel> findByPerson(Person person);

}
