package com.calicoapps.kumabudget.common;

import com.calicoapps.kumabudget.exception.KumaException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class DataService<T, ID> {

    private final String entityName;

    public DataService(String entityName) {
        this.entityName = entityName;
    }

    protected abstract JpaRepository<T, ID> getRepository();

    public T findById(ID id) {
        Optional<T> optional = getRepository().findById(id);
        return KumaException.orElseThrow(optional, entityName, optional.toString());
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }

}
