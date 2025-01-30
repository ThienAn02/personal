package com.annie.patient.dao;


import com.annie.patient.entity.Patient;
import com.annie.utils.HibernateFactory;
import com.annie.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import org.hibernate.Session;

import java.util.Optional;

@Stateless
public class PatientDao extends ImplementBaseDAO<Patient, Long> {

    public PatientDao() {
        super(Patient.class);
    }

    public Optional<Patient> findByName(String name) {
        try (Session session = HibernateFactory.getSession();) {
            Patient patient = session.createQuery("FROM Patient WHERE name = :name", Patient.class)
                    .setParameter("name", name)
                    .getSingleResultOrNull();
            return Optional.ofNullable(patient);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
