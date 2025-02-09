package com.annie.doctor.dto;

import com.annie.common.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRequestDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @NotBlank(message = "Phone number cannot be blank")
    private String phone;

    @NotBlank(message = "Specialty cannot be blank")
    private String specialty;
}
