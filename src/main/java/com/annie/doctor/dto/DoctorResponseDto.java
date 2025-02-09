package com.annie.doctor.dto;

import com.annie.medical_record.dto.MedicalRecordResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DoctorResponseDto {
    private Long id;
    private String name;
    private String address;
    private String gender;
    private String phone;
    private String specialty;
    private List<MedicalRecordResponseDto> medicalRecords;
}
