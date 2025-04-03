package com.annie.specialty.service;

import com.annie.specialty.dto.SpecialtyDoctorResponseDto;
import com.annie.specialty.dto.SpecialtyResponseDto;

import java.util.List;

public interface SpecialtyService {
    List<SpecialtyDoctorResponseDto> getDoctorsBySpecialty(Long specialtyId);
    List<SpecialtyResponseDto> getAllSpecialty();
}
