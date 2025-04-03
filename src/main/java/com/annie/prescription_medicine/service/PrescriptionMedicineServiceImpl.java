package com.annie.prescription_medicine.service;

import com.annie.prescription_medicine.constants.PrescriptionMedicineExceptionMessage;
import com.annie.prescription_medicine.dao.PrescriptionMedicineDao;
import com.annie.prescription_medicine.dto.PrescriptionMedicineResponseDto;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;

@Stateless
public class PrescriptionMedicineServiceImpl implements PrescriptionMedicineService {

    @Inject
    private PrescriptionMedicineDao presciptionMedicineDao;

    public List<PrescriptionMedicineResponseDto> getMedicineByPrescription(Long prescriptionId) {
        if (prescriptionId == null || prescriptionId <= 0) {
            throw new BadRequestException(PrescriptionMedicineExceptionMessage.INVALID_PRESCRIPTION_ID);
        }
        return presciptionMedicineDao.findMedicineByPresciption(prescriptionId);
    }
}
