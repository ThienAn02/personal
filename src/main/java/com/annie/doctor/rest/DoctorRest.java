package com.annie.doctor.rest;

import com.annie.appointment.dto.AppointmentResponseDto;
import com.annie.appointment.service.AppointmentService;
import com.annie.base.configuration.JwtTokenFilter;
import com.annie.doctor.dto.DoctorRequestDto;
import com.annie.doctor.dto.DoctorResponseDto;
import com.annie.doctor.service.DoctorService;
import com.annie.base.response.ResponseModel;
import com.annie.patient.service.PatientService;
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
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("/doctors")
@Tag(name = "Doctor", description = "Operations related to doctors")
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoctorRest {

    @Inject
    private DoctorService doctorService;

    @Inject
    private AppointmentService appointmentService;

    @Inject
    private PatientService patientService;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all doctors", description = "Retrieve a list of all doctors")
    public Response getAllDoctors() {
        return Response.ok().entity(ResponseModel.builder()
                .data(doctorService.getAllDoctors())
                .build()).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get doctor by ID", description = "Retrieve doctor details by ID")
    public Response getDoctorById(@PathParam("id") Long id) {
        var doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Doctor not found")
                    .build();
        }
        return Response.ok().entity(ResponseModel.builder()
                .data(doctor)
                .build()).build();
    }

    @POST
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Add a new doctor", description = "Add a new doctor with the provided details")
    public Response addDoctor(@Valid DoctorRequestDto doctorRequest) {
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                .data(doctorService.addDoctor(doctorRequest))
                .build()).build();
    }

    @PUT
    @Path("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Update a doctor", description = "Update doctor details by ID")
    public Response updateDoctor(@PathParam("id") Long id, @Valid DoctorRequestDto doctorRequestDto) {
        return Response.ok().entity(ResponseModel.builder()
                .data(doctorService.updateDoctor(id, doctorRequestDto))
                .build()).build();
    }

    @DELETE
    @Path("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Delete a doctor", description = "Delete a doctor by ID")
    public Response deleteDoctor(@PathParam("id") Long id) {
        doctorService.deleteDoctor(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{doctorId}/available-slots")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get available slots", description = "Retrieve available slots for a doctor on a specific date")
    public Response getAvailableSlots(@PathParam("doctorId") Long doctorId,
                                      @QueryParam("date") String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate date = LocalDate.parse(dateStr, formatter);
            List<LocalDateTime> availableSlots = doctorService.getAvailableSlots(doctorId, date);
            return Response.ok(availableSlots).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date format. Please use yyyy-MM-dd.")
                    .build();
        }
    }

    @GET
    @Path("/status/{checkstatus}")
    @Operation(summary = "Get doctors by status", description = "Retrieve a list of doctors based on their active status")
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed("DOCTOR")
    public Response getDoctorsByStatus(@PathParam("checkstatus") boolean isDeleted) {
        try {
            List<DoctorResponseDto> doctors = doctorService.getDoctorsByStatus(isDeleted);
            return Response.ok(doctors).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Doctors not found!")
                    .build();
        }
    }
    @GET
    @Path("/by-specialty")
    @Operation(summary = "Get doctors by status", description = "Retrieve a list of doctors based on their active status")
    public Response getDoctorsBySpecialty(@QueryParam("specialty") String specialtyName) {
        List<DoctorResponseDto> doctors = doctorService.getDoctorsBySpecialty(specialtyName);
        return Response.ok(doctors).build();
    }

    @GET
    @Path("/{doctorId}/appointments")

    public Response getDoctorAppointments(
            @PathParam("doctorId") Long doctorId,
            @QueryParam("date") String date,
            @QueryParam("week") String week,
            @QueryParam("month") String month) {

        List<AppointmentResponseDto> appointments = appointmentService.getDoctorAppointments(doctorId, date, week, month);
        return Response.ok(appointments).build();
    }

    @GET
    @Path("/{patientId}/history")
    @RolesAllowed({"DOCTOR"})
    @Operation(summary = "Get patient's medical history",
            description = "Allows doctors to view a patient's medical history if the patient has an appointment with the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient history retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Doctor not authorized to view this patient's history"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    public Response getPatientHistory(
            @PathParam("patientId") Long patientId,
            @Context HttpServletRequest request) {

            String token = JwtTokenFilter.getCurrentToken(request);
                return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                        .data(patientService.getPatientHistoryForDoctor(patientId,token))
                        .build()).build();

    }


    @POST
    @Path("/doctors/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Get patient's medical history",
            description = "Allows doctors to view a patient's medical history if the patient has an appointment with the doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient history retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Doctor not authorized to view this patient's history"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")

    public Response importDoctors(@MultipartForm MultipartFormDataInput input) {
        List<DoctorResponseDto> importedDoctors = doctorService.importDoctorsFromExcel(input);
        return Response.ok(importedDoctors).build();
    }

}
