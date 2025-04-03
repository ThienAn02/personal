package com.annie.medicine.service;

import com.annie.medicine.dto.MedicineResponseDto;
import com.annie.medicine.entity.Medicine;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "cdi")
public interface MedicineMapper {
    List<MedicineResponseDto> toMedicineResponseDTOs(List<Medicine> medicines);
}
