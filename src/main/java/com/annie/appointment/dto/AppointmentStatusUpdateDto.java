package com.annie.appointment.dto;

import com.annie.base.common.Status;
import lombok.Data;

@Data
public class AppointmentStatusUpdateDto {
    private Status status;
}
