package com.annie.prescription_medicine.service;

import com.annie.prescription_medicine.dto.PrescriptionMedicineResponseDto;

import java.util.List;

public interface PrescriptionMedicineService {
    List<PrescriptionMedicineResponseDto> getMedicineByPrescription(Long prescriptionId);
}
