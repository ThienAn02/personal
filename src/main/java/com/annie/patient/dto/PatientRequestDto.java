package com.annie.patient.dto;

import com.annie.patient.entity.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatientRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Age is required") // Đổi @NotBlank thành @NotNull
    private Integer age;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required") // Đổi @NotBlank thành @NotNull
    private Gender gender;

    @NotBlank(message = "Phone is required")
    private String phone;
}
