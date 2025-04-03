package com.annie.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientHistoryItemDto {
    private Long appointmentId;
    private LocalDateTime date;
    private String doctorName;
    private String doctorSpecialty;
    private Long prescriptionId;
    private String diagnosis;
    private String treatment;
}