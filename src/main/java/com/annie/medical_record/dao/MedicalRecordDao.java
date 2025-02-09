package com.annie.medical_record.dao;

import com.annie.medical_record.entity.MedicalRecord;
import com.annie.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Stateless
public class MedicalRecordDao extends ImplementBaseDAO<MedicalRecord, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    public MedicalRecordDao() {
        super(MedicalRecord.class);
    }

    public List<MedicalRecord> findByDoctorId(Long doctorId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MedicalRecord> cq = cb.createQuery(MedicalRecord.class);
        Root<MedicalRecord> root = cq.from(MedicalRecord.class);

        // WHERE doctor_id = :doctorId
        Predicate doctorPredicate = cb.equal(root.get("doctor").get("id"), doctorId);
        cq.where(doctorPredicate);

        return entityManager.createQuery(cq).getResultList();
    }

    public List<MedicalRecord> findByPatientId(Long patientId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MedicalRecord> cq = cb.createQuery(MedicalRecord.class);
        Root<MedicalRecord> root = cq.from(MedicalRecord.class);

        // WHERE patient_id = :patientId
        Predicate patientPredicate = cb.equal(root.get("patient").get("id"), patientId);
        cq.where(patientPredicate);

        return entityManager.createQuery(cq).getResultList();
    }
}
