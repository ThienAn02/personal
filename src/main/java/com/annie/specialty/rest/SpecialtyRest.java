package com.annie.specialty.rest;

import com.annie.base.response.ResponseModel;
import com.annie.specialty.dto.SpecialtyDoctorResponseDto;
import com.annie.specialty.dto.SpecialtyResponseDto;
import com.annie.specialty.service.SpecialtyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/specialties")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Specialty", description = "Operations related to medical specialties")
public class SpecialtyRest {

    @Inject
    private SpecialtyService specialtyService;

    /**
     * Retrieves doctors associated with a specific specialty.
     * @param specialtyId ID of the specialty
     * @return Response containing list of doctors
     */
    @GET
    @Path("/{id}/doctors")
    @Operation(
        summary = "Get doctors by specialty", 
        description = "Returns a list of doctors associated with a specific specialty. Each doctor includes their basic information and specialization details"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of doctors"),
            @ApiResponse(responseCode = "404", description = "No doctors found for the specified specialty"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    public Response getDoctorsBySpecialty(@PathParam("id") Long specialtyId) {
        return Response.ok().entity(ResponseModel.builder()
                .data(specialtyService.getDoctorsBySpecialty(specialtyId))
                .build()).build();
    }
    @GET
    @Operation(
        summary = "Get all medical specialties", 
        description = "Returns a comprehensive list of all medical specialties available in the system, including their details and descriptions"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of specialties"),
            @ApiResponse(responseCode = "404", description = "No specialties found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
     public Response getAllSpecialty() {
        return Response.ok().entity(ResponseModel.builder()
                .data(specialtyService.getAllSpecialty())
                .build()).build();
    }
}
