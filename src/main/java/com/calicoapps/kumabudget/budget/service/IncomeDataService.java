package com.calicoapps.kumabudget.budget.service;

import com.calicoapps.kumabudget.budget.entity.Income;
import com.calicoapps.kumabudget.budget.repository.IncomeRepository;
import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.family.entity.Family;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeDataService extends DataService<Income, Long> {

    public static final String ENTITY_NAME = "Income";

    private final IncomeRepository repository;

    public IncomeDataService(IncomeRepository repository) {
        super(ENTITY_NAME);
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Income, Long> getRepository() {
        return repository;
    }

    public List<Income> getAllOfOnePerson(Person person) {
        return repository.findByPerson(person);
    }

    public List<Income> getAllOfAFamily(Family family) {
        return repository.findByPersonsInList(family.getMembers());
    }

}
