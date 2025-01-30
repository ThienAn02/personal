package com.annie.team.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeamExceptionMessage {

    public static final String TEAM_NOT_FOUND = "Team not found";
    public static final String TEAM_NAME_EXISTED = "Team name already exists";
    public static final String TEAM_NAME_MUST_FIELD = "You must enter a name";
    public static final String TEAM_NAME_DUPLICATED = "It is already your team name";
}
