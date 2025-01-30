package com.annie.team.service.impl;

import com.annie.team.constants.TeamExceptionMessage;
import com.annie.team.dao.TeamDAO;
import com.annie.team.dto.request.TeamRequestDTO;
import com.annie.team.dto.response.TeamResponseDTO;
import com.annie.team.entity.Team;
import com.annie.team.service.TeamMapper;
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
public class TeamServiceImplTest {

    @Mock
    private TeamDAO teamDao;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Spy
    private TeamMapper teamMapper;

    private TeamRequestDTO teamRequestDTO;
    private Team team;
    private TeamResponseDTO teamResponseDTO;

    @BeforeEach
    void setUp() {
        teamRequestDTO = new TeamRequestDTO();
        teamRequestDTO.setName("Test Team");

        team = new Team();
        team.setId(1L);
        team.setName("Test Team");

        teamResponseDTO = new TeamResponseDTO();
        teamResponseDTO.setId(1L);
        teamResponseDTO.setName("Test Team");
    }

    @Test
    void getTeamById_TeamExists_ReturnsTeam() {
        Optional<Team> optionalTeam = Optional.of(team);
        when(teamDao.findById(team.getId())).thenReturn(optionalTeam);
        when(teamMapper.toResponseDTO(team)).thenReturn(teamResponseDTO);

        TeamResponseDTO result = teamService.getTeamById(team.getId());

        assertEquals(teamResponseDTO, result);
        verify(teamDao, times(1)).findById(team.getId());
        verify(teamMapper, times(1)).toResponseDTO(team);
    }

    @Test
    void getTeamById_TeamNotFound_ThrowsBadRequestException() {
        when(teamDao.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> teamService.getTeamById(1L));

        assertEquals(TeamExceptionMessage.TEAM_NOT_FOUND, exception.getMessage());
        verify(teamDao, times(1)).findById(1L);
        verify(teamMapper, never()).toResponseDTO(any());
    }

    @Test
    void getAllTeams_ReturnsAllTeams() {
        when(teamDao.findAll()).thenReturn(Collections.singletonList(team));
        when(teamMapper.toResponseDTOList(any())).thenReturn(Collections.singletonList(teamResponseDTO));

        List<TeamResponseDTO> response = teamService.getAllTeams();

        assertEquals(1, response.size());
        assertEquals("Test Team", response.get(0).getName());
        verify(teamDao, times(1)).findAll();
    }

    @Test
    void addTeam_ValidRequest_AddsTeam() {
        when(teamDao.findByName(teamRequestDTO.getName())).thenReturn(Optional.empty());
        when(teamMapper.toEntity(teamRequestDTO)).thenReturn(team);
        when(teamDao.save(team)).thenReturn(team);
        when(teamMapper.toResponseDTO(team)).thenReturn(teamResponseDTO);

        TeamResponseDTO response = teamService.addTeam(teamRequestDTO);

        assertEquals(teamRequestDTO.getName(), response.getName());
        verify(teamDao, times(1)).findByName(teamRequestDTO.getName());
        verify(teamDao, times(1)).save(team);
    }

    @Test
    void addTeam_TeamNameExists_ThrowsIllegalArgumentException() {
        Optional<Team> optionalTeam = Optional.of(team);
        when(teamDao.findByName(teamRequestDTO.getName())).thenReturn(optionalTeam);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teamService.addTeam(teamRequestDTO));

        assertEquals(TeamExceptionMessage.TEAM_NAME_EXISTED, exception.getMessage());
        verify(teamDao, times(1)).findByName(teamRequestDTO.getName());
        verify(teamDao, times(0)).save(any(Team.class));
    }

    @Test
    void updateTeam_ValidRequest_UpdatesTeam() {
        teamRequestDTO.setName("Test Team 2");
        teamResponseDTO.setName("Test Team 2");
        Team updatedTeam = new Team(1L, "Test Team 2");
        Optional<Team> optionalTeam = Optional.of(team);
        when(teamDao.findById(team.getId())).thenReturn(optionalTeam);
        when(teamDao.findByName(teamRequestDTO.getName())).thenReturn(Optional.empty());
        when(teamMapper.toEntity(teamRequestDTO)).thenReturn(team);
        when(teamDao.update(team)).thenReturn(updatedTeam);
        when(teamMapper.toResponseDTO(updatedTeam)).thenReturn(teamResponseDTO);

        TeamResponseDTO response = teamService.updateTeam(1L, teamRequestDTO);

        assertEquals(team.getId(), response.getId());
        assertEquals("Test Team 2", response.getName());
        verify(teamDao, times(1)).findByName(teamResponseDTO.getName());
        verify(teamDao, times(1)).update(team);
    }

    @Test
    void updateTeam_DuplicateTeamName_ThrowsIllegalArgumentException() {
        Optional<Team> optionalTeam = Optional.of(team);
        when(teamDao.findById(team.getId())).thenReturn(optionalTeam);
        when(teamDao.findByName(teamRequestDTO.getName())).thenReturn(optionalTeam);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teamService.updateTeam(1L, teamRequestDTO));

        assertEquals(TeamExceptionMessage.TEAM_NAME_DUPLICATED, exception.getMessage());
        verify(teamDao, times(1)).findByName(teamRequestDTO.getName());
        verify(teamDao, times(0)).update(any(Team.class));
    }

    @Test
    void updateTeam_TeamNameExists_ThrowsIllegalArgumentException() {
        Optional<Team> optionalTeam1 = Optional.of(team);
        when(teamDao.findById(1L)).thenReturn(optionalTeam1);
        team.setId(2L);
        Optional<Team> optionalTeam2 = Optional.of(team);
        when(teamDao.findByName(teamRequestDTO.getName())).thenReturn(optionalTeam2);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> teamService.updateTeam(1L, teamRequestDTO));

        assertEquals(TeamExceptionMessage.TEAM_NAME_EXISTED, exception.getMessage());
        verify(teamDao, times(1)).findByName(teamRequestDTO.getName());
        verify(teamDao, times(0)).update(any(Team.class));
    }

    @Test
    void updateTeam_TeamNotFound_ThrowsBadRequestException() {
        Optional<Team> optionalTeam = Optional.empty();
        when(teamDao.findById(1L)).thenReturn(optionalTeam);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> teamService.updateTeam(1L, teamRequestDTO));

        assertEquals(TeamExceptionMessage.TEAM_NOT_FOUND, exception.getMessage());
        verify(teamDao, times(1)).findById(anyLong());
        verify(teamDao, times(0)).update(any(Team.class));
    }

    @Test
    void deleteTeam_ValidRequest_DeletesTeam() {
        Optional<Team> optionalTeam = Optional.of(team);
        when(teamDao.findById(1L)).thenReturn(optionalTeam);
        doNothing().when(teamDao).delete(team.getId());

        teamService.deleteTeam(1L);

        verify(teamDao, times(1)).findById(1L);
        verify(teamDao, times(1)).delete(team.getId());
    }

    @Test
    void deleteTeam_TeamNotFound_ThrowsBadRequestException() {
        Optional<Team> optionalTeam = Optional.empty();
        when(teamDao.findById(1L)).thenReturn(optionalTeam);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> teamService.deleteTeam(1L));

        assertEquals(TeamExceptionMessage.TEAM_NOT_FOUND, exception.getMessage());
        verify(teamDao, times(1)).findById(1L);
        verify(teamDao, times(0)).delete(anyLong());
    }
}
