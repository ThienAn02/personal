package com.annie.medical_record.service;

import com.annie.doctor.service.DoctorMapper;
import com.annie.medical_record.dto.MedicalRecordRequestDto;
import com.annie.patient.service.PatientMapper;
import com.annie.medical_record.dto.MedicalRecordResponseDto;
import com.annie.medical_record.entity.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi", uses = {DoctorMapper.class, PatientMapper.class})
public interface MedicalRecordMapper {

    MedicalRecordMapper INSTANCE = Mappers.getMapper(MedicalRecordMapper.class);
    MedicalRecord toEntity(MedicalRecordRequestDto medicalRecordRequestDto);
    @Mapping(target = "doctorName", source = "doctor.name") // Ánh xạ tên bác sĩ
    @Mapping(target = "patientName", source = "patient.name") // Ánh xạ tên bệnh nhân
    MedicalRecordResponseDto toResponseDTO(MedicalRecord entity);

    List<MedicalRecordResponseDto> toResponseDTOList(List<MedicalRecord> records);
}
