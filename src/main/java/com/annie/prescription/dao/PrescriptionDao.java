package com.annie.prescription.dao;

import com.annie.appointment.entity.Appointment;
import com.annie.base.dao.BaseDAO;
import com.annie.doctor.entity.Doctor;
import com.annie.medicine.entity.Medicine;
import com.annie.patient.entity.Patient;
import com.annie.prescription.dto.PrescriptionAppointmentDto;
import com.annie.prescription.entity.Prescription;
import com.annie.prescription_medicine.entity.PrescriptionMedicine;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;

import java.util.List;

@Stateless
public class PrescriptionDao extends BaseDAO<Prescription> {

    public PrescriptionDao() {
        super(Prescription.class);
    }

    public List<Prescription> findByAppointmentId(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Prescription> cq = cb.createQuery(Prescription.class);
        Root<Prescription> root = cq.from(Prescription.class);
        Join<Prescription, Appointment> appointmentJoin = root.join("appointment");
        Join<Appointment, Doctor> doctorJoin =appointmentJoin.join("doctor");
        Predicate namePredicate =cb.equal(appointmentJoin.get("id"),id);
        cq.select(root).where(namePredicate);
        return entityManager.createQuery(cq).getResultList();

    }
    public List<PrescriptionAppointmentDto> getAppointmentsByMedicineName(String medicineName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PrescriptionAppointmentDto> cq = cb.createQuery(PrescriptionAppointmentDto.class);

        Root<PrescriptionMedicine> pm = cq.from(PrescriptionMedicine.class);
        Join<PrescriptionMedicine, Medicine> m = pm.join("medicine");
        Join<PrescriptionMedicine, Prescription> p = pm.join("prescription");
        Join<Prescription, Appointment> a = p.join("appointment");
        Join<Appointment, Doctor> d = a.join("doctor");
        Join<Appointment, Patient> pa = a.join("patient");

        cq.select(cb.construct(PrescriptionAppointmentDto.class,
                a.get("dateTime"),
                d.get("id"),
                d.get("name"),
                pa.get("id"),
                pa.get("name"),
                p.get("diagnosis"),
                m.get("name")
        )).where(cb.equal(m.get("name"), medicineName));

        return entityManager.createQuery(cq).getResultList();
    }
    public int deleteAllById(List<Long> prescriptionIds) {
        if (prescriptionIds == null || prescriptionIds.isEmpty()) {
            return 0;
        }

        String deleteQuery = "DELETE FROM Prescription p WHERE p.id IN :prescriptionIds";

        Query query = entityManager.createQuery(deleteQuery)
                .setParameter("prescriptionIds", prescriptionIds);

        return query.executeUpdate();
    }

}

