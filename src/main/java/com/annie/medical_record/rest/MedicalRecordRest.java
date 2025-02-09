package com.annie.medical_record.rest;

import com.annie.medical_record.dto.MedicalRecordRequestDto;
import com.annie.medical_record.service.MedicalRecordService;
import com.annie.response.ResponseModel;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@Path("/medical-records")
public class MedicalRecordRest {

    @Inject
    private MedicalRecordService medicalRecordService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMedicalRecords() {
        return Response.ok().entity(ResponseModel.builder()
                .data(medicalRecordService.getAllMedicalRecords())
                .build()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicalRecordById(@PathParam("id") Long id) {
        var medicalRecord = medicalRecordService.getMedicalRecordById(id);
        if (medicalRecord == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Medical record not found")
                    .build();
        }
        return Response.ok().entity(ResponseModel.builder()
                .data(medicalRecord)
                .build()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMedicalRecord(@Valid MedicalRecordRequestDto medicalRecordRequest) {
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                .data(medicalRecordService.addMedicalRecord(medicalRecordRequest))
                .build()).build();
    }


    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMedicalRecord(@PathParam("id") Long id, @Valid MedicalRecordRequestDto medicalRecordRequest) {
        return Response.ok().entity(ResponseModel.builder()
                .data(medicalRecordService.updateMedicalRecord(id, medicalRecordRequest))
                .build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMedicalRecord(@PathParam("id") Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return Response.noContent().build();
    }
}
