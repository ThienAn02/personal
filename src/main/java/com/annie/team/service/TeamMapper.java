package com.annie.team.service;

import com.annie.team.dto.request.TeamRequestDTO;
import com.annie.team.dto.response.TeamResponseDTO;
import com.annie.team.entity.Team;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface TeamMapper {
    Team toEntity(TeamRequestDTO teamRequestDTO);

    TeamResponseDTO toResponseDTO(Team team);

    List<TeamResponseDTO> toResponseDTOList(List<Team> teams);
}