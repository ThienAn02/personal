package com.annie.doctor.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRequestDto {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private LocalDate dob;
    private Integer experienceYears;
    private String qualification;
    private Long specialtyId;
}
