package com.annie.prescription.service;

import com.annie.prescription.dto.PrescriptionResponseDto;
import com.annie.prescription.dto.PrescriptionAppointmentDto;

import java.util.List;

public interface PresciptionService {
    List<PrescriptionResponseDto> findByAppointmentId (Long id);
    List<PrescriptionAppointmentDto> getAppointmentsByMedicine(String medicineName);
}
