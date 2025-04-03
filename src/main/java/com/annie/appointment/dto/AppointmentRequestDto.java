package com.annie.appointment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequestDto {
    private Long doctorId;
    private Long patientId;
    private LocalDateTime dateTime;
}