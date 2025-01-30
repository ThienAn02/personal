package com.annie.team.rest;

import com.annie.response.ResponseModel;
import com.annie.team.constants.TeamExceptionMessage;
import com.annie.team.dto.request.TeamRequestDTO;
import com.annie.team.dto.response.TeamResponseDTO;
import com.annie.team.service.TeamService;
import jakarta.validation.*;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamRestTest {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamRest teamRest;

    private TeamRequestDTO teamRequestDTO;
    private TeamResponseDTO teamResponseDTO;
    private Validator validator;

    @BeforeEach
    void setUp() {
        teamRequestDTO = new TeamRequestDTO();
        teamRequestDTO.setName("Test Team");

        teamResponseDTO = new TeamResponseDTO();
        teamResponseDTO.setId(1L);
        teamResponseDTO.setName("Test Team");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private void assertConstraintViolation(TeamRequestDTO requestDTO, String expectedMessage) {
        Set<ConstraintViolation<TeamRequestDTO>> violations = validator.validate(requestDTO);
        assertFalse(violations.isEmpty());
        assertEquals(expectedMessage, violations.iterator().next().getMessage());
    }

    @Test
    void getTeamById_TeamExists_ReturnTeam() {
        when(teamService.getTeamById(1L)).thenReturn(teamResponseDTO);

        Response response = teamRest.getTeamById(1L);

        assertEquals(200, response.getStatus());
        ResponseModel<TeamResponseDTO> responseObject = (ResponseModel<TeamResponseDTO>) response.getEntity();
        assertEquals(200, responseObject.getCode());
        assertEquals(teamResponseDTO, responseObject.getData());
        verify(teamService, times(1)).getTeamById(1L);
    }

    @Test
    void getTeamById_TeamNotFound_ThrowsBadRequestException() {
        Long teamId = 1L;
        doThrow(new BadRequestException(TeamExceptionMessage.TEAM_NOT_FOUND)).when(teamService).getTeamById(teamId);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> teamRest.getTeamById(teamId));

        assertEquals(TeamExceptionMessage.TEAM_NOT_FOUND, exception.getMessage());
        verify(teamService, times(1)).getTeamById(teamId);
    }

    @Test
    void getAllTeams_ValidRequest_ReturnsAllTeams() {
        List<TeamResponseDTO> teams = Collections.singletonList(teamResponseDTO);
        when(teamService.getAllTeams()).thenReturn(teams);

        Response response = teamRest.getAllTeams();

        assertEquals(200, response.getStatus());
        ResponseModel<List<TeamResponseDTO>> responseObject = (ResponseModel<List<TeamResponseDTO>>) response.getEntity();
        assertEquals(200, responseObject.getCode());
        assertEquals(teams, responseObject.getData());
        verify(teamService, times(1)).getAllTeams();
    }

    @Test
    void addTeam_ValidRequest_AddsTeam() {
        when(teamService.addTeam(any(TeamRequestDTO.class))).thenReturn(teamResponseDTO);

        Response response = teamRest.addTeam(teamRequestDTO);

        assertEquals(201, response.getStatus());
        ResponseModel<TeamResponseDTO> responseObject = (ResponseModel<TeamResponseDTO>) response.getEntity();
        assertEquals(201, responseObject.getCode());
        assertEquals(teamResponseDTO, responseObject.getData());
        verify(teamService, times(1)).addTeam(any(TeamRequestDTO.class));
    }

    @Test
    void addTeam_TeamNameIsNull_ThrowsConstraintViolationException() {
        teamRequestDTO.setName(null);

        assertConstraintViolation(teamRequestDTO, TeamExceptionMessage.TEAM_NAME_MUST_FIELD);
        verify(teamService, times(0)).addTeam(any(TeamRequestDTO.class));
    }

    @Test
    void addTeam_TeamNameIsEmpty_ThrowsConstraintViolationException() {
        teamRequestDTO.setName("");

        assertConstraintViolation(teamRequestDTO, TeamExceptionMessage.TEAM_NAME_MUST_FIELD);
        verify(teamService, times(0)).addTeam(any(TeamRequestDTO.class));
    }

    @Test
    void addTeam_TeamNameExists_ThrowsBadRequestException() {
        doThrow(new IllegalArgumentException(TeamExceptionMessage.TEAM_NAME_EXISTED)).when(teamService).addTeam(any(TeamRequestDTO.class));

        Executable executable = () -> teamRest.addTeam(teamRequestDTO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(TeamExceptionMessage.TEAM_NAME_EXISTED, exception.getMessage());
        verify(teamService, times(1)).addTeam(any(TeamRequestDTO.class));
    }

    @Test
    void updateTeam_ValidRequest_UpdatesTeam() {
        when(teamService.updateTeam(anyLong(), any(TeamRequestDTO.class))).thenReturn(teamResponseDTO);

        Response response = teamRest.updateTeam(1L, teamRequestDTO);

        assertEquals(200, response.getStatus());
        ResponseModel<TeamResponseDTO> responseObject = (ResponseModel<TeamResponseDTO>) response.getEntity();
        assertEquals(teamResponseDTO, responseObject.getData());
        verify(teamService, times(1)).updateTeam(anyLong(), any(TeamRequestDTO.class));
    }

    @Test
    void updateTeam_TeamNameIsNull_ThrowsConstraintViolationException() {
        teamRequestDTO.setName(null);

        assertConstraintViolation(teamRequestDTO, TeamExceptionMessage.TEAM_NAME_MUST_FIELD);
        verify(teamService, times(0)).updateTeam(anyLong(), any(TeamRequestDTO.class));
    }

    @Test
    void updateTeam_TeamNameIsEmpty_ThrowsConstraintViolationException() {
        teamRequestDTO.setName("");

        assertConstraintViolation(teamRequestDTO, TeamExceptionMessage.TEAM_NAME_MUST_FIELD);
        verify(teamService, times(0)).updateTeam(anyLong(), any(TeamRequestDTO.class));
    }

    @Test
    void updateTeam_TeamNotFound_ThrowsBadRequestException() {
        doThrow(new BadRequestException(TeamExceptionMessage.TEAM_NOT_FOUND)).when(teamService).updateTeam(anyLong(), any(TeamRequestDTO.class));

        Executable executable = () -> teamRest.updateTeam(1L, teamRequestDTO);

        BadRequestException exception = assertThrows(BadRequestException.class, executable);
        assertEquals(TeamExceptionMessage.TEAM_NOT_FOUND, exception.getMessage());
        verify(teamService, times(1)).updateTeam(anyLong(), any(TeamRequestDTO.class));
    }

    @Test
    void deleteTeam_ValidRequest_DeletesTeam() {
        doNothing().when(teamService).deleteTeam(anyLong());

        Response response = teamRest.deleteTeam(1L);

        assertEquals(204, response.getStatus());
        verify(teamService, times(1)).deleteTeam(1L);
    }

    @Test
    void deleteTeam_TeamNotFound_ThrowsBadRequestException() {
        doThrow(new BadRequestException(TeamExceptionMessage.TEAM_NOT_FOUND)).when(teamService).deleteTeam(1L);

        Executable executable = () -> teamRest.deleteTeam(1L);

        BadRequestException exception = assertThrows(BadRequestException.class, executable);
        assertEquals(TeamExceptionMessage.TEAM_NOT_FOUND, exception.getMessage());
        verify(teamService, times(1)).deleteTeam(1L);
    }
}