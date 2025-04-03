package com.annie.appointment.service;

import com.annie.appointment.dto.AppointmentRequestDto;
import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.appointment.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface AppointmentMapper {

    AppointmentResponseDto toResponseDto(Appointment savedAppointment);
    List<AppointmentResponseDto> toResponseListDto(List<Appointment> savedAppointments);
    @Mapping(target = "createdAt", expression = "java(toTimestamp(request.getDateTime()))")
    Appointment toEntity(AppointmentRequestDto request);

    default Timestamp toTimestamp(LocalDateTime localDateTime) {
        return (localDateTime != null) ? Timestamp.valueOf(localDateTime) : null;
    }
}
