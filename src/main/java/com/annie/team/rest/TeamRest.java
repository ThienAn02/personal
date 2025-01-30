package com.annie.team.rest;

import com.annie.response.ResponseModel;
import com.annie.team.dto.request.TeamRequestDTO;
import com.annie.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("teams")
@Tag(name = "Team", description = "Operations related to teams")
public class TeamRest {

    @Inject
    private TeamService teamService;

    @GET
    @Produces("application/json")
    @Operation(
            summary = "Get all teams",
            description = "Fetches a list of all teams in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of teams fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getAllTeams() {
        return Response.ok().entity(ResponseModel.builder()
                .code(200)
                .data(teamService.getAllTeams())
                .build()).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Operation(
            summary = "Get a team by id",
            description = "Fetches a team in the system by id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of teams fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getTeamById(@PathParam("id") Long id) {
        return Response.ok().entity(ResponseModel.builder()
                .code(200)
                .data(teamService.getTeamById(id))
                .build()).build();
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(
            summary = "Add team",
            description = "Add team"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Add team successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response addTeam(@Valid TeamRequestDTO teamRequest) {
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                .code(201)
                .data(teamService.addTeam(teamRequest))
                .build()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(
            summary = "Update team",
            description = "Update team."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update team successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response updateTeam(@PathParam("id") Long id, @Valid TeamRequestDTO teamRequest) {
        return Response.ok().entity(ResponseModel.builder()
                .code(200)
                .data(teamService.updateTeam(id, teamRequest))
                .build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    @Operation(
            summary = "Delete team",
            description = "Fetches a list of all teams in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delete team"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response deleteTeam(@PathParam("id") Long id) {
        teamService.deleteTeam(id);
        return Response.noContent().build();
    }
}