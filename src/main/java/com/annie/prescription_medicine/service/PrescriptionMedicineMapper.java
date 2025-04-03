package com.annie.prescription_medicine.service;

import com.annie.prescription_medicine.dto.PrescriptionMedicineResponseDto;
import com.annie.prescription_medicine.entity.PrescriptionMedicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "cdi")
public interface PrescriptionMedicineMapper {

    @Mappings({
            @Mapping(source = "prescription.id", target = "prescriptionId"),
            @Mapping(source = "prescription.dateIssued", target = "dateIssued"),
            @Mapping(source = "medicine.id", target = "medicineId"),
            @Mapping(source = "medicine.name", target = "medicineName"),
            @Mapping(source = "prescription.appointment.doctor.name", target = "doctorName"),
            @Mapping(source = "prescription.appointment.patient.name", target = "patientName"),
            @Mapping(source = "dosage", target = "dosage"),
            @Mapping(source = "duration", target = "duration")
    })
    PrescriptionMedicineResponseDto toResponseDTO(PrescriptionMedicine prescriptionMedicine);

}
