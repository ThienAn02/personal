package com.annie.patient;

import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.dto.PatientResponseDto;
import com.annie.patient.rest.PatientRest;
import com.annie.patient.service.PatientService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientRestTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientRest patientRest;

    private PatientRequestDto patientRequestDto;
    private PatientResponseDto patientResponseDto;

    @BeforeEach
    void setUp() {
        patientRequestDto = new PatientRequestDto();
        patientRequestDto.setName("John Doe");
        patientRequestDto.setAge(30);
        patientRequestDto.setPhone("123456789");

        patientResponseDto = new PatientResponseDto();
        patientResponseDto.setId(1L);
        patientResponseDto.setName("John Doe");
    }

    @Test
    void getAllPatients_ReturnsListOfPatients() {
        when(patientService.getAllPatients()).thenReturn(List.of(patientResponseDto));

        Response response = patientRest.getAllPatients();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(patientService, times(1)).getAllPatients();
    }

    @Test
    void getPatientById_ExistingId_ReturnsPatient() {
        when(patientService.getPatientById(1L)).thenReturn(patientResponseDto);

        Response response = patientRest.getPatientById(1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(patientService, times(1)).getPatientById(1L);
    }

    @Test
    void addPatient_ValidRequest_AddsPatient() {
        when(patientService.addPatient(any(PatientRequestDto.class))).thenReturn(patientResponseDto);

        Response response = patientRest.addPatient(patientRequestDto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(patientService, times(1)).addPatient(any(PatientRequestDto.class));
    }

    @Test
    void updatePatient_ValidRequest_UpdatesPatient() {
        when(patientService.updatePatient(eq(1L), any(PatientRequestDto.class))).thenReturn(patientResponseDto);

        Response response = patientRest.updatePatient(1L, patientRequestDto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(patientService, times(1)).updatePatient(eq(1L), any(PatientRequestDto.class));
    }

    @Test
    void deletePatient_Success() {
        doNothing().when(patientService).deletePatient(1L);

        Response response = patientRest.deletePatient(1L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(patientService, times(1)).deletePatient(1L);
    }

    @Nested
    class AddPatientValidationTests {

        @Test
        void addPatient_NameIsNull_ThrowsBadRequestException() {
            patientRequestDto.setName(null);

            try {
                patientRest.addPatient(patientRequestDto);
                fail("Expected BadRequestException to be thrown");
            } catch (BadRequestException e) {
                assertEquals("Patient name must not be null", e.getMessage());
            }
            verify(patientService, times(0)).addPatient(any(PatientRequestDto.class));
        }


        @Test
        void addPatient_NameIsEmpty_ThrowsBadRequestException() {
            patientRequestDto.setName("");

            BadRequestException exception = assertThrows(BadRequestException.class, () -> patientRest.addPatient(patientRequestDto));
            assertEquals("Patient name must not be empty", exception.getMessage());
            verify(patientService, times(0)).addPatient(any(PatientRequestDto.class));
        }

        @Test
        void addPatient_AgeIsNull_ThrowsBadRequestException() {
            patientRequestDto.setAge(null);

            BadRequestException exception = assertThrows(BadRequestException.class, () -> patientRest.addPatient(patientRequestDto));
            assertEquals("Patient age must not be null", exception.getMessage());
            verify(patientService, times(0)).addPatient(any(PatientRequestDto.class));
        }

        @Test
        void addPatient_PhoneIsNull_ThrowsBadRequestException() {
            patientRequestDto.setPhone(null);

            BadRequestException exception = assertThrows(BadRequestException.class, () -> patientRest.addPatient(patientRequestDto));
            assertEquals("Patient phone number must not be null or empty", exception.getMessage());
            verify(patientService, times(0)).addPatient(any(PatientRequestDto.class));
        }

        @Test
        void addPatient_PhoneIsEmpty_ThrowsBadRequestException() {
            patientRequestDto.setPhone("");

            BadRequestException exception = assertThrows(BadRequestException.class, () -> patientRest.addPatient(patientRequestDto));
            assertEquals("Patient phone number must not be null or empty", exception.getMessage());
            verify(patientService, times(0)).addPatient(any(PatientRequestDto.class));
        }


    }

    @Nested
    class UpdatePatientValidationTests {

        @Test
        void updatePatient_NameIsNull_ThrowsBadRequestException() {
            patientRequestDto.setName(null);

            BadRequestException exception = assertThrows(BadRequestException.class, () -> patientRest.updatePatient(1L, patientRequestDto));
            assertEquals("Patient name must not be null", exception.getMessage());
            verify(patientService, times(0)).updatePatient(eq(1L), any(PatientRequestDto.class));
        }

        @Test
        void updatePatient_NameIsEmpty_ThrowsBadRequestException() {
            patientRequestDto.setName("");

            // Assert that BadRequestException is thrown
            BadRequestException exception = assertThrows(BadRequestException.class, () -> patientRest.updatePatient(1L, patientRequestDto));
            assertEquals("Patient name must not be empty", exception.getMessage());

            // Verify no service update is called
            verify(patientService, times(0)).updatePatient(eq(1L), any(PatientRequestDto.class));
        }

    }
}
