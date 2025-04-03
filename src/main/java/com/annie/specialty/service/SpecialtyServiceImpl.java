package com.annie.specialty.service;

import com.annie.specialty.constants.SpecialtyExceptionMessage;
import com.annie.specialty.dao.SpecialtyDao;
import com.annie.specialty.dto.SpecialtyDoctorResponseDto;
import com.annie.specialty.dto.SpecialtyResponseDto;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@Stateless
public class SpecialtyServiceImpl implements SpecialtyService {

    @Inject
    private SpecialtyDao specialtyDao;

    @Inject
    private SpecialtyMapper specialtyMapper;

   @Override
    public List<SpecialtyDoctorResponseDto> getDoctorsBySpecialty(Long specialtyId) {
        List<SpecialtyDoctorResponseDto> doctors = specialtyDao.getDoctorsBySpecialty(specialtyId);
        if (doctors.isEmpty()) {
            throw new NotFoundException(SpecialtyExceptionMessage.DOCTORS_NOT_FOUND);
        }
        return doctors;
    }

    @Override
    public List<SpecialtyResponseDto> getAllSpecialty() {
        List<SpecialtyResponseDto> specialties = specialtyMapper.toListResponseDto(specialtyDao.findAll());
        if (specialties.isEmpty()) {
            throw new NotFoundException(SpecialtyExceptionMessage.SPECIALTY_NOT_FOUND);
        }
        return specialties;
    }
}
