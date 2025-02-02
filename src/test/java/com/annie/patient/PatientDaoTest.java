package com.annie.patient;

import com.annie.patient.dao.PatientDao;
import com.annie.patient.entity.Patient;
import com.annie.utils.HibernateFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientDaoTest {

    @Mock
    private Session session;

    @Mock
    private Query<Patient> query;

    @InjectMocks
    private PatientDao patientDao;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
    }

    @Test
    void findById_PatientExists_ReturnsPatient() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.find(Patient.class, 1L)).thenReturn(patient);

            Optional<Patient> result = patientDao.findById(1L);

            assertTrue(result.isPresent());
            assertEquals("John Doe", result.get().getName());
            verify(session, times(1)).find(Patient.class, 1L);
        }
    }

    @Test
    void findById_PatientNotFound_ReturnsEmpty() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.find(Patient.class, 1L)).thenReturn(null);

            Optional<Patient> result = patientDao.findById(1L);

            assertFalse(result.isPresent());
            verify(session, times(1)).find(Patient.class, 1L);
        }
    }

    @Test
    void findAll_PatientsExist_ReturnsList() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM com.annie.patient.entity.Patient", Patient.class))
                    .thenReturn(query);
            when(query.getResultList()).thenReturn(Collections.singletonList(patient));

            List<Patient> result = patientDao.findAll();

            assertEquals(1, result.size());
            assertEquals("John Doe", result.get(0).getName());
            verify(session, times(1)).createQuery("FROM com.annie.patient.entity.Patient", Patient.class);
            verify(query, times(1)).getResultList();
        }
    }

    @Test
    void findAll_NoPatients_ReturnsEmptyList() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM com.annie.patient.entity.Patient", Patient.class)).thenReturn(query);
            when(query.getResultList()).thenReturn(Collections.emptyList());

            List<Patient> result = patientDao.findAll();

            assertTrue(result.isEmpty());
            verify(session, times(1)).createQuery("FROM com.annie.patient.entity.Patient", Patient.class);
            verify(query, times(1)).getResultList();
        }
    }

    @Test
    void save_ValidPatient_SavesSuccessfully() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            doNothing().when(session).persist(patient);

            Patient result = patientDao.save(patient);

            assertNotNull(result);
            assertEquals("John Doe", result.getName());
            verify(session, times(1)).persist(patient);
        }
    }

    @Test
    void update_ValidPatient_UpdatesSuccessfully() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.merge(patient)).thenReturn(patient);

            Patient result = patientDao.update(patient);

            assertNotNull(result);
            assertEquals("John Doe", result.getName());
            verify(session, times(1)).merge(patient);
        }
    }

    @Test
    void delete_PatientExists_DeletesSuccessfully() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.find(Patient.class, 1L)).thenReturn(patient);
            doNothing().when(session).remove(patient);

            patientDao.delete(1L);

            verify(session, times(1)).find(Patient.class, 1L);
            verify(session, times(1)).remove(patient);
        }
    }

    @Test
    void delete_PatientNotFound_DoesNothing() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.find(Patient.class, 1L)).thenReturn(null);

            patientDao.delete(1L);

            verify(session, times(1)).find(Patient.class, 1L);
            verify(session, times(0)).remove(any(Patient.class));
        }
    }

    @Test
    void findByName_PatientExists_ReturnsPatient() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM Patient WHERE name = :name", Patient.class)).thenReturn(query);
            when(query.setParameter("name", "John Doe")).thenReturn(query);
            when(query.getSingleResultOrNull()).thenReturn(patient);

            Optional<Patient> result = patientDao.findByName("John Doe");

            assertTrue(result.isPresent());
            assertEquals("John Doe", result.get().getName());
            verify(session, times(1)).createQuery("FROM Patient WHERE name = :name", Patient.class);
            verify(query, times(1)).setParameter("name", "John Doe");
            verify(query, times(1)).getSingleResultOrNull();
        }
    }

    @Test
    void findByName_PatientNotFound_ReturnsEmpty() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM Patient WHERE name = :name", Patient.class)).thenReturn(query);
            when(query.setParameter("name", "Unknown")).thenReturn(query);
            when(query.getSingleResultOrNull()).thenReturn(null);

            Optional<Patient> result = patientDao.findByName("Unknown");

            assertFalse(result.isPresent());
            verify(session, times(1)).createQuery("FROM Patient WHERE name = :name", Patient.class);
            verify(query, times(1)).setParameter("name", "Unknown");
            verify(query, times(1)).getSingleResultOrNull();
        }
    }
}
