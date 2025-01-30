package com.annie.patient.service;


import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.dto.PatientResponseDto;
import com.annie.patient.entity.Patient;
import com.annie.team.dto.request.TeamRequestDTO;
import com.annie.team.dto.response.TeamResponseDTO;
import com.annie.team.entity.Team;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "cdi")
public interface PatientMapper {
    Patient toEntity(PatientRequestDto patientRequestDto);
    PatientResponseDto toResponseDTO(Patient patient);
    List<PatientResponseDto> toResponseDTOList(List<Patient> entities);


}