package com.annie.patient.dao;

import com.annie.patient.entity.Patient;
import com.annie.base.dao.BaseDAO;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Optional;

@Stateless
public class PatientDao extends BaseDAO<Patient> {

    public PatientDao() {
        super(Patient.class);
    }

    public Optional<Patient> findByName(String name) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Patient> cq = cb.createQuery(Patient.class);
            Root<Patient> root = cq.from(Patient.class);

            Predicate namePredicate = cb.equal(root.get("name"), name);
            cq.where(namePredicate);

            Patient patient = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(patient);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
    public Optional<Patient> findByEmail(String email) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Patient> cq = cb.createQuery(Patient.class);
            Root<Patient> root = cq.from(Patient.class);

            Predicate emailPredicate = cb.equal(root.get("email"), email);
            cq.where(emailPredicate);

            Patient patient = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(patient);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
    public int deleteById(Long id) {
        if (id == null) {
            return 0;
        }

        String deleteQuery = "DELETE FROM Patient p WHERE p.id = :id";

        Query query = entityManager.createQuery(deleteQuery)
                .setParameter("id", id);

        return query.executeUpdate();
    }

}
