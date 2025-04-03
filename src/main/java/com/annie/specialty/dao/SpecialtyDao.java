package com.annie.specialty.dao;

import com.annie.appointment.entity.Appointment;
import com.annie.doctor.entity.Doctor;
import com.annie.patient.entity.Patient;
import com.annie.specialty.dto.SpecialtyDoctorResponseDto;
import com.annie.specialty.dto.SpecialtyResponseDto;
import com.annie.specialty.entity.Specialty;
import com.annie.base.dao.BaseDAO;
import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

@Stateless
public class SpecialtyDao extends BaseDAO<Specialty> {

    public SpecialtyDao() {
        super(Specialty.class);
    }
    public List<SpecialtyDoctorResponseDto> getDoctorsBySpecialty(Long specialtyId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SpecialtyDoctorResponseDto> cq = cb.createQuery(SpecialtyDoctorResponseDto.class);

        Root<Doctor> d = cq.from(Doctor.class);
        Join<Doctor, Specialty> s = d.join("specialty");
        Join<Doctor, Appointment> ap = d.join("appointments");
        Join<Appointment, Patient> p = ap.join("patient");

        cq.select(cb.construct(SpecialtyDoctorResponseDto.class,
                d.get("id"),
                d.get("name"),
                s.get("name"),
                p.get("name"),
                ap.get("dateTime")
        )).where(cb.equal(s.get("id"), specialtyId));

        return entityManager.createQuery(cq).getResultList();
    }

    public Optional<Specialty> findByName(String specialtyName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Specialty> cq = cb.createQuery(Specialty.class);

        Root<Specialty> specialtyRoot = cq.from(Specialty.class);

        cq.select(specialtyRoot)
                .where(cb.equal(specialtyRoot.get("name"), specialtyName));

        Specialty result = entityManager.createQuery(cq).getResultStream().findFirst().orElse(null);

        return Optional.ofNullable(result);
    }

}
