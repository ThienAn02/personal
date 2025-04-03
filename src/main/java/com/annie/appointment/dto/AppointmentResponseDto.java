package com.annie.appointment.dto;

import com.annie.base.common.Status;
import com.annie.doctor.entity.Doctor;
import com.annie.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Data
public class AppointmentResponseDto {
    private Long id;
    private LocalDateTime dateTime;
    private Status status;
    private Timestamp createdAt;
    private Doctor doctor;
    private Patient patient;
}
