package com.annie.doctor.service;

import com.annie.doctor.dto.DoctorRequestDto;
import com.annie.doctor.dto.DoctorResponseDto;
import com.annie.doctor.entity.Doctor;
import com.annie.medical_record.service.MedicalRecordMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi", uses = MedicalRecordMapper.class)
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);
    Doctor toEntity(DoctorRequestDto dto);
    @Mapping(target = "medicalRecords", source = "medicalRecords")
    DoctorResponseDto toResponseDTO(Doctor doctor);

    List<DoctorResponseDto> toResponseDTOList(List<Doctor> doctors);
}
