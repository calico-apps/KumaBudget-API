package com.calicoapps.kumabudget.family.service;

import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.family.entity.Person;
import com.calicoapps.kumabudget.family.repository.PersonRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public class PersonDataService extends DataService<Person, Long> {

    public static final String ENTITY_NAME = "Person";

    private final PersonRepository repository;

    public PersonDataService(PersonRepository repository) {
        super(ENTITY_NAME);
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Person, Long> getRepository() {
        return repository;
    }
}