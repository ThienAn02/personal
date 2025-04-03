package com.annie.patient.rest;

import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.base.configuration.AppConfig;
import com.annie.base.configuration.JwtTokenFilter;
import com.annie.base.exception.IdNotFoundException;
import com.annie.base.exception.UnauthorizedException;
import com.annie.base.filter.AuthenticationFilter;
import com.annie.base.security.JwtPayload;
import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.service.PatientService;
import com.annie.base.response.ResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Patient", description = "Operations related to patient management")
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class PatientRest {

    @Inject
    private PatientService patientService;
    @Inject
    private AuthenticationFilter authenticationFilter;

    /**
     * Retrieves all patients.
     * @return Response containing list of patients
     */
    @GET
    @Operation(summary = "Get all patients", description = "Retrieve a list of all registered patients.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patients retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed({"DOCTOR","AMIN"})
    public Response getAllPatients() {
        return Response.ok().entity(ResponseModel.builder()
                .data(patientService.getAllPatients())
                .build()).build();
    }

    /**
     * Retrieves a patient by ID.
     * @param id Patient ID
     * @return Response containing patient details
     */
    @GET
    @Path("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieve a patient's details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed("DOCTOR")
    public Response getPatientById(@PathParam("id") Long id) {
        var patient = patientService.getPatientById(id);

        if (patient == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Patient not found")
                    .build();
        }
        return Response.ok().entity(ResponseModel.builder()
                .data(patient)
                .build()).build();
    }

    /**
     * Adds a new patient.
     * @param patientRequest Patient request DTO
     * @return Response containing created patient
     */
    @POST
    @Operation(summary = "Add new patient", description = "Create a new patient record.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed("ADMIN")
    public Response addPatient(@Valid PatientRequestDto patientRequest) {
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                .data(patientService.addPatient(patientRequest))
                .build()).build();
    }

    /**
     * Updates patient details.
     * @param id Patient ID
     * @param patientRequestDto Updated patient details
     * @return Response containing updated patient
     */
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update patient", description = "Update the details of an existing patient.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed({"ADMIN","PATIENT"})
    public Response updatePatient(@PathParam("id") Long id, @Valid PatientRequestDto patientRequestDto) {
        return Response.ok().entity(ResponseModel.builder()
                .data(patientService.updatePatient(id, patientRequestDto))
                .build()).build();
    }

    @GET
    @Path("/{patientId}/appointments")
    @Operation(summary = "Get patient's appointments", description = "Retrieve a patient's appointment list after verifying their email from the JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not allowed to access another patient's data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed({"PATIENT"})
    public Response getPatientAppointments(
            @PathParam("patientId") Long patientId,
            @Context HttpServletRequest request) {

        String token = JwtTokenFilter.getCurrentToken(request);
        log.warn("token" + token);

        return Response.ok().entity(ResponseModel.builder()
                .data(patientService.getAppointmentsWithEmailCheck(patientId,token))
                .build()).build();

    }
}
