package com.annie.patient.service;


import com.annie.patient.constants.PatientExceptionMessage;
import com.annie.patient.dao.PatientDao;
import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.dto.PatientResponseDto;
import com.annie.patient.entity.Patient;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.util.List;
import java.util.Optional;

@Stateless
public class PatientServiceImpl implements PatientService {

    @Inject
    private PatientDao patientDao;

    @Inject
    private PatientMapper patientMapper;

    @Override
    public PatientResponseDto getPatientById(Long id) {
        Optional<Patient> patientById = patientDao.findById(id);
        return patientById.map(patient -> patientMapper.toResponseDTO(patient))
                .orElseThrow(() -> new BadRequestException(PatientExceptionMessage.PATIENT_NOT_FOUND));
    }

    @Override
    public List<PatientResponseDto> getAllPatients() {
        List<Patient> patients = patientDao.findAll();
        return patientMapper.toResponseDTOList(patients);
    }

    @Override
    public PatientResponseDto addPatient(PatientRequestDto patientRequest) {
        boolean isExisted = patientDao.findByName(patientRequest.getName()).isPresent();
        if (isExisted) {
            throw new IllegalArgumentException(PatientExceptionMessage.PATIENT_NAME_EXISTED);
        }
        Patient patient = patientMapper.toEntity(patientRequest);
        Patient addedPatient = patientDao.save(patient);
        return patientMapper.toResponseDTO(addedPatient);
    }



    @Transactional
    @Override
    public PatientResponseDto updatePatient(Long id, PatientRequestDto patientRequest) {
        boolean isExistedById = patientDao.findById(id).isPresent();
        if (!isExistedById) {
            throw new BadRequestException(PatientExceptionMessage.PATIENT_NOT_FOUND);
        }

        Optional<Patient> existedPatientByName = patientDao.findByName(patientRequest.getName());
        if (existedPatientByName.isPresent()) {
            if (existedPatientByName.get().getId().equals(id)) {
                throw new IllegalArgumentException(PatientExceptionMessage.PATIENT_NAME_DUPLICATED);
            } else {
                throw new IllegalArgumentException(PatientExceptionMessage.PATIENT_NAME_EXISTED);
            }
        }

        Patient patient = patientMapper.toEntity(patientRequest);
        patient.setId(id);
        Patient updatedPatient = patientDao.update(patient);
        return patientMapper.toResponseDTO(updatedPatient);
    }

    @Transactional
    @Override
    public void deletePatient(Long id) {
        boolean isExisted = patientDao.findById(id).isPresent();
        if (!isExisted) {
            throw new BadRequestException(PatientExceptionMessage.PATIENT_NOT_FOUND);
        }
        patientDao.delete(id);
    }
}