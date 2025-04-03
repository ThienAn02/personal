package com.annie.prescription.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PrescriptionResponseDto {
    private Long id;
    private LocalDateTime dateIssued;
    private String diagnosis;
    private String treatment;
    private String note;
    private LocalDate nextAppointmentDate;
    private Long appointmentId;
}
