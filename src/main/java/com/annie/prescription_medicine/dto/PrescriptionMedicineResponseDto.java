package com.annie.prescription_medicine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionMedicineResponseDto {
    private Long prescriptionId;  // ID đơn thuốc
    private LocalDateTime dateIssued; // Ngày phát hành đơn thuốc
    private String medicineName;  // Tên thuốc
    private Long medicineId;      // ID thuốc
    private String doctorName;    // Tên bác sĩ
    private String patientName;   // Tên bệnh nhân
    private Integer dosage;       // Liều lượng
    private Integer duration;     // Thời gian sử dụng
}
