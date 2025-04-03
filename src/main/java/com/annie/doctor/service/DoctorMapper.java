package com.annie.doctor.service;

import com.annie.doctor.dto.DoctorRequestDto;
import com.annie.doctor.dto.DoctorResponseDto;
import com.annie.doctor.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface DoctorMapper {
    Doctor toEntity(DoctorRequestDto dto);
    DoctorResponseDto toResponseDTO(Doctor doctor);
    List<DoctorResponseDto> toResponseLisDTO(List<Doctor> doctors);

}
