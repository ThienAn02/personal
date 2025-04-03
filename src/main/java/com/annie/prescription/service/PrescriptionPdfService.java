package com.annie.prescription.service;

import com.annie.prescription.entity.Prescription;
import com.annie.prescription_medicine.entity.PrescriptionMedicine;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PrescriptionPdfService {
    private static final Logger LOGGER = Logger.getLogger(PrescriptionPdfService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public byte[] generatePrescriptionPdf(Long prescriptionId) {
        try {
            // Fetch prescription
            Prescription prescription = entityManager.find(Prescription.class, prescriptionId);

            if (prescription == null) {
                LOGGER.severe("No prescription found with ID: " + prescriptionId);
                throw new RuntimeException("Prescription not found with ID: " + prescriptionId);
            }

            // Fetch prescription medicines
            List<PrescriptionMedicine> prescriptionMedicines = entityManager.createQuery(
                            "SELECT pm FROM PrescriptionMedicine pm " +
                                    "LEFT JOIN FETCH pm.medicine " +
                                    "WHERE pm.prescription.id = :prescriptionId", PrescriptionMedicine.class)
                    .setParameter("prescriptionId", prescriptionId)
                    .getResultList();

            // Debug: Create local file for verification
            File debugPdfFile = File.createTempFile("prescription_debug_", ".pdf");
            LOGGER.info("Debug PDF file created at: " + debugPdfFile.getAbsolutePath());

            // First, create a local file for debugging
            try (FileOutputStream debugFos = new FileOutputStream(debugPdfFile);
                 PdfWriter debugWriter = new PdfWriter(debugFos);
                 PdfDocument debugPdf = new PdfDocument(debugWriter);
                 Document debugDocument = new Document(debugPdf)) {

                createPdfContent(debugDocument, prescription, prescriptionMedicines);
            }

            // Now create the byte array output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (PdfWriter writer = new PdfWriter(baos);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                createPdfContent(document, prescription, prescriptionMedicines);
            }

            byte[] pdfBytes = baos.toByteArray();

            // Log PDF byte array details
            LOGGER.info("Generated PDF byte array length: " + pdfBytes.length);

            return pdfBytes;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating prescription PDF", e);
            throw new RuntimeException("Error generating prescription PDF", e);
        }
    }

    private void createPdfContent(Document document,
                                  Prescription prescription,
                                  List<PrescriptionMedicine> prescriptionMedicines) {
        // Prescription Header
        document.add(new Paragraph("MEDICAL PRESCRIPTION")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setBold());

        // Patient and Doctor Information
        document.add(new Paragraph("Patient: " +
                (prescription.getAppointment().getPatient() != null
                        ? prescription.getAppointment().getPatient().getName()
                        : "N/A"))
                .setFontSize(12));
        document.add(new Paragraph("Doctor: " +
                (prescription.getAppointment().getDoctor() != null
                        ? prescription.getAppointment().getDoctor().getName()
                        : "N/A"))
                .setFontSize(12));

        // Prescription Details
        document.add(new Paragraph("Date Issued: " +
                prescription.getDateIssued().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .setFontSize(12));
        document.add(new Paragraph("Diagnosis: " +
                (prescription.getDiagnosis() != null ? prescription.getDiagnosis() : "N/A"))
                .setFontSize(12));
        document.add(new Paragraph("Treatment: " +
                (prescription.getTreatment() != null ? prescription.getTreatment() : "N/A"))
                .setFontSize(12));

        // Medicines Table
        Table medicinesTable = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        medicinesTable.addHeaderCell("Medicine");
        medicinesTable.addHeaderCell("Dosage");
        medicinesTable.addHeaderCell("Duration");
        medicinesTable.addHeaderCell("Administration");

        // Add medicines to table
        if (prescriptionMedicines != null && !prescriptionMedicines.isEmpty()) {
            for (PrescriptionMedicine pm : prescriptionMedicines) {
                medicinesTable.addCell(pm.getMedicine() != null
                        ? pm.getMedicine().getName()
                        : "N/A");
                medicinesTable.addCell(pm.getDosage() + " mg");
                medicinesTable.addCell(pm.getDuration() + " days");
                medicinesTable.addCell(pm.getMedicine() != null
                        ? pm.getMedicine().getAdministration()
                        : "N/A");
            }
        } else {
            medicinesTable.addCell("No medicines prescribed");
        }
        document.add(medicinesTable);

        // Additional Notes
        if (prescription.getNote() != null && !prescription.getNote().isEmpty()) {
            document.add(new Paragraph("Notes: " + prescription.getNote())
                    .setFontSize(10)
                    .setItalic());
        }

        // Next Appointment
        if (prescription.getNextAppointmentDate() != null) {
            document.add(new Paragraph("Next Appointment: " +
                    prescription.getNextAppointmentDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                    .setFontSize(12));
        }
    }
}