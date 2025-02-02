package com.annie.patient.service;

import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.dto.PatientResponseDto;

import java.util.List;

public interface PatientService {
    PatientResponseDto getPatientById(Long id);
    List<PatientResponseDto> getAllPatients();
    PatientResponseDto addPatient(PatientRequestDto patientRequest);
    PatientResponseDto updatePatient(Long id, PatientRequestDto patientRequest);
    void deletePatient(Long id);
}
