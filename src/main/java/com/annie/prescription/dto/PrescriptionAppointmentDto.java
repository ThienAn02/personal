package com.annie.prescription.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionAppointmentDto {
    private LocalDateTime appointmentDate;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String diagnosisName;
    private String name;
}
