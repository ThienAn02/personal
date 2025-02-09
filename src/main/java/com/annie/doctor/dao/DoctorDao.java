package com.annie.doctor.dao;

import com.annie.doctor.entity.Doctor;
import com.annie.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

@Stateless
public class DoctorDao extends ImplementBaseDAO<Doctor, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    public DoctorDao() {
        super(Doctor.class);
    }

    public Optional<Doctor> findByName(String name) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Doctor> cq = cb.createQuery(Doctor.class);
            Root<Doctor> root = cq.from(Doctor.class);
            Predicate namePredicate = cb.equal(root.get("name"), name);
            cq.where(namePredicate);

            Doctor doctor = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(doctor);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public boolean findByPhone(@NotBlank(message = "Phone number cannot be blank") String phone) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Doctor> criteriaQuery = criteriaBuilder.createQuery(Doctor.class);
            Root<Doctor> root = criteriaQuery.from(Doctor.class);

            Predicate phonePredicate = criteriaBuilder.equal(root.get("phone"), phone);
            criteriaQuery.where(phonePredicate);

            long count = entityManager.createQuery(criteriaQuery).getResultList().size();

            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
