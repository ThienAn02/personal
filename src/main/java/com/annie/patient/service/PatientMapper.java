package com.annie.patient.service;

import com.annie.appointment.entity.Appointment;
import com.annie.patient.dto.PatientHistoryItemDto;
import com.annie.patient.dto.PatientHistoryResponseDto;
import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.dto.PatientResponseDto;
import com.annie.patient.entity.Patient;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface PatientMapper {

    Patient toEntity(PatientRequestDto dto);

    PatientResponseDto toResponseDTO(Patient patient);

    List<PatientResponseDto> toResponseDTOList(List<Patient> patients);

    @Mapping(target = "historyItems", ignore = true)
    PatientHistoryResponseDto toPatientHistoryDto(Patient patient);

    List<PatientHistoryItemDto> appointmentsToHistoryItems(List<Appointment> appointments);

}