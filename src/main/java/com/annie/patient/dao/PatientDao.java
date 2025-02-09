package com.annie.patient.dao;

import com.annie.patient.entity.Patient;
import com.annie.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Optional;

@Stateless
public class PatientDao extends ImplementBaseDAO<Patient, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    public PatientDao() {
        super(Patient.class);
    }

    public Optional<Patient> findByName(String name) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Patient> cq = cb.createQuery(Patient.class);
            Root<Patient> root = cq.from(Patient.class);

            // WHERE name = :name
            Predicate namePredicate = cb.equal(root.get("name"), name);
            cq.where(namePredicate);

            Patient patient = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(patient);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
