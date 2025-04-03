package com.annie.prescription_medicine.dao;
import com.annie.appointment.entity.Appointment;
import com.annie.doctor.entity.Doctor;
import com.annie.medicine.entity.Medicine;
import com.annie.patient.entity.Patient;
import com.annie.prescription.entity.Prescription;
import com.annie.prescription_medicine.dto.PrescriptionMedicineResponseDto;
import com.annie.prescription_medicine.entity.PrescriptionMedicine;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
;

import java.util.List;
@Stateless
public class PrescriptionMedicineDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PrescriptionMedicineResponseDto> findMedicineByPresciption(Long prescriptionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PrescriptionMedicineResponseDto> cq = cb.createQuery(PrescriptionMedicineResponseDto.class);

        Root<PrescriptionMedicine> pm = cq.from(PrescriptionMedicine.class);
        Join<PrescriptionMedicine, Medicine> m = pm.join("medicine");
        Join<PrescriptionMedicine, Prescription> pr = pm.join("prescription");
        Join<Prescription, Appointment> ap = pr.join("appointment");
        Join<Appointment, Doctor> d = ap.join("doctor");
        Join<Appointment, Patient> p = ap.join("patient");

        cq.select(cb.construct(PrescriptionMedicineResponseDto.class,
                pr.get("id"),          // ID đơn thuốc
                pr.get("dateIssued"),  // Ngày phát hành đơn thuốc
                m.get("name"),         // Tên thuốc
                m.get("id"),           // ID thuốc
                d.get("name"),         // Tên bác sĩ
                p.get("name"),         // Tên bệnh nhân
                pm.get("dosage"),      // Liều lượng
                pm.get("duration")     // Thời gian sử dụng
        )).where(cb.equal(pr.get("id"), prescriptionId));

        return entityManager.createQuery(cq).getResultList();
    }
    public int deleteAllByPrescriptions(List<Long> prescriptionIds) {
        if (prescriptionIds == null || prescriptionIds.isEmpty()) {
            return 0;
        }

        String deleteQuery = "DELETE FROM PrescriptionMedicine pm WHERE pm.prescription.id IN :prescriptionIds";

        Query query = entityManager.createQuery(deleteQuery)
                .setParameter("prescriptionIds", prescriptionIds);

        return query.executeUpdate();
    }

}
