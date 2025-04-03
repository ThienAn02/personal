package com.annie.patient.dto;

import com.annie.appointment.entity.Appointment;
import com.annie.base.common.BloodType;
import com.annie.base.common.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class PatientResponseDto {
    private Long id;
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
