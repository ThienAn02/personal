package com.annie.patient.rest;

import com.annie.patient.dto.PatientRequestDto;
import com.annie.patient.service.PatientService;
import com.annie.response.ResponseModel;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@Path("/patients")
public class PatientRest {

    @Inject
    private PatientService patientService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPatients() {
        return Response.ok().entity(ResponseModel.builder()
                .data(patientService.getAllPatients())
                .build()).build();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPatient(@Valid PatientRequestDto patientRequest) {
        if (patientRequest.getName() == null) {
            throw new BadRequestException("Patient name must not be null");
        }
        if (patientRequest.getName().trim().isEmpty()) {
            throw new BadRequestException("Patient name must not be empty");
        }
        if (patientRequest.getAge() == null) {
            throw new BadRequestException("Patient age must not be null");
        }
        if (patientRequest.getPhone() == null || patientRequest.getPhone().trim().isEmpty()) {
            throw new BadRequestException("Patient phone number must not be null or empty");
        }

        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                .data(patientService.addPatient(patientRequest))
                .build()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePatient(@PathParam("id") Long id, @Valid PatientRequestDto patientRequestDto) {
        if (patientRequestDto.getName() == null) {
            throw new BadRequestException("Patient name must not be null");
        }
        if (patientRequestDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Patient name must not be empty");
        }
        if (patientRequestDto.getAge() == null) {
            throw new BadRequestException("Patient age must not be null");
        }
        if (patientRequestDto.getPhone() == null || patientRequestDto.getPhone().trim().isEmpty()) {
            throw new BadRequestException("Patient phone number must not be null or empty");
        }

        return Response.ok().entity(ResponseModel.builder()
                .data(patientService.updatePatient(id, patientRequestDto))
                .build()).build();
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePatient(@PathParam("id") Long id) {
        patientService.deletePatient(id);
        return Response.noContent().build();
    }
}