package com.annie.patient.dto;

import com.annie.base.common.BloodType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PatientHistoryResponseDto {
    private Long patientId;
    private String name;
    private String healthNote;
    private BloodType bloodType;
    private String allergies;
    private List<PatientHistoryItemDto> historyItems;
}