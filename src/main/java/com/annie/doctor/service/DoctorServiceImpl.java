package com.annie.doctor.service;

import com.annie.auth.service.AuthService;
import com.annie.base.common.Gender;
import com.annie.doctor.constants.DoctorExceptionMessage;
import com.annie.doctor.dao.DoctorDao;
import com.annie.doctor.dto.DoctorRequestDto;
import com.annie.doctor.dto.DoctorResponseDto;
import com.annie.doctor.entity.Doctor;
import com.annie.specialty.dao.SpecialtyDao;
import com.annie.specialty.entity.Specialty;
import com.annie.base.exception.IdNotFoundException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Stateless
public class DoctorServiceImpl implements DoctorService {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private DoctorDao doctorDao;

    @Inject
    private SpecialtyDao specialtyDAO;

    @Inject
    private DoctorMapper doctorMapper;

    @Inject
    private AuthService authService;

    /**
     * Retrieves a doctor by their ID.
     *
     * @param id the ID of the doctor
     * @return the corresponding DoctorResponseDto
     * @throws BadRequestException if the doctor is not found
     */
    @Override
    public DoctorResponseDto getDoctorById(Long id) {
        Optional<Doctor> doctorById = doctorDao.findById(id);
        return doctorById.map(doctorMapper::toResponseDTO)
                .orElseThrow(() -> new BadRequestException(DoctorExceptionMessage.DOCTOR_NOT_FOUND));
    }

    /**
     * Retrieves all doctors.
     *
     * @return a list of DoctorResponseDto
     */
    @Override
    public List<DoctorResponseDto> getAllDoctors() {
        List<Doctor> doctors = doctorDao.findAll();
        return doctors.stream()
                .map(doctorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Adds a new doctor.
     *
     * @param doctorRequestDto the request data for creating a doctor
     * @return the created DoctorResponseDto
     * @throws BadRequestException if a doctor with the same phone already exists
     * @throws IdNotFoundException if the specified specialty is not found
     */
    @Override
    public DoctorResponseDto addDoctor(DoctorRequestDto doctorRequestDto) {
        boolean isExisted = doctorDao.findByPhone(doctorRequestDto.getPhone());
        if (isExisted) {
            throw new BadRequestException(DoctorExceptionMessage.DOCTOR_PHONE_EXISTS);
        }
        Doctor doctor = doctorMapper.toEntity(doctorRequestDto);
        Specialty specialty = specialtyDAO.findById(doctorRequestDto.getSpecialtyId())
                .orElseThrow(() -> new IdNotFoundException(DoctorExceptionMessage.SPECIALTY_NOT_FOUND));
        doctor.setSpecialty(specialty);
        Doctor addedDoctor = doctorDao.save(doctor);
        return doctorMapper.toResponseDTO(addedDoctor);
    }

    /**
     * Updates an existing doctor.
     *
     * @param id               the ID of the doctor to be updated
     * @param doctorRequestDto the updated details
     * @return the updated DoctorResponseDto
     * @throws IdNotFoundException if the doctor or specialty is not found
     */
    @Override
    public DoctorResponseDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto) {
        Doctor existingDoctor = doctorDao.findById(id)
                .orElseThrow(() -> new IdNotFoundException(DoctorExceptionMessage.DOCTOR_NOT_FOUND));

        existingDoctor.setName(doctorRequestDto.getName());
        existingDoctor.setEmail(doctorRequestDto.getEmail());
        existingDoctor.setPhone(doctorRequestDto.getPhone());
        existingDoctor.setAddress(doctorRequestDto.getAddress());
        existingDoctor.setGender(Gender.valueOf(doctorRequestDto.getGender()));
        existingDoctor.setDob(doctorRequestDto.getDob());
        existingDoctor.setExperienceYears(doctorRequestDto.getExperienceYears());
        existingDoctor.setQualification(doctorRequestDto.getQualification());

        Specialty specialty = specialtyDAO.findById(doctorRequestDto.getSpecialtyId())
                .orElseThrow(() -> new IdNotFoundException(DoctorExceptionMessage.SPECIALTY_NOT_FOUND));
        existingDoctor.setSpecialty(specialty);

        Doctor updatedDoctor = doctorDao.update(existingDoctor);

        return doctorMapper.toResponseDTO(updatedDoctor);
    }

    /**
     * Deletes a doctor by ID.
     *
     * @param id the ID of the doctor to be deleted
     * @throws BadRequestException if the doctor is not found
     */
    @Transactional
    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorDao.findById(id)
                .orElseThrow(() -> new BadRequestException(DoctorExceptionMessage.DOCTOR_NOT_FOUND));

        doctor.setDeleted(true);
        doctorDao.save(doctor);
    }

