package com.annie.prescription.rest;

import com.annie.base.response.ResponseModel;
import com.annie.prescription.service.PresciptionService;
import com.annie.prescription.service.PrescriptionPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/prescriptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Prescription", description = "Operations related to prescriptions")
public class PresciptionRest {
    private static final Logger LOGGER = Logger.getLogger(PresciptionRest.class.getName());
    @Inject
    private PresciptionService presciptionService;
    @Inject
    private PrescriptionPdfService prescriptionPdfService;

    /**
     * Retrieves a prescription by appointment ID.
     * @param id Appointment ID
     * @return Response containing prescription details
     */
    @GET
    @Path("/{id}")
    @Operation(summary = "Find prescription by appointment ID",
            description = "Retrieve a prescription associated with an appointment.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prescription found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response findByAppointmentId(@PathParam("id") Long id) {
        var prescription = presciptionService.findByAppointmentId(id);

        if (prescription == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Appointment not found")
                    .build();
        }
        return Response.ok().entity(ResponseModel.builder()
                .data(prescription)
                .build()).build();
    }

    /**
     * Retrieves appointments by medicine name.
     * @param medicineName Name of the medicine
     * @return Response containing list of appointments where the medicine was prescribed
     */
    @GET
    @Path("/appointments")
    @Operation(summary = "Get appointments by medicine name",
            description = "Retrieve appointments where a specific medicine was prescribed.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (Medicine name required)"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getAppointmentsByMedicine(@QueryParam("medicinename") String medicineName) {
        if (medicineName == null || medicineName.isEmpty()) {
            throw new BadRequestException("Medicine name is required");
        }
        var result = presciptionService.getAppointmentsByMedicine(medicineName);
        return Response.ok().entity(ResponseModel.builder()
                .data(result)
                .build()).build();
    }
    @GET
    @Path("/{prescriptionId}/download")
    @Produces("application/pdf")
    public Response downloadPrescriptionPdf(@PathParam("prescriptionId") Long prescriptionId) {
        try {
            byte[] pdfContent = prescriptionPdfService.generatePrescriptionPdf(prescriptionId);
            if (pdfContent == null || pdfContent.length == 0) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("No PDF content generated")
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }

            return Response.ok(pdfContent)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=prescription_" + prescriptionId + ".pdf")
                    .type("application/pdf")
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in PDF download", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error generating prescription PDF: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

}
