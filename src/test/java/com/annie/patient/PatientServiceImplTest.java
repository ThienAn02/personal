package com.annie.patient;

import com.annie.patient.constants.PatientExceptionMessage;
import com.annie.patient.dao.PatientDao;
import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.dto.PatientResponseDto;
import com.annie.patient.entity.Gender;
import com.annie.patient.entity.Patient;
import com.annie.patient.service.PatientMapper;
import com.annie.patient.service.PatientServiceImpl;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest {

    @Mock
    private PatientDao patientDao;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Spy
    private PatientMapper patientMapper;

    private PatientRequestDto patientRequestDTO;
    private Patient patient;
    private PatientResponseDto patientResponseDTO;

    @BeforeEach
    void setUp() {
        patientRequestDTO = new PatientRequestDto();
        patientRequestDTO.setName("John Doe");
        patientRequestDTO.setAddress("123 Main St");
        patientRequestDTO.setAge(30);
        patientRequestDTO.setGender(Gender.MALE);
        patientRequestDTO.setPhone("1234567890");

        patient = new Patient(1L, "John Doe", "123 Main St", 30, Gender.MALE, "1234567890");

        patientResponseDTO = new PatientResponseDto();
        patientResponseDTO.setId(1L);
        patientResponseDTO.setName("John Doe");
        patientResponseDTO.setAge(30);
    }

    @Test
    void getPatientById_PatientExists_ReturnsPatient() {
        Optional<Patient> optionalPatient = Optional.of(patient);
        when(patientDao.findById(patient.getId())).thenReturn(optionalPatient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(patientResponseDTO);

        PatientResponseDto result = patientService.getPatientById(patient.getId());

        assertEquals(patientResponseDTO, result);
        verify(patientDao, times(1)).findById(patient.getId());
        verify(patientMapper, times(1)).toResponseDTO(patient);
    }

    @Test
    void getPatientById_PatientNotFound_ThrowsBadRequestException() {
        when(patientDao.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> patientService.getPatientById(1L));

        assertEquals(PatientExceptionMessage.PATIENT_NOT_FOUND, exception.getMessage());
        verify(patientDao, times(1)).findById(1L);
        verify(patientMapper, never()).toResponseDTO(any());
    }

    @Test
    void getAllPatients_ReturnsAllPatients() {
        when(patientDao.findAll()).thenReturn(Collections.singletonList(patient));
        when(patientMapper.toResponseDTOList(any())).thenReturn(Collections.singletonList(patientResponseDTO));

        List<PatientResponseDto> response = patientService.getAllPatients();

        assertEquals(1, response.size());
        assertEquals("John Doe", response.get(0).getName());
        verify(patientDao, times(1)).findAll();
    }
    @Test
    void addPatient_ValidRequest_AddsPatient() {

        when(patientMapper.toEntity(patientRequestDTO)).thenReturn(patient);
        when(patientDao.save(patient)).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(patientResponseDTO);

        PatientResponseDto response = patientService.addPatient(patientRequestDTO);

        assertEquals(patientRequestDTO.getName(), response.getName());
        assertEquals(patientRequestDTO.getAge(), response.getAge());
        verify(patientDao, times(1)).save(patient);
    }


    @Test
    void updatePatient_PatientNotFound_ThrowsBadRequestException() {
        Optional<Patient> optionalPatient = Optional.empty();
        when(patientDao.findById(1L)).thenReturn(optionalPatient);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> patientService.updatePatient(1L, patientRequestDTO));

        assertEquals(PatientExceptionMessage.PATIENT_NOT_FOUND, exception.getMessage());
        verify(patientDao, times(1)).findById(anyLong());
        verify(patientDao, times(0)).update(any(Patient.class));
    }

    @Test
    void deletePatient_ValidRequest_DeletesPatient() {
        Optional<Patient> optionalPatient = Optional.of(patient);
        when(patientDao.findById(1L)).thenReturn(optionalPatient);
        doNothing().when(patientDao).delete(patient.getId());

        patientService.deletePatient(1L);

        verify(patientDao, times(1)).findById(1L);
        verify(patientDao, times(1)).delete(patient.getId());
    }

    @Test
    void deletePatient_PatientNotFound_ThrowsBadRequestException() {
        Optional<Patient> optionalPatient = Optional.empty();
        when(patientDao.findById(1L)).thenReturn(optionalPatient);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> patientService.deletePatient(1L));

        assertEquals(PatientExceptionMessage.PATIENT_NOT_FOUND, exception.getMessage());
        verify(patientDao, times(1)).findById(1L);
        verify(patientDao, times(0)).delete(anyLong());
    }
}
