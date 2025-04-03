package com.annie.patient.service;

import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.appointment.entity.Appointment;
import com.annie.patient.dto.PatientHistoryResponseDto;
import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.dto.PatientResponseDto;
import com.annie.patient.entity.Patient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;

import java.util.List;

public interface PatientService {
    PatientResponseDto getPatientById(Long id);
    List<PatientResponseDto> getAllPatients();
    PatientResponseDto addPatient(PatientRequestDto patientRequest);
    PatientResponseDto updatePatient(Long id, PatientRequestDto patientRequest);
    Patient getPatientEntityById(Long id);

    List<AppointmentResponseDto> getAppointmentsWithEmailCheck(Long patientId, String token);

    PatientHistoryResponseDto getPatientHistoryForDoctor(Long patientId, String token);
}
