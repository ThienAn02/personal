package com.annie.doctor.service;

import com.annie.doctor.dao.DoctorDao;
import com.annie.doctor.dto.DoctorRequestDto;
import com.annie.doctor.dto.DoctorResponseDto;
import com.annie.doctor.entity.Doctor;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class DoctorServiceImpl implements DoctorService {

    @Inject
    private DoctorDao doctorDao;

    @Inject
    private DoctorMapper doctorMapper;

    @Override
    public DoctorResponseDto getDoctorById(Long id) {
        Optional<Doctor> doctorById = doctorDao.findById(id);
        return doctorById.map(doctorMapper::toResponseDTO)
                .orElseThrow(() -> new BadRequestException("Doctor not found"));
    }

    @Override
    public List<DoctorResponseDto> getAllDoctors() {
        List<Doctor> doctors = doctorDao.findAll();
        return doctors.stream()
                .map(doctorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponseDto addDoctor(DoctorRequestDto doctorRequestDto) {
        boolean isExisted = doctorDao.findByPhone(doctorRequestDto.getPhone());
        if (isExisted) {
            throw new BadRequestException("Doctor with this phone already exists");
        }

        // Map DTO to entity
        Doctor doctor = doctorMapper.toEntity(doctorRequestDto);
        Doctor addedDoctor = doctorDao.save(doctor);
        return doctorMapper.toResponseDTO(addedDoctor);
    }

    @Override
    public DoctorResponseDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto) {
        Optional<Doctor> existingDoctorOpt = doctorDao.findById(id);
        if (!existingDoctorOpt.isPresent()) {
            throw new BadRequestException("Doctor not found");
        }

        Doctor existingDoctor = existingDoctorOpt.get();

        if (doctorRequestDto.getName() != null) {
            existingDoctor.setName(doctorRequestDto.getName());
        }
        if (doctorRequestDto.getAddress() != null) {
            existingDoctor.setAddress(doctorRequestDto.getAddress());
        }
        if (doctorRequestDto.getPhone() != null) {
            existingDoctor.setPhone(doctorRequestDto.getPhone());
        }
        if (doctorRequestDto.getSpecialty() != null) {
            existingDoctor.setSpecialty(doctorRequestDto.getSpecialty());
        }
        if (doctorRequestDto.getGender() != null) {
            existingDoctor.setGender(doctorRequestDto.getGender());
        }

        Doctor updatedDoctor = doctorDao.update(existingDoctor);
        return doctorMapper.toResponseDTO(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        boolean isExisted = doctorDao.findById(id).isPresent();
        if (!isExisted) {
            throw new BadRequestException("Doctor not found");
        }
        doctorDao.delete(id);
    }
}
