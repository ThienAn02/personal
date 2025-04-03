package com.annie.appointment.service;

import com.annie.appointment.dto.AppointmentRequestDto;
import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.appointment.entity.Appointment;
import com.annie.base.common.Status;
import com.annie.patient.dto.PatientResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AppointmentService {
    AppointmentResponseDto createAppointment(AppointmentRequestDto request, String token);
    List<AppointmentResponseDto> getAppointmentsByPatientId(Long patientId);
    List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId, Status status, LocalDate date, String token);
    AppointmentResponseDto updateAppointmentStatus(Long appointmentId, Status newStatus);
    Map<String, Long> getAppointmentsCountByDoctor();
    List<PatientResponseDto> getPatientsByYearAndMedicine(int year, String medicineName);
    List<AppointmentResponseDto> getDoctorAppointments(
            Long doctorId,
            String dateStr,
            String weekStr,
            String monthStr) ;

    boolean hasAppointmentBetweenDoctorAndPatient(Long id, Long patientId);

    List<Appointment> findCompletedAppointmentsByPatientId(Long id);
}
