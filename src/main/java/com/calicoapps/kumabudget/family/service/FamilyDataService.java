package com.calicoapps.kumabudget.family.service;

import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.family.entity.Family;
import com.calicoapps.kumabudget.family.repository.FamilyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public class FamilyDataService extends DataService<Family, Long> {

    public static final String ENTITY_NAME = "Family";

    private final FamilyRepository repository;

    public FamilyDataService(FamilyRepository repository) {
        super(ENTITY_NAME);
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Family, Long> getRepository() {
        return repository;
    }
}