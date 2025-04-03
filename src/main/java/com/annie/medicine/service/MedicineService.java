package com.annie.medicine.service;

import com.annie.medicine.dto.MedicineResponseDto;

import java.util.List;

public interface MedicineService {
    List<MedicineResponseDto> findMedicineByName(String name,Integer year);
    List<MedicineResponseDto> getMedicinesByPrescriptionId(Integer prescriptionId);
}
