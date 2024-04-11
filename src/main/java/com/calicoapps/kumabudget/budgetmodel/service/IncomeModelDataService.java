package com.calicoapps.kumabudget.budgetmodel.service;

import com.calicoapps.kumabudget.budgetmodel.entity.IncomeModel;
import com.calicoapps.kumabudget.budgetmodel.repository.IncomeModelRepository;
import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeModelDataService extends DataService<IncomeModel, Long> {

    public static final String ENTITY_NAME = "IncomeModel";

    private final IncomeModelRepository repository;

    public IncomeModelDataService(IncomeModelRepository repository) {
        super(ENTITY_NAME);
        this.repository = repository;
    }

    @Override
    protected JpaRepository<IncomeModel, Long> getRepository() {
        return repository;
    }

    public List<IncomeModel> getAllOfOnePerson(Person person) {
        return repository.findByPerson(person);
    }

}
