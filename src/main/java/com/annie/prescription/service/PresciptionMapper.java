package com.annie.prescription.service;

import com.annie.prescription.dto.PrescriptionResponseDto;
import com.annie.prescription.dto.PrescriptionAppointmentDto;
import com.annie.prescription.entity.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "cdi")
public interface PresciptionMapper {
    @Mapping(source = "appointment.id", target = "appointmentId")
    List<PrescriptionResponseDto> toResponseDTOList(List<Prescription> prescriptions);

    @Mapping(source = "appointment.dateTime", target = "appointmentDate")
    @Mapping(source = "appointment.doctor.id", target = "doctorId")
    @Mapping(source = "appointment.doctor.name", target = "doctorName")
    @Mapping(source = "appointment.patient.id", target = "patientId")
    @Mapping(source = "appointment.patient.name", target = "patientName")
    @Mapping(source = "prescription.diagnosis", target = "diagnosisName")
    PrescriptionAppointmentDto toDto(Prescription prescription);

}
