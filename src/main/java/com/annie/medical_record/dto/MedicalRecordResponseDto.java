package com.annie.medical_record.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MedicalRecordResponseDto {
    private Long id;
    private String doctorName;
    private String patientName;
    private LocalDate recordDate;
    private String diagnosis;
    private String treatment;
}
