package com.annie.patient.service;

import com.annie.medical_record.service.MedicalRecordMapper;
import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.dto.PatientResponseDto;
import com.annie.patient.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi", uses = MedicalRecordMapper.class)
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);
    Patient toEntity(PatientRequestDto dto);
    @Mapping(target = "medicalRecords", source = "medicalRecords") // Ánh xạ medicalRecords từ Patient
    PatientResponseDto toResponseDTO(Patient patient);

    List<PatientResponseDto> toResponseDTOList(List<Patient> patients);
}
