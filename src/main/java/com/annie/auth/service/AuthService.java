package com.annie.auth.service;

import com.annie.account.dao.AccountDao;
import com.annie.account.dto.AccountDto;
import com.annie.account.entity.Account;
import com.annie.account.service.AccountMapper;
import com.annie.auth.dto.LoginRequestDto;
import com.annie.auth.dto.LoginResponseDto;
import com.annie.auth.dto.SignUpRequestDto;
import com.annie.base.common.Role;
import com.annie.base.exception.BadRequestException;
import com.annie.base.security.JwtGenerator;
import com.annie.base.security.JwtPayload;
import com.annie.doctor.entity.Doctor;
import com.annie.patient.dao.PatientDao;
import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.entity.Patient;
import com.annie.patient.service.PatientService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.annie.auth.contants.AuthExceptionMessage.*;
@Stateless
public class AuthService {

    private static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_\\-+={}\\]|:;\"'<,>.?/]).{8,}$";

    @Inject
    private AccountDao accountDao;

    @Inject
    private PatientService patientService;
    @Inject
    private PatientDao patientDao;

    @Inject
    private JwtGenerator jwtGenerator;

    @Inject
    private AccountMapper accountMapper;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Account account = accountDao.getByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new BadRequestException(INVALID_CREDENTIALS));

        if (!BCrypt.checkpw(loginRequestDto.getPassword(), account.getPassword())) {
            throw new BadRequestException(INVALID_CREDENTIALS);
        }

        return new LoginResponseDto(generateJWT(account), accountMapper.toDto(account));
    }

    private String generateJWT(Account account) {
        JwtPayload payload = new JwtPayload(account.getEmail(), Role.fromString(account.getRole()));
        return jwtGenerator.generateToken(payload.toMap());
    }
    public AccountDto register(SignUpRequestDto signUpRequestDto) {
        validateRegisterRequest(signUpRequestDto);

        PatientRequestDto patientRequest = PatientRequestDto.builder()
                .name(signUpRequestDto.getName())
                .email(signUpRequestDto.getEmail())
                .address(signUpRequestDto.getAddress())
                .phone(signUpRequestDto.getPhone())
                .healthNote(signUpRequestDto.getHealthNote())
                .emergencyContact(signUpRequestDto.getEmergencyContact())
                .bloodType(signUpRequestDto.getBloodType())
                .allergies(signUpRequestDto.getAllergies())
                .insuranceNumber(signUpRequestDto.getInsuranceNumber())
                .dob(signUpRequestDto.getDob())
                .gender(signUpRequestDto.getGender())
                .build();

        patientService.addPatient(patientRequest);

        Patient patient = patientDao.findByEmail(signUpRequestDto.getEmail())
                .orElseThrow(() -> new BadRequestException("Patient not found after creation"));

        Account account = Account.builder()
                .email(signUpRequestDto.getEmail())
                .password(BCrypt.hashpw(signUpRequestDto.getPassword(), BCrypt.gensalt(10)))
                .role(Role.PATIENT.name())
                .patient(patient)
                .build();

        return accountMapper.toDto(accountDao.save(account));
    }


    private void validateRegisterRequest(SignUpRequestDto request) {
        Optional<Account> existingAccount = accountDao.getByEmail(request.getEmail());
        if (existingAccount.isPresent()) {
            throw new BadRequestException(EMAIL_ALREADY_EXISTS);
        }

        if (!Pattern.matches(PASSWORD_VALIDATION_REGEX, request.getPassword())) {
            throw new BadRequestException(WEAK_PASSWORD);
        }
    }
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_PASSWORD = "asAS12!@sdSD";

    @PostConstruct
    public void createAdminAccountIfNotExists() {
        Optional<Account> existingAdmin = accountDao.getByEmail(ADMIN_EMAIL);

        if (existingAdmin.isEmpty()) {
            Account admin = new Account();
            admin.setEmail(ADMIN_EMAIL);
            admin.setPassword(BCrypt.hashpw(ADMIN_PASSWORD, BCrypt.gensalt(10))); // Hash password
            admin.setRole(String.valueOf(Role.ADMIN));

            accountDao.save(admin);
            System.out.println("Admin account created successfully!");
        } else {
            System.out.println("Admin account already exists.");
        }
    }

    public AccountDto createDoctorAccount(Doctor savedDoctor) {
        if (savedDoctor == null) {
            throw new IllegalArgumentException("Doctor entity cannot be null");
        }

        Account account = Account.builder()
                .email(savedDoctor.getEmail())
                .password(BCrypt.hashpw("defaultPassword", BCrypt.gensalt(10)))
                .role("DOCTOR")
                .doctor(savedDoctor)
                .build();

        Account savedAccount = accountDao.save(account);

        return AccountDto.builder()
                .id(savedAccount.getId())
                .email(savedAccount.getEmail())
                .build();
    }

}
