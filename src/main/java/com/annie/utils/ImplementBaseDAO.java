package com.annie.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class ImplementBaseDAO<T, ID> implements BaseDAO<T, ID> {

    private final Class<T> classType;

    @PersistenceContext
    private EntityManager entityManager;

    public ImplementBaseDAO(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public Optional<T> findById(ID id) {
        try {
            T findingObject = entityManager.find(classType, id);
            return Optional.ofNullable(findingObject);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll() {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(classType);
            Root<T> root = cq.from(classType);
            cq.select(root);
            return entityManager.createQuery(cq).getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Transactional
    @Override
    public T save(T entity) {
        try {
            entityManager.persist(entity);
            return entity;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Override
    public T update(T entity) {
        try {
            return entityManager.merge(entity);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Override
    public void delete(ID id) {
        Optional<T> deleteEntity = findById(id);
        deleteEntity.ifPresent(entityManager::remove);
    }
}
