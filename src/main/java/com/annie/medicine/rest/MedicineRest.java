package com.annie.medicine.rest;

import com.annie.medicine.dto.MedicineResponseDto;
import com.annie.medicine.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/medicines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Medicine", description = "Operations related to medicines")
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class MedicineRest {

    @Inject
    private MedicineService medicineService;

    /**
     * Search for medicines by patient name and year.
     *
     * @param patientName Patient's name.
     * @param year The year to filter medicines.
     * @return List of medicines.
     */
    @GET
    @Path("/search")
    @Operation(summary = "Search Medicines", description = "Find medicines based on patient name and year.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicines retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed({"DOCTOR","ADMIN"})
    public Response getMedicineByNameAndYear(
            @QueryParam("patientName") String patientName,
            @QueryParam("year") Integer year) {
        List<MedicineResponseDto> medicines = medicineService.findMedicineByName(patientName, year);
        return Response.ok(medicines).build();
    }

    /**
     * Get medicines by prescription ID.
     *
     * @param id Prescription ID.
     * @return List of medicines associated with the prescription.
     */
    @GET
    @Path("/by-prescription/{id}")
    @Operation(summary = "Get Medicines by Prescription", description = "Retrieve medicines associated with a specific prescription ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicines retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid prescription ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Prescription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @RolesAllowed({"DOCTOR","ADMIN"})
    public Response getMedicinesByPrescription(@PathParam("id") Integer id) {
        List<MedicineResponseDto> medicines = medicineService.getMedicinesByPrescriptionId(id);
        return Response.ok(medicines).build();
    }
}
