package com.annie.specialty.service;

import com.annie.appointment.entity.Appointment;
import com.annie.doctor.entity.Doctor;
import com.annie.specialty.dto.SpecialtyDoctorResponseDto;
import com.annie.specialty.dto.SpecialtyResponseDto;
import com.annie.specialty.entity.Specialty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface SpecialtyMapper {
    SpecialtyMapper INSTANCE = Mappers.getMapper(SpecialtyMapper.class);

    @Mapping(source = "id", target = "doctorId")
    @Mapping(source = "name", target = "doctorName")
    @Mapping(source = "specialty.name", target = "specialtyName")
    @Mapping(source = "appointments", target = "patientName", qualifiedByName = "mapPatientName")
    @Mapping(source = "appointments", target = "appointmentDate", qualifiedByName = "mapAppointmentDate")
    SpecialtyDoctorResponseDto toDto(Doctor doctor);

    @Named("mapPatientName")
    default String mapPatientName(List<Appointment> appointments) {
        return (appointments != null && !appointments.isEmpty() && appointments.get(0).getPatient() != null)
                ? appointments.get(0).getPatient().getName()
                : null;
    }

    @Named("mapAppointmentDate")
    default String mapAppointmentDate(List<Appointment> appointments) {
        return (appointments != null && !appointments.isEmpty() && appointments.get(0).getDateTime() != null)
                ? appointments.get(0).getDateTime().toString()
                : null;
    }
    List<SpecialtyResponseDto> toListResponseDto(List<Specialty> specialties);
}
