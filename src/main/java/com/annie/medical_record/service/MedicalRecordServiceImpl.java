package com.annie.medical_record.service;

import com.annie.medical_record.dao.MedicalRecordDao;
import com.annie.medical_record.dto.MedicalRecordRequestDto;
import com.annie.medical_record.dto.MedicalRecordResponseDto;
import com.annie.medical_record.entity.MedicalRecord;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Inject
    private MedicalRecordDao medicalRecordDao;

    @Inject
    private MedicalRecordMapper medicalRecordMapper;

    @Override
    public MedicalRecordResponseDto getMedicalRecordById(Long id) {
        Optional<MedicalRecord> recordById = medicalRecordDao.findById(id);
        return recordById.map(medicalRecordMapper::toResponseDTO)
                .orElseThrow(() -> new BadRequestException("Medical Record not found"));
    }

    @Override
    public List<MedicalRecordResponseDto> getMedicalRecordsByDoctorId(Long doctorId) {
        List<MedicalRecord> records = medicalRecordDao.findByDoctorId(doctorId);
        return medicalRecordMapper.toResponseDTOList(records);
    }

    @Override
    public List<MedicalRecordResponseDto> getMedicalRecordsByPatientId(Long patientId) {
        List<MedicalRecord> records = medicalRecordDao.findByPatientId(patientId);
        return medicalRecordMapper.toResponseDTOList(records);
    }

    @Override
    public MedicalRecordResponseDto addMedicalRecord(MedicalRecordRequestDto recordRequest) {
        MedicalRecord record = medicalRecordMapper.toEntity(recordRequest);
        MedicalRecord addedRecord = medicalRecordDao.save(record);
        return medicalRecordMapper.toResponseDTO(addedRecord);
    }

    @Override
    public MedicalRecordResponseDto updateMedicalRecord(Long id, MedicalRecordRequestDto recordRequest) {
        Optional<MedicalRecord> existingRecordOpt = medicalRecordDao.findById(id);
        if (existingRecordOpt.isEmpty()) {
            throw new BadRequestException("Medical Record not found");
        }

        MedicalRecord existingRecord = existingRecordOpt.get();
        if (recordRequest.getDiagnosis() != null) {
            existingRecord.setDiagnosis(recordRequest.getDiagnosis());
        }

        MedicalRecord updatedRecord = medicalRecordDao.update(existingRecord);
        return medicalRecordMapper.toResponseDTO(updatedRecord);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        if (medicalRecordDao.findById(id).isEmpty()) {
            throw new BadRequestException("Medical Record not found");
        }
        medicalRecordDao.delete(id);
    }
    @Override
    public List<MedicalRecordResponseDto> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordDao.findAll();

        return medicalRecords.stream()
                .map(medicalRecord -> medicalRecordMapper.toResponseDTO(medicalRecord))
                .collect(Collectors.toList());
    }

}