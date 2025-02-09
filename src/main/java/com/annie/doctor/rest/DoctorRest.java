package com.annie.doctor.rest;

import com.annie.doctor.dto.DoctorRequestDto;
import com.annie.doctor.service.DoctorService;
import com.annie.response.ResponseModel;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@Path("/doctors")
public class DoctorRest {

    @Inject
    private DoctorService doctorService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDoctors() {
        return Response.ok().entity(ResponseModel.builder()
                .data(doctorService.getAllDoctors())
                .build()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDoctor(@Valid DoctorRequestDto doctorRequest) {
        return Response.status(Response.Status.CREATED).entity(ResponseModel.builder()
                .data(doctorService.addDoctor(doctorRequest))
                .build()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDoctor(@PathParam("id") Long id, @Valid DoctorRequestDto doctorRequestDto) {
        return Response.ok().entity(ResponseModel.builder()
                .data(doctorService.updateDoctor(id, doctorRequestDto))
                .build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDoctor(@PathParam("id") Long id) {
        doctorService.deleteDoctor(id);
        return Response.noContent().build();
    }
}
