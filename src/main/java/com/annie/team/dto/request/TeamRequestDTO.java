package com.annie.team.dto.request;

import com.annie.team.constants.TeamExceptionMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamRequestDTO {

    @NotBlank(message = TeamExceptionMessage.TEAM_NAME_MUST_FIELD)
    private String name;

}
