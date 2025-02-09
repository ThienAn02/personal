package com.annie.patient.dto;

import com.annie.medical_record.dto.MedicalRecordResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PatientResponseDto {
    private Long id;
    private String name;
    private String address;
    private Integer age;
    private String gender;
    private String phone;
    private List<MedicalRecordResponseDto> medicalRecords;
}
