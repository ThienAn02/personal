package com.annie.medicine.service;

import com.annie.medicine.dao.MedicineDao;
import com.annie.medicine.dto.MedicineResponseDto;
import com.annie.medicine.entity.Medicine;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class MedicineServiceImpl implements MedicineService{

    @Inject
    private MedicineDao medicineDao;

    @Inject
    private MedicineMapper medicineMapper;


    @Override
    public List<MedicineResponseDto> findMedicineByName(String name, Integer year) {
        List<Medicine> medicines = medicineDao.findMedicineByName(name, year);
        return  medicineMapper.toMedicineResponseDTOs(medicines);
    }

    public List<MedicineResponseDto> getMedicinesByPrescriptionId(Integer prescriptionId) {
        return medicineMapper.toMedicineResponseDTOs(medicineDao.findMedicinesByPrescriptionId(prescriptionId));
    }


}
