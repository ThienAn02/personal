
package com.annie.patient.service;

import com.annie.appointment.dao.AppointmentDao;
import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.appointment.entity.Appointment;
import com.annie.appointment.service.AppointmentService;
import com.annie.base.configuration.JwtTokenFilter;
import com.annie.base.exception.ForbiddenException;
import com.annie.base.exception.UnauthorizedException;
import com.annie.doctor.entity.Doctor;
import com.annie.doctor.service.DoctorService;
import com.annie.patient.constants.PatientExceptionMessage;
import com.annie.patient.dao.PatientDao;
import com.annie.patient.dto.PatientHistoryItemDto;
import com.annie.patient.dto.PatientHistoryResponseDto;
import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.dto.PatientResponseDto;
import com.annie.patient.entity.Patient;
import com.annie.base.exception.IdNotFoundException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Stateless
public class PatientServiceImpl implements PatientService {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private PatientDao patientDao;
    @Inject
    private AppointmentDao appointmentDao;

    @Inject
    private PatientMapper patientMapper;

    @Inject
    private AppointmentService appointmentService;

    @Inject
    DoctorService doctorService;

    /**
     * Retrieves a patient by ID.
     * @param id Patient ID
     * @return Patient response DTO
     */
    @Override
    public PatientResponseDto getPatientById(Long id) {
        Optional<Patient> patientById = patientDao.findById(id);
        return patientById.map(patient -> patientMapper.toResponseDTO(patient))
                .orElseThrow(() -> new BadRequestException(PatientExceptionMessage.PATIENT_NOT_FOUND));
    }

    /**
     * Retrieves all patients.
     * @return List of patient response DTOs
     */
    @Override
    public List<PatientResponseDto> getAllPatients() {
        List<Patient> patients = patientDao.findAll();
        return patientMapper.toResponseDTOList(patients);
    }

    /**
     * Adds a new patient.
     * @param patientRequest Patient request DTO
     * @return Created patient response DTO
     */
    @Override
    public PatientResponseDto addPatient(PatientRequestDto patientRequest) {
        Patient patient = patientMapper.toEntity(patientRequest);
        Patient addedPatient = patientDao.save(patient);
        return patientMapper.toResponseDTO(addedPatient);
    }

    /**
     * Updates patient details.
     * @param id Patient ID
     * @param patientRequest Updated patient details
     * @return Updated patient response DTO
     */
    @Override
    public PatientResponseDto updatePatient(Long id, PatientRequestDto patientRequest) {
        boolean isExistedById = patientDao.findById(id).isPresent();
        if (!isExistedById) {
            throw new IdNotFoundException(PatientExceptionMessage.PATIENT_NOT_FOUND);
        }
        Patient updateDoctor = patientMapper.toEntity(patientRequest);
        updateDoctor.setId(id);
        return patientMapper.toResponseDTO(patientDao.update(updateDoctor));
    }

    /**
     * Deletes a patient by ID.
     * @param id Patient ID
     */

    /**
     * Retrieves a patient entity by ID.
     * @param id Patient ID
     * @return Patient entity
     */
    @Override
    public Patient getPatientEntityById(Long id) {
        return patientDao.findById(id)
                .orElseThrow(() -> new IdNotFoundException(PatientExceptionMessage.PATIENT_NOT_FOUND));
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsWithEmailCheck(Long patientId, String token) {
        String emailFromToken = JwtTokenFilter.getEmail(token);
        log.info("Email from token: {}", emailFromToken);

        if (emailFromToken == null) {
            throw new UnauthorizedException(PatientExceptionMessage.INVALID_TOKEN_OR_EMAIL_NOT_FOUND);
        }

        Patient patient = getPatientEntityById(patientId);
        if (patient == null) {
            throw new IdNotFoundException(PatientExceptionMessage.PATIENT_NOT_FOUND_WITH_ID + patientId);
        }

        if (!emailFromToken.equals(patient.getEmail())) {
            throw new UnauthorizedException(PatientExceptionMessage.UNAUTHORIZED_ACCESS);
        }

        return appointmentService.getAppointmentsByPatientId(patientId);
    }

    public PatientHistoryResponseDto getPatientHistoryForDoctor(Long patientId, String token) {
        String doctorEmail = JwtTokenFilter.getEmail(token);
        if (doctorEmail == null) {
            throw new UnauthorizedException("Invalid token or email not found");
        }

        Patient patient = getPatientEntityById(patientId);
        if (patient == null) {
            throw new IdNotFoundException("Patient not found with ID: " + patientId);
        }

        Doctor doctor = doctorService.getDoctorByEmail(doctorEmail);
        if (doctor == null) {
            throw new ForbiddenException("Doctor not found with email: " + doctorEmail);
        }

        boolean hasAppointment = appointmentService.hasAppointmentBetweenDoctorAndPatient(doctor.getId(), patientId);
        if (!hasAppointment) {
            throw new ForbiddenException("Not authorized to view this patient's history. The patient does not have an appointment with you.");
        }

        return buildPatientHistoryResponse(patient);
    }

    private PatientHistoryResponseDto buildPatientHistoryResponse(Patient patient) {
        // Get all completed appointments for the patient
         List<PatientHistoryItemDto> historyItems = appointmentDao.findPatientHistoryItemsByPatientId(patient.getId());

        // Build the patient history response using builder pattern
        return PatientHistoryResponseDto.builder()
                .patientId(patient.getId())
                .name(patient.getName())
                .healthNote(patient.getHealthNote())
                .bloodType(patient.getBloodType())
                .allergies(patient.getAllergies())
                .historyItems(historyItems)
                .build();
    }


}
