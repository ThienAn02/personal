package com.annie.appointment.dao;


import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.appointment.entity.Appointment;
import com.annie.base.common.Status;
import com.annie.base.dao.BaseDAO;
import com.annie.doctor.entity.Doctor;
import com.annie.patient.dto.PatientHistoryItemDto;
import com.annie.patient.entity.Patient;
import com.annie.prescription.entity.Prescription;
import com.annie.specialty.entity.Specialty;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Stateless
public class AppointmentDao extends BaseDAO<Appointment> {

    public AppointmentDao() {
        super(Appointment.class);
    }

    public List<LocalDateTime> getBookedSlots(Long doctorId, LocalDate date) {
        String jpql = "SELECT a.dateTime FROM Appointment a " +
                "WHERE a.doctor.id = :doctorId " +
                "AND FUNCTION('DATE', a.dateTime) = :date";

        TypedQuery<LocalDateTime> query = entityManager.createQuery(jpql, LocalDateTime.class);
        query.setParameter("doctorId", doctorId);
        query.setParameter("date", java.sql.Date.valueOf(date));

        return query.getResultList();
    }

    public List<Appointment> findByPatientId(Long patientId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);

        root.fetch("doctor", JoinType.LEFT);
        root.fetch("patient", JoinType.LEFT);

        cq.select(root)
                .where(cb.equal(root.get("patient").get("id"), patientId));

        return entityManager.createQuery(cq).getResultList();
    }

    public List<Appointment> findByDoctor(Long doctorId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);

        root.fetch("doctor", JoinType.INNER);
        root.fetch("patient", JoinType.INNER);

        query.select(root)
                .where(cb.equal(root.get("doctor").get("id"), doctorId));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Appointment> findByDoctorAndStatus(Long doctorId, Status status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);

        root.fetch("doctor", JoinType.INNER);
        root.fetch("patient", JoinType.INNER);

        query.select(root)
                .where(
                        cb.equal(root.get("doctor").get("id"), doctorId),
                        cb.equal(root.get("status"), status)
                );

        List<Appointment> appointments = entityManager.createQuery(query).getResultList();

        appointments.forEach(appointment -> Hibernate.initialize(appointment.getDoctor().getAppointments()));

        return appointments;
    }


    public List<Appointment> findByDoctorAndDate(Long doctorId, LocalDate date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);

        root.fetch("doctor", JoinType.INNER);
        root.fetch("patient", JoinType.INNER);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        query.select(root)
                .where(
                        cb.equal(root.get("doctor").get("id"), doctorId),
                        cb.between(root.get("dateTime"), startOfDay, endOfDay)
                );

        return entityManager.createQuery(query).getResultList();
    }

    public List<Appointment> findByDoctorAndStatusAndDate(Long doctorId, Status status, LocalDate date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);

        root.fetch("doctor", JoinType.INNER);
        root.fetch("patient", JoinType.INNER);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        query.select(root)
                .where(
                        cb.equal(root.get("doctor").get("id"), doctorId),
                        cb.equal(root.get("status"), status),
                        cb.between(root.get("dateTime"), startOfDay, endOfDay)
                );

        return entityManager.createQuery(query).getResultList();
    }
    public List<Patient> getPatientsByYearAndMedicine(int year, String medicineName) {
        String nativeQuery = """
    SELECT DISTINCT p.* FROM patient p
    JOIN appointment a ON p.id = a.patient_id
    JOIN prescription pr ON a.id = pr.appointment_id
    JOIN prescription_medicine pm ON pr.id = pm.prescription_id
    JOIN medicine m ON pm.medicine_id = m.id
    WHERE EXTRACT(YEAR FROM a.created_at) = :year
    AND LOWER(m.name) = :medicineName
    """;

        return entityManager.createNativeQuery(nativeQuery, Patient.class)
                .setParameter("year", year)
                .setParameter("medicineName", medicineName.toLowerCase())
                .getResultList();
    }
    public int deleteAllById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        String deleteQuery = "DELETE FROM Appointment a WHERE a.id IN :appointmentIds";

        Query query = entityManager.createQuery(deleteQuery)
                .setParameter("appointmentIds", ids);

        return query.executeUpdate();
    }


    public List<Appointment> findByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);

        root.fetch("doctor", JoinType.INNER);
        root.fetch("patient", JoinType.INNER);

        LocalDateTime startOfFirstDay = startDate.atStartOfDay();
        LocalDateTime endOfLastDay = endDate.atTime(23, 59, 59);

        query.select(root)
                .where(
                        cb.equal(root.get("doctor").get("id"), doctorId),
                        cb.between(root.get("dateTime"), startOfFirstDay, endOfLastDay)
                );

        return entityManager.createQuery(query).getResultList();
    }


    public List<Appointment> findCompletedByPatientId(Long patientId) {
        LocalDateTime now = LocalDateTime.now();

        return entityManager.createQuery(
                        "SELECT a FROM Appointment a " +
                                "WHERE a.patient.id = :patientId " +
                                "AND (a.status = :pendingStatus OR a.status = :confirmedStatus) " +
                                "AND a.dateTime < :currentTime " +
                                "ORDER BY a.dateTime DESC",
                        Appointment.class)
                .setParameter("patientId", patientId)
                .setParameter("pendingStatus", Status.PENDING)
                .setParameter("confirmedStatus", Status.CONFIRMED)
                .setParameter("currentTime", now)
                .getResultList();
    }

    public List<Appointment> findByDoctorAndPatient(Long doctorId, Long patientId) {
        return entityManager.createQuery(
                        "SELECT a FROM Appointment a " +
                                "WHERE a.doctor.id = :doctorId AND a.patient.id = :patientId",
                        Appointment.class)
                .setParameter("doctorId", doctorId)
                .setParameter("patientId", patientId)
                .getResultList();
    }
    public List<PatientHistoryItemDto> findPatientHistoryItemsByPatientId(Long patientId) {
        String jpql = """
        SELECT new com.annie.patient.dto.PatientHistoryItemDto(
            a.id,
            a.dateTime,
            d.name,
            s.name,
            p.id,
            p.diagnosis,
            p.treatment
        )
        FROM Appointment a
        JOIN a.doctor d
        JOIN d.specialty s
        LEFT JOIN a.prescription p
        WHERE a.patient.id = :patientId
        ORDER BY a.id ASC
        """;

        return entityManager.createQuery(jpql, PatientHistoryItemDto.class)
                .setParameter("patientId", patientId)
                .getResultList();
    }
}
