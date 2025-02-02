package com.annie.patient.dto;

import com.annie.patient.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatientRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    private String address;

    @NotNull(message = "Age is required")
    private Integer age;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank
    @Size(min = 1, max = 15)
    private String phone;


}