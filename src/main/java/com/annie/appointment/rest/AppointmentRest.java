package com.annie.appointment.rest;

import com.annie.appointment.dto.AppointmentRequestDto;
import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.appointment.dto.AppointmentStatusUpdateDto;
import com.annie.appointment.service.AppointmentService;
import com.annie.base.common.Status;
import com.annie.base.configuration.JwtTokenFilter;
import com.annie.base.response.ResponseModel;
import com.annie.patient.dto.PatientResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Path("/appointments")
@Tag(name = "Appointment", description = "Operations related to appointments")
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class AppointmentRest {

    @Inject
    private AppointmentService appointmentService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create an appointment", description = "Creates a new appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Appointment created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not allowed to create appointment for this patient/doctor"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed({"DOCTOR","PATIENT"})
    public Response createAppointment(
            @Valid AppointmentRequestDto request,
            @Context HttpServletRequest httpRequest) {

        String token = JwtTokenFilter.getCurrentToken(httpRequest);
        AppointmentResponseDto appointment = appointmentService.createAppointment(request, token);

        return Response.status(Response.Status.CREATED)
                .entity(new ResponseModel<>("Appointment created successfully", appointment))
                .build();
    }

    @GET
    @Path("/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get appointments by patient ID", description = "Retrieve a list of appointments for a specific patient")
    @RolesAllowed({"DOCTOR","PATIENT"})
    public Response getAppointmentsByPatientId(@PathParam("patientId") Long patientId) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        return Response.ok(new ResponseModel<>("Appointments retrieved successfully", appointments)).build();
    }

    @GET
    @Path("/doctors/{doctorId}")
    @Produces(MediaType.APPLICATION_JSON)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get appointments ok by doctor", description = "Retrieve a list of appointments for a specific doctor, filtered by status and/or date")
    public Response getAppointmentsByDoctor(
            @PathParam("doctorId") Long doctorId,
            @QueryParam("status") Status status,
            @QueryParam("date") String dateStr,
            @Context HttpServletRequest httpRequest
    ) {
        String token = JwtTokenFilter.getCurrentToken(httpRequest);
        LocalDate date = (dateStr != null && !dateStr.isEmpty()) ? LocalDate.parse(dateStr) : null;

        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDoctor(doctorId, status, date, token);
        return Response.ok(new ResponseModel<>("Appointments retrieved successfully", appointments)).build();
    }

    @PUT
    @Path("/{appointmentId}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update appointment status", description = "Updates the status of an appointment")
    public Response updateAppointmentStatus(
            @PathParam("appointmentId") Long appointmentId,
            AppointmentStatusUpdateDto statusUpdateDto) {

        AppointmentResponseDto updatedAppointment = appointmentService.updateAppointmentStatus(appointmentId, statusUpdateDto.getStatus());
        return Response.ok(new ResponseModel<>("Appointment status updated successfully", updatedAppointment)).build();
    }

    @GET
    @Path("/count-by-doctor")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get appointment count by doctor", description = "Retrieve the count of appointments for each doctor")
    public Response getAppointmentsCountByDoctor() {
        Map<String, Long> appointmentsCount = appointmentService.getAppointmentsCountByDoctor();
        return Response.ok(new ResponseModel<>("Appointment counts retrieved successfully", appointmentsCount)).build();
    }

    @GET
    @Path("/patients")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get patients by year and medicine", description = "Retrieve patients who have appointments in a specific year and have taken a specific medicine")
    public Response getPatientsByYearAndMedicine(@QueryParam("year") int year, @QueryParam("medicine") String medicineName) {
        List<PatientResponseDto> patients = appointmentService.getPatientsByYearAndMedicine(year, medicineName);
        return Response.ok(new ResponseModel<>("Patients retrieved successfully", patients)).build();
    }
}
