package com.annie.team.service.impl;

import com.annie.team.constants.TeamExceptionMessage;
import com.annie.team.dao.TeamDAO;
import com.annie.team.service.TeamMapper;
import com.annie.team.dto.request.TeamRequestDTO;
import com.annie.team.dto.response.TeamResponseDTO;
import com.annie.team.entity.Team;
import com.annie.team.service.TeamService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;
import java.util.Optional;

@Stateless
public class TeamServiceImpl implements TeamService {

    @Inject
    private TeamDAO teamDAO;

    @Inject
    private TeamMapper teamMapper;

    @Override
    public TeamResponseDTO getTeamById(Long id) {
        Optional<Team> teamById = teamDAO.findById(id);
        return teamById.map(team -> teamMapper.toResponseDTO(team))
                .orElseThrow(() -> new BadRequestException(TeamExceptionMessage.TEAM_NOT_FOUND));
    }

    @Override
    public List<TeamResponseDTO> getAllTeams() {
        List<Team> teams = teamDAO.findAll();
        return teamMapper.toResponseDTOList(teams);
    }

    @Override
    public TeamResponseDTO addTeam(TeamRequestDTO teamRequest) {
        boolean isExisted = teamDAO.findByName(teamRequest.getName()).isPresent();
        if (isExisted) {
            throw new IllegalArgumentException(TeamExceptionMessage.TEAM_NAME_EXISTED);
        }
        Team team = teamMapper.toEntity(teamRequest);
        Team addedTeam = teamDAO.save(team);
        return teamMapper.toResponseDTO(addedTeam);
    }

    @Override
    public TeamResponseDTO updateTeam(Long id, TeamRequestDTO teamRequest) {
        boolean isExistedById = teamDAO.findById(id).isPresent();
        if (!isExistedById) {
            throw new BadRequestException(TeamExceptionMessage.TEAM_NOT_FOUND);
        }

        Optional<Team> existedTeamByName = teamDAO.findByName(teamRequest.getName());
        if (existedTeamByName.isPresent()) {
            if (existedTeamByName.get().getId().equals(id)) {
                throw new IllegalArgumentException(TeamExceptionMessage.TEAM_NAME_DUPLICATED);
            } else {
                throw new IllegalArgumentException(TeamExceptionMessage.TEAM_NAME_EXISTED);
            }
        }

        Team team = teamMapper.toEntity(teamRequest);
        team.setId(id);
        Team updatedTeam = teamDAO.update(team);
        return teamMapper.toResponseDTO(updatedTeam);
    }

    @Override
    public void deleteTeam(Long id) {
        boolean isExisted = teamDAO.findById(id).isPresent();
        if (!isExisted) {
            throw new BadRequestException(TeamExceptionMessage.TEAM_NOT_FOUND);
        }
        teamDAO.delete(id);
    }
}