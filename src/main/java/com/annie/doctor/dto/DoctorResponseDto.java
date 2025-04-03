package com.annie.doctor.dto;
import com.annie.appointment.entity.Appointment;
import com.annie.specialty.entity.Specialty;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DoctorResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private LocalDate dob;
    private Integer experienceYears;
    private String qualification;
    private Specialty specialty;

}
