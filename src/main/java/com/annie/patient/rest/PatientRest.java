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
                .code(200)
                .data(patientService.getAllPatients())
                .build()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPatient(@Valid PatientRequestDto patientRequest) {
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                .code(200)
                .data(patientService.addPatient(patientRequest))
                .build()).build();
    }
}