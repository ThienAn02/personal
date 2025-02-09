package com.annie.doctor.service;

import com.annie.doctor.dto.DoctorRequestDto;
import com.annie.doctor.dto.DoctorResponseDto;

import java.util.List;

public interface DoctorService {
    DoctorResponseDto getDoctorById(Long id);
    List<DoctorResponseDto> getAllDoctors();
    DoctorResponseDto addDoctor(DoctorRequestDto doctorRequest);
    DoctorResponseDto updateDoctor(Long id, DoctorRequestDto doctorRequest);
    void deleteDoctor(Long id);
}
