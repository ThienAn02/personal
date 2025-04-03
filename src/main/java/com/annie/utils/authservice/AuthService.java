package com.annie.utils.authservice;

public interface AuthService {

    /**
     * Validates if the JWT token belongs to the specified patient
     *
     * @param patientId ID of the patient to validate against
     * @param token JWT token from the request
     * @return true if the token belongs to the patient, false otherwise
     * @throws UnauthorizedException if token is invalid or email not found
     */
    boolean validatePatientToken(Long patientId, String token);

    /**
     * Validates if the JWT token belongs to the specified doctor
     *
     * @param doctorId ID of the doctor to validate against
     * @param token JWT token from the request
     * @return true if the token belongs to the doctor, false otherwise
     * @throws UnauthorizedException if token is invalid or email not found
     */
    boolean validateDoctorToken(Long doctorId, String token);

    /**
     * Validates if the JWT token belongs to either the specified patient or doctor
     *
     * @param patientId ID of the patient to validate against
     * @param doctorId ID of the doctor to validate against
     * @param token JWT token from the request
     * @return true if the token belongs to either the patient or doctor, false otherwise
     * @throws UnauthorizedException if token is invalid or email not found
     */
    boolean validatePatientOrDoctorToken(Long patientId, Long doctorId, String token);

    /**
     * Extracts the email from the JWT token
     *
     * @param token JWT token from the request
     * @return email address extracted from the token
     * @throws UnauthorizedException if token is invalid or email not found
     */
    String getEmailFromToken(String token);
    boolean validateAdminToken(String token);

    }