package com.annie.doctor.dao;

import com.annie.appointment.entity.Appointment;
import com.annie.base.configuration.AppConfig;
import com.annie.doctor.constants.DoctorExceptionMessage;
import com.annie.doctor.entity.Doctor;
import com.annie.base.dao.BaseDAO;
import com.annie.specialty.entity.Specialty;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class DoctorDao extends BaseDAO<Doctor> {

    public DoctorDao() {
        super(Doctor.class);
    }

    public boolean findByPhone(@NotBlank(message = "Phone number cannot be blank") String phone) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Doctor> root = query.from(Doctor.class);

        query.select(cb.count(root))
                .where(cb.equal(root.get("phone"), phone));

        Long count = entityManager.createQuery(query).getSingleResult();
        return count > 0;
    }

    public List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(DoctorExceptionMessage.SELECTED_DATE_NOT_PERMIT);
        }

        LocalTime startTime1 = LocalTime.of(8, 0);
        LocalTime endTime1 = LocalTime.of(12, 0);
        LocalTime startTime2 = LocalTime.of(13, 30);
        LocalTime endTime2 = LocalTime.of(17, 0);

        List<LocalDateTime> allSlots = new ArrayList<>();
        allSlots.addAll(generateDoctorSchedule(date, startTime1, endTime1));
        allSlots.addAll(generateDoctorSchedule(date, startTime2, endTime2));

        String jpql = "SELECT DISTINCT a.dateTime FROM Appointment a " +
                "WHERE a.doctor.id = :doctorId " +
                "AND FUNCTION('DATE', a.dateTime) = :date";

        TypedQuery<LocalDateTime> query = entityManager.createQuery(jpql, LocalDateTime.class);
        query.setParameter("doctorId", doctorId);
        query.setParameter("date", java.sql.Timestamp.valueOf(date.atStartOfDay()));

        List<LocalDateTime> bookedSlots = query.getResultList();
        return filterAvailableSlots(allSlots, bookedSlots);
    }


    private List<LocalDateTime> generateDoctorSchedule(LocalDate date, LocalTime start, LocalTime end) {
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime current = date.atTime(start);

        while (current.toLocalTime().isBefore(end)) {
            slots.add(current);
            current = current.plusMinutes(30);
        }

        return slots;
    }
    private List<LocalDateTime> filterAvailableSlots(List<LocalDateTime> allSlots, List<LocalDateTime> bookedSlots) {
        Set<LocalDateTime> bookedSet = new HashSet<>(bookedSlots);
        List<LocalDateTime> availableSlots = new ArrayList<>();

        for (LocalDateTime slot : allSlots) {
            boolean isBooked = bookedSet.contains(slot);
            if (!isBooked) {
                availableSlots.add(slot);
            }
        }
        return availableSlots;
    }

    public List<Doctor> getDoctorsBySpecialty(String specialtyName) {
        String defaultSpecialty = AppConfig.getDefaultSpecialty();
        if (specialtyName == null || specialtyName.trim().isEmpty()) {
            specialtyName = defaultSpecialty;
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Doctor> query = cb.createQuery(Doctor.class);
        Root<Doctor> doctor = query.from(Doctor.class);
        Join<Doctor, Specialty> specialty = doctor.join("specialty");

        String searchTerm = specialtyName.toLowerCase();

        Predicate nameCondition = cb.like(
                cb.lower(specialty.get("name")),
                "%" + searchTerm + "%"
        );

        Predicate descriptionCondition = cb.like(
                cb.lower(specialty.get("description")),
                "%" + searchTerm + "%"
        );

        Predicate finalCondition = cb.or(nameCondition, descriptionCondition);

        query.select(doctor).where(finalCondition);

        return entityManager.createQuery(query).getResultList();
    }


    public Doctor getDoctorByEmail(String doctorEmail) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Doctor> query = cb.createQuery(Doctor.class);
        Root<Doctor> doctor = query.from(Doctor.class);

        Predicate emailCondition = cb.equal(doctor.get("email"), doctorEmail);
        Predicate notDeletedCondition = cb.isFalse(doctor.get("isDeleted"));
        query.select(doctor).where(cb.and(emailCondition, notDeletedCondition));

        return entityManager.createQuery(query).getSingleResult();

    }

}


