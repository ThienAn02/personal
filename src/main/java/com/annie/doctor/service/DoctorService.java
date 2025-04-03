package com.annie.doctor.service;

import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.doctor.dto.DoctorRequestDto;
import com.annie.doctor.dto.DoctorResponseDto;
import com.annie.doctor.entity.Doctor;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public interface DoctorService {
    DoctorResponseDto getDoctorById(Long id);
    List<DoctorResponseDto> getAllDoctors();
    DoctorResponseDto addDoctor(DoctorRequestDto doctorRequest);
    DoctorResponseDto updateDoctor(Long id, DoctorRequestDto doctorRequest);
    void deleteDoctor(Long id);
    List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date);
    Doctor getDoctorEntityById(Long id);
    List<DoctorResponseDto> getDoctorsByStatus(boolean isDeleted);
    List<DoctorResponseDto> getDoctorsBySpecialty(String specialtyName);
    Doctor getDoctorByEmail(String doctorEmail);
    List<DoctorResponseDto> importDoctorsFromExcel(MultipartFormDataInput input);
}
