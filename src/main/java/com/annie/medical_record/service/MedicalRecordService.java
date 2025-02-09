package com.annie.medical_record.service;

import com.annie.medical_record.dto.MedicalRecordRequestDto;
import com.annie.medical_record.dto.MedicalRecordResponseDto;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecordResponseDto getMedicalRecordById(Long id);
    List<MedicalRecordResponseDto> getMedicalRecordsByDoctorId(Long doctorId);
    List<MedicalRecordResponseDto> getMedicalRecordsByPatientId(Long patientId);
    MedicalRecordResponseDto addMedicalRecord(MedicalRecordRequestDto recordRequest);
    MedicalRecordResponseDto updateMedicalRecord(Long id, MedicalRecordRequestDto recordRequest);
    void deleteMedicalRecord(Long id);

    List<MedicalRecordResponseDto> getAllMedicalRecords();
}