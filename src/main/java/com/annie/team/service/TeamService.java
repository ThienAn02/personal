package com.annie.team.service;

import com.annie.team.dto.request.TeamRequestDTO;
import com.annie.team.dto.response.TeamResponseDTO;

import java.util.List;

public interface TeamService {
    TeamResponseDTO getTeamById(Long id);
    List<TeamResponseDTO> getAllTeams();
    TeamResponseDTO addTeam(TeamRequestDTO teamRequest);
    TeamResponseDTO updateTeam(Long id, TeamRequestDTO teamRequest);
    void deleteTeam(Long id);
}
