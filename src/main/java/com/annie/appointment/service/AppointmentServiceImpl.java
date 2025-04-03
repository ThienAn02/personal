package com.annie.appointment.service;

import com.annie.appointment.constants.AppointmentExceptionMessage;
import com.annie.appointment.dao.AppointmentDao;
import com.annie.appointment.dto.AppointmentRequestDto;
import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.appointment.entity.Appointment;
import com.annie.base.common.Status;
import com.annie.base.configuration.JwtTokenFilter;
import com.annie.base.exception.IdNotFoundException;
import com.annie.base.exception.UnauthorizedException;
import com.annie.doctor.constants.DoctorExceptionMessage;
import com.annie.doctor.dao.DoctorDao;
import com.annie.doctor.entity.Doctor;
import com.annie.doctor.service.DoctorService;
import com.annie.patient.constants.PatientExceptionMessage;
import com.annie.patient.dao.PatientDao;
import com.annie.patient.dto.PatientResponseDto;
import com.annie.patient.entity.Patient;
import com.annie.patient.service.PatientMapper;
import com.annie.patient.service.PatientService;
import com.annie.prescription.service.PrescriptionPdfService;
import com.annie.utils.authservice.AuthService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;

import javax.swing.text.DateFormatter;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class AppointmentServiceImpl implements AppointmentService {
    private static final Logger LOGGER = Logger.getLogger(AppointmentServiceImpl.class.getName());

    @Inject
    private AppointmentDao appointmentDao;


    @Inject
    private AppointmentMapper appointmentMapper;

    @Inject
    private PatientMapper patientMapper;

    @Inject
    private AuthService authService;
    @Inject
    private DoctorDao doctorDao;
    @Inject
    private PatientDao patientDao;


    public AppointmentResponseDto createAppointment(AppointmentRequestDto request, String token) {

        String emailFromToken = JwtTokenFilter.getEmail(token);

        if (emailFromToken == null) {
            throw new NotAuthorizedException(PatientExceptionMessage.INVALID_TOKEN_OR_EMAIL_NOT_FOUND);
        }

        Doctor doctorEntity = doctorDao.findById(request.getDoctorId())
                .orElseThrow(() -> new NotFoundException(DoctorExceptionMessage.DOCTOR_NOT_FOUND));

        Patient patientEntity = patientDao.findById(request.getPatientId())
                .orElseThrow(() -> new NotFoundException(PatientExceptionMessage.PATIENT_NOT_FOUND));

        boolean isAuthorized = emailFromToken.equals(patientEntity.getEmail()) ||
                emailFromToken.equals(doctorEntity.getEmail());

        if (!isAuthorized) {
            LOGGER.warning("Email from token does not match either patient's or doctor's email. Token email: " + emailFromToken);
            throw new NotAuthorizedException(PatientExceptionMessage.UNAUTHORIZED_ACCESS);
        }

        LocalDateTime requestedTime = request.getDateTime();

        if (requestedTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date and time must be in the future.");
        }
        List<LocalDateTime> bookedSlots = appointmentDao.getBookedSlots(request.getDoctorId(), requestedTime.toLocalDate());

        for (LocalDateTime booked : bookedSlots) {
            if (requestedTime.equals(booked) ||
                    (requestedTime.isAfter(booked.minusMinutes(30)) && requestedTime.isBefore(booked.plusMinutes(30)))) {
                throw new IllegalArgumentException(AppointmentExceptionMessage.TIME_SLOT_BOOKED);
            }
        }

        Appointment appointment = appointmentMapper.toEntity(request);
        appointment.setDoctor(doctorEntity);
        appointment.setPatient(patientEntity);
        appointment.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Appointment savedAppointment = appointmentDao.save(appointment);
        return appointmentMapper.toResponseDto(savedAppointment);
    }
    /**
     * Retrieves appointments by patient ID.
     *
     * @param patientId the ID of the patient
     * @return a list of appointment response DTOs
     */
    public List<AppointmentResponseDto> getAppointmentsByPatientId(Long patientId) {
        List<Appointment> appointments = appointmentDao.findByPatientId(patientId);
        return appointments.stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves appointments by doctor ID, status, and/or date.
     *
     * @param doctorId the ID of the doctor
     * @param status   the appointment status (optional)
     * @param date     the date of the appointment (optional)
     * @return a list of appointment response DTOs
     */
    public List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId, Status status, LocalDate date, String token) {
        if (!authService.validateDoctorToken(doctorId, token) && !authService.validateAdminToken(token)) {
            throw new NotAuthorizedException("Unauthorized access");
        }

        List<Appointment> appointments;
        if (status != null && date != null) {
            appointments = appointmentDao.findByDoctorAndStatusAndDate(doctorId, status, date);
        } else if (status != null) {
            appointments = appointmentDao.findByDoctorAndStatus(doctorId, status);
        } else if (date != null) {
            appointments = appointmentDao.findByDoctorAndDate(doctorId, date);
        } else {
            appointments = appointmentDao.findByDoctor(doctorId);
        }

        return appointments.stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates the status of an appointment.
     *
     * @param appointmentId the ID of the appointment
     * @param newStatus     the new status (CONFIRMED or CANCELLED)
     * @return the updated appointment response DTO
     * @throws IllegalArgumentException if the appointment is not found or the status is invalid
     */
    public AppointmentResponseDto updateAppointmentStatus(Long appointmentId, Status newStatus) {
        
        Appointment appointment = appointmentDao.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException(AppointmentExceptionMessage.APPOINTMENT_NOT_FOUND));

        if (newStatus != Status.CONFIRMED && newStatus != Status.CANCELLED) {
            throw new IllegalArgumentException(AppointmentExceptionMessage.INVALID_STATUS);
        }
        appointment.setStatus(newStatus);
        appointmentDao.update(appointment);

        return appointmentMapper.toResponseDto(appointment);
    }
    public Map<String, Long> getAppointmentsCountByDoctor() {
        List<Appointment> appointments = appointmentDao.findAll();
        return appointments.stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getDoctor().getName(), Collectors.counting()));
    }
    public List<PatientResponseDto> getPatientsByYearAndMedicine(int year, String medicineName) {
        List<Patient> patients = appointmentDao.getPatientsByYearAndMedicine(year, medicineName.toLowerCase());
        return patients.stream()
                .sorted(Comparator.comparing(Patient::getName))
                .map(patientMapper::toResponseDTO).toList();
    }
    public List<AppointmentResponseDto> getDoctorAppointments(
            Long doctorId,
            String dateStr,
            String weekStr,
            String monthStr) {

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        if (dateStr != null) {
            LocalDate dateTime = parseLocalDate(dateStr);
            return appointmentMapper.toResponseListDto(
                    appointmentDao.findByDoctorAndDate(doctorId, dateTime)
            );
        } else if (weekStr != null) {
            LocalDate week = parseLocalDate(weekStr);
            LocalDate startOfWeek = week.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            return appointmentMapper.toResponseListDto(
                    appointmentDao.findByDoctorAndDateRange(doctorId, startOfWeek, endOfWeek)
            );
        } else if (monthStr != null) {
            YearMonth month = YearMonth.parse(monthStr, monthFormatter);
            LocalDate startOfMonth = month.atDay(1);
            LocalDate endOfMonth = month.atEndOfMonth();

            return appointmentMapper.toResponseListDto(
                    appointmentDao.findByDoctorAndDateRange(doctorId, startOfMonth, endOfMonth)
            );
        }

        return appointmentMapper.toResponseListDto(
                appointmentDao.findByDoctor(doctorId)
        );
    }

    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        dateStr = dateStr.trim().split("\\s")[0];

        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-M-d"));
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd format.", ex);
            }
        }
    }

    public boolean hasAppointmentBetweenDoctorAndPatient(Long doctorId, Long patientId) {
        List<Appointment> appointments = appointmentDao.findByDoctorAndPatient(doctorId, patientId);
        return !appointments.isEmpty();
    }

    public List<Appointment> findCompletedAppointmentsByPatientId(Long patientId) {
        return appointmentDao.findCompletedByPatientId(patientId);
    }

}
