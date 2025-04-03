package com.annie.medicine.dao;
import com.annie.base.dao.BaseDAO;
import com.annie.medicine.entity.Medicine;
import com.annie.prescription_medicine.entity.PrescriptionMedicine;
import jakarta.ejb.Stateless;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MedicineDao extends BaseDAO<Medicine> {
    public MedicineDao() {
        super(Medicine.class);
    }

    public List<Medicine> findMedicineByName(String name, Integer year) {
        LocalDateTime startDateTime = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(year + 1, 1, 1, 0, 0);

        String jpql = "SELECT DISTINCT m FROM Medicine m " +
                "JOIN m.prescriptionMedicines pm " +
                "JOIN pm.prescription pr " +
                "JOIN pr.appointment a " +
                "JOIN a.patient p " +
                "WHERE LOWER(p.name) LIKE :patientName " +
                "AND a.dateTime >= :startDate AND a.dateTime < :endDate";

        TypedQuery<Medicine> query = entityManager.createQuery(jpql, Medicine.class);
        query.setParameter("patientName", "%" + name.toLowerCase() + "%");
        query.setParameter("startDate", startDateTime);
        query.setParameter("endDate", endDateTime);

        return query.getResultList();
    }
    public List<Medicine> findMedicinesByPrescriptionId(Integer prescriptionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Medicine> cq = cb.createQuery(Medicine.class);
        Root<PrescriptionMedicine> pmRoot = cq.from(PrescriptionMedicine.class);
        Join<PrescriptionMedicine, Medicine> medicineJoin = pmRoot.join("medicine");

        cq.select(medicineJoin)
                .where(cb.equal(pmRoot.get("prescription").get("serial"), prescriptionId));

        return entityManager.createQuery(cq).getResultList();
    }

}

