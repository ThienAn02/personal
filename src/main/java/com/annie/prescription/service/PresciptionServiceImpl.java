package com.annie.prescription.service;

import com.annie.prescription.dao.PrescriptionDao;
import com.annie.prescription.dto.PrescriptionResponseDto;
import com.annie.prescription.dto.PrescriptionAppointmentDto;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class PresciptionServiceImpl implements PresciptionService {
    @Inject
    PrescriptionDao prescriptionDao;

    @Inject
    PresciptionMapper presciptionMapper;

    public List<PrescriptionResponseDto> findByAppointmentId (Long id) {
        return presciptionMapper.toResponseDTOList(prescriptionDao.findByAppointmentId(id));
    }
    public List<PrescriptionAppointmentDto> getAppointmentsByMedicine(String medicineName) {
        return prescriptionDao.getAppointmentsByMedicineName(medicineName);
    }

}
