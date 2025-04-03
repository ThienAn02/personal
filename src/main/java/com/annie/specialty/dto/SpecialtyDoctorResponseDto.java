package com.annie.specialty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyDoctorResponseDto {
    private Long doctorId;
    private String doctorName;
    private String specialtyName;
    private String patientName;
    private LocalDateTime appointmentDate;
}
