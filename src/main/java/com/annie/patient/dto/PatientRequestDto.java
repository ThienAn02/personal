package com.annie.patient.dto;

import com.annie.base.common.BloodType;
import com.annie.base.common.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PatientRequestDto {
    private String name;
    private String email;
    private String address;
    private String phone;
    private String healthNote;
    private String emergencyContact;
    private BloodType bloodType;
    private String allergies;
    private String insuranceNumber;
    private LocalDate dob;
    private Gender gender;

}