    /**
     * Retrieves available slots for a doctor on a specific date.
     *
     * @param doctorId the ID of the doctor
     * @param date     the date for which slots are requested
     * @return a list of available LocalDateTime slots
     */
    public List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date) {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        if (date.isBefore(today)) {
            throw new IllegalArgumentException(DoctorExceptionMessage.SELECTED_DATE_NOT_PERMIT);
        }
        return doctorDao.getAvailableSlots(doctorId, date);
    }


    /**
     * Retrieves a Doctor entity by ID.
     *
     * @param id the ID of the doctor
     * @return the Doctor entity
     * @throws IdNotFoundException if the doctor is not found
     */
    @Override
    public Doctor getDoctorEntityById(Long id) {
        return doctorDao.findById(id)
                .orElseThrow(() -> new IdNotFoundException(DoctorExceptionMessage.DOCTOR_NOT_FOUND));
    }

    public List<DoctorResponseDto> getDoctorsByStatus(boolean isDeleted) {
        List<Doctor> doctors = doctorDao.findAll().stream()
                .filter(doctor -> doctor.isDeleted() == isDeleted)
                .toList();
        return doctorMapper.toResponseLisDTO(doctors);
    }

    public List<DoctorResponseDto> getDoctorsBySpecialty(String specialtyName) {
        List<Doctor> doctors = doctorDao.getDoctorsBySpecialty(specialtyName);

        return doctorMapper.toResponseLisDTO(doctors)
                .stream()
                .sorted(Comparator.comparing(DoctorResponseDto::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Doctor getDoctorByEmail(String doctorEmail) {
        return doctorDao.getDoctorByEmail(doctorEmail);
    }

    private LocalDate getCellValueAsDate(Cell cell) {
        if (cell == null) {
            throw new BadRequestException(DoctorExceptionMessage.DATA_COLUMN_IS_EMPTY);
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            String dateStr = cell.getStringCellValue().trim();

            if (dateStr.isEmpty()) {
                throw new BadRequestException(DoctorExceptionMessage.DATA_COLUMN_IS_EMPTY);
            }
            try {
                return LocalDate.parse(dateStr);
            } catch (Exception e) {
                String[] patterns = {
                        "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy",
                        "yyyy/MM/dd", "dd-MM-yyyy", "MM-dd-yyyy",
                        "dd-MMM-yyyy", "MMM dd, yyyy"
                };

                for (String pattern : patterns) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                        return LocalDate.parse(dateStr, formatter);
                }
            }

            throw new BadRequestException(DoctorExceptionMessage.INVALID_DATE_FORMAT);
        }

        throw new BadRequestException(DoctorExceptionMessage.INVALID_DATE_FORMAT);
    }


    private List<Doctor> parseExcelFile(InputStream inputStream) throws IOException {
        List<Doctor> doctors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    Cell dobCell = row.getCell(8);
                    LocalDate dob = getCellValueAsDate(dobCell);

                    if (dob == null) {
                        throw new IllegalArgumentException("DOB is missing or invalid at row " + (i + 1));
                    }

                    Doctor doctor = Doctor.builder()
                            .name(getCellValueAsString(row.getCell(0)))
                            .email(getCellValueAsString(row.getCell(1)))
                            .phone(getCellValueAsString(row.getCell(2)))
                            .specialty(findSpecialtyByName(getCellValueAsString(row.getCell(3))))
                            .experienceYears(getCellValueAsInteger(row.getCell(4)))
                            .qualification(getCellValueAsString(row.getCell(5)))
                            .address(getCellValueAsString(row.getCell(6)))
                            .gender(parseGender(getCellValueAsString(row.getCell(7))))
                            .dob(dob)
                            .build();

                    doctors.add(doctor);
                } catch (Exception e) {
                    System.err.println("Error parsing row " + i + ": " + e.getMessage());
                    throw new IllegalArgumentException("Error in row " + (i + 1) + ": " + e.getMessage(), e);
                }
            }
        }

        return doctors;
    }


    public List<DoctorResponseDto> importDoctorsFromExcel(MultipartFormDataInput input) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");

            if (inputParts == null || inputParts.isEmpty()) {
                throw new BadRequestException("No file uploaded");
            }

            InputPart inputPart = inputParts.get(0);
            InputStream inputStream = inputPart.getBody(InputStream.class, null);

            List<Doctor> doctors = parseExcelFile(inputStream);

            if (doctors.isEmpty()) {
                throw new BadRequestException("No valid doctor records found in the Excel file. Check date format.");
            }

            List<Doctor> savedDoctors = new ArrayList<>();

            for (Doctor doctor : doctors) {
                if (doctor.getDob() == null) {
                    throw new IllegalArgumentException("Doctor " + doctor.getName() + " has an invalid or missing DOB.");
                }

                Doctor savedDoctor = doctorDao.save(doctor);
                authService.createDoctorAccount(savedDoctor);
                savedDoctors.add(savedDoctor);
            }

            return savedDoctors.stream()
                    .map(doctorMapper::toResponseDTO)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new BadRequestException("Failed to process Excel file: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid data in Excel file: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new BadRequestException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                    }
                    DecimalFormat df = new DecimalFormat("0");
                    return df.format(cell.getNumericCellValue());
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);

                    Cell tempCell = cell.getRow().createCell(Short.MAX_VALUE);

                    switch (cellValue.getCellType()) {
                        case BOOLEAN:
                            tempCell.setCellValue(cellValue.getBooleanValue());
                            break;
                        case NUMERIC:
                            tempCell.setCellValue(cellValue.getNumberValue());
                            break;
                        case STRING:
                            tempCell.setCellValue(cellValue.getStringValue());
                            break;
                        default:
                            cell.getRow().removeCell(tempCell);
                            return null;
                    }

                    String result = getCellValueAsString(tempCell);

                    cell.getRow().removeCell(tempCell);

                    return result;
                case BLANK:
                    return "";
                case ERROR:
                    return "ERROR: " + cell.getErrorCellValue();
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private Gender parseGender(String genderStr) {
        if (genderStr == null || genderStr.trim().isEmpty()) {
            return null;
        }
        return switch (genderStr.toUpperCase()) {
            case "MALE" -> Gender.MALE;
            case "FEMALE" -> Gender.FEMALE;
            case "OTHER" -> Gender.OTHER;
            default -> throw new IllegalArgumentException("Invalid gender: " + genderStr);
        };
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
        return (int) cell.getNumericCellValue();
    }

    private Specialty findSpecialtyByName(String specialtyName) {
        if (specialtyName == null || specialtyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialty name is missing");
        }
        return specialtyDAO.findByName(specialtyName)
                .orElseThrow(() -> new IllegalArgumentException("Specialty not found: " + specialtyName));
    }

}
