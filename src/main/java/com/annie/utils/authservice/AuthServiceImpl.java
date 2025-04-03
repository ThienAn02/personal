package com.annie.utils.authservice;

import com.annie.base.common.Role;
import com.annie.base.configuration.JwtTokenFilter;
import com.annie.doctor.constants.DoctorExceptionMessage;
import com.annie.doctor.entity.Doctor;
import com.annie.doctor.service.DoctorService;
import com.annie.patient.constants.PatientExceptionMessage;
import com.annie.patient.entity.Patient;
import com.annie.patient.service.PatientService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import java.util.logging.Logger;

@Stateless
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthServiceImpl.class.getName());

    @Inject
    private PatientService patientService;

    @Inject
    private DoctorService doctorService;

    @Override
    public boolean validatePatientToken(Long patientId, String token) {
        String emailFromToken = getEmailFromToken(token);
        LOGGER.info("Email from token: " + emailFromToken);

        Patient patient = patientService.getPatientEntityById(patientId);
        if (patient == null) {
            throw new NotFoundException(PatientExceptionMessage.PATIENT_NOT_FOUND_WITH_ID + patientId);
        }

        boolean isValid = emailFromToken.equals(patient.getEmail());

        if (!isValid) {
            LOGGER.warning("Email from token does not match patient's email. Token email: " +
                    emailFromToken + ", Patient ID: " + patientId);
        }

        return isValid;
    }

    @Override
    public boolean validateDoctorToken(Long doctorId, String token) {
        String emailFromToken = getEmailFromToken(token);
        LOGGER.info("Email from token: " + emailFromToken);

        Doctor doctor = doctorService.getDoctorEntityById(doctorId);
        if (doctor == null) {
            throw new NotFoundException(DoctorExceptionMessage.DOCTOR_NOT_FOUND);
        }

        boolean isValid = emailFromToken.equals(doctor.getEmail());

        if (!isValid) {
            LOGGER.warning("Email from token does not match doctor's email. Token email: " +
                    emailFromToken + ", Doctor ID: " + doctorId);
        }

        return isValid;
    }
    @Override
    public boolean validateAdminToken(String token) {
        return JwtTokenFilter.getRole(token) == Role.ADMIN;
    }


    @Override
    public boolean validatePatientOrDoctorToken(Long patientId, Long doctorId, String token) {
        String emailFromToken = getEmailFromToken(token);
        LOGGER.info("Email from token: " + emailFromToken);

        Patient patient = patientService.getPatientEntityById(patientId);
        if (patient == null) {
            throw new NotFoundException(PatientExceptionMessage.PATIENT_NOT_FOUND_WITH_ID + patientId);
        }

        Doctor doctor = doctorService.getDoctorEntityById(doctorId);
        if (doctor == null) {
            throw new NotFoundException(DoctorExceptionMessage.DOCTOR_NOT_FOUND);
        }

        boolean isValid = emailFromToken.equals(patient.getEmail()) || emailFromToken.equals(doctor.getEmail());

        if (!isValid) {
            LOGGER.warning("Email from token does not match either patient's or doctor's email. Token email: " + emailFromToken);
        }

        return isValid;
    }

    @Override
    public String getEmailFromToken(String token) {
        String emailFromToken = JwtTokenFilter.getEmail(token);

        if (emailFromToken == null) {
            LOGGER.severe("Invalid token or email not found in token");
            throw new NotAuthorizedException(PatientExceptionMessage.INVALID_TOKEN_OR_EMAIL_NOT_FOUND);
        }

        return emailFromToken;
    }
}