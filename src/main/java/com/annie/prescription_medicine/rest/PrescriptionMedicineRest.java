package com.annie.prescription_medicine.rest;

import com.annie.base.response.ResponseModel;
import com.annie.prescription_medicine.dto.PrescriptionMedicineResponseDto;
import com.annie.prescription_medicine.service.PrescriptionMedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/prescription-medicine")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Prescription Medicine", description = "Operations related to medicines in prescriptions")
public class PrescriptionMedicineRest {

    @Inject
    private PrescriptionMedicineService prescriptionMedicineService;

    /**
     * Retrieves medicines associated with a specific prescription.
     * @param prescriptionId ID of the prescription
     * @return Response containing list of medicines
     */
    @GET
    @Path("/{prescriptionId}")
    @Operation(summary = "Get medicines by prescription ID",
            description = "Retrieve all medicines associated with a given prescription.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicines found"),
            @ApiResponse(responseCode = "404", description = "Prescription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getMedicineByPrescription(@PathParam("prescriptionId") Long prescriptionId) {
        List<PrescriptionMedicineResponseDto> medicines = prescriptionMedicineService.getMedicineByPrescription(prescriptionId);

        if (medicines.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No medicines found for this prescription")
                    .build();
        }

        return Response.ok().entity(ResponseModel.builder()
                .data(medicines)
                .build()).build();
    }
}
