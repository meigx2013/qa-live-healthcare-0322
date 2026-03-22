package com.leansofx.qaserviceuser.service.impl;

import com.leansofx.qaserviceuser.dto.request.DoctorUserRequest;
import com.leansofx.qaserviceuser.dto.response.DoctorUserResponse;
import com.leansofx.qaserviceuser.entity.DoctorUser;
import com.leansofx.qaserviceuser.repository.DoctorUserRepository;
import com.leansofx.qaserviceuser.service.DoctorUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorUserServiceImpl implements DoctorUserService {
    
    @Autowired
    private DoctorUserRepository doctorUserRepository;
    
    @Override
    public List<DoctorUserResponse> getAllDoctors() {
        return doctorUserRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public DoctorUserResponse getDoctorById(String id) {
        return doctorUserRepository.findById(id)
                .map(this::convertToResponse)
                .orElse(null);
    }
    
    @Override
    public DoctorUserResponse getDoctorByUsername(String username) {
        return doctorUserRepository.findByUsername(username)
                .map(this::convertToResponse)
                .orElse(null);
    }
    
    @Override
    public List<DoctorUserResponse> getActiveDoctors() {
        return doctorUserRepository.findByIsActive(true).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public DoctorUserResponse createDoctor(DoctorUserRequest request) {
        DoctorUser doctor = convertToEntity(request);
        DoctorUser savedDoctor = doctorUserRepository.save(doctor);
        return convertToResponse(savedDoctor);
    }
    
    @Override
    public DoctorUserResponse updateDoctor(String id, DoctorUserRequest request) {
        return doctorUserRepository.findById(id)
                .map(existingDoctor -> {
                    updateDoctorFromRequest(existingDoctor, request);
                    DoctorUser updatedDoctor = doctorUserRepository.save(existingDoctor);
                    return convertToResponse(updatedDoctor);
                })
                .orElse(null);
    }
    
    @Override
    public void deleteDoctor(String id) {
        doctorUserRepository.deleteById(id);
    }
    
    @Override
    public DoctorUserResponse convertToResponse(DoctorUser doctor) {
        if (doctor == null) {
            return null;
        }
        
        return new DoctorUserResponse(
                doctor.getId(),
                doctor.getUsername(),
                doctor.getName(),
                doctor.getTitle(),
                doctor.getDepartment(),
                doctor.getAvatar(),
                doctor.getExperience(),
                doctor.getSpecialties(),
                doctor.getIsActive()
        );
    }
    
    @Override
    public DoctorUser convertToEntity(DoctorUserRequest request) {
        if (request == null) {
            return null;
        }
        
        return new DoctorUser(
                request.getId(),
                request.getUsername(),
                request.getPassword(),
                request.getName(),
                request.getTitle(),
                request.getDepartment(),
                request.getAvatar(),
                request.getExperience(),
                request.getSpecialties(),
                request.getIsActive()
        );
    }
    
    /**
     * 更新医生信息（不更新ID）
     */
    private void updateDoctorFromRequest(DoctorUser doctor, DoctorUserRequest request) {
        doctor.setUsername(request.getUsername());
        doctor.setPassword(request.getPassword());
        doctor.setName(request.getName());
        doctor.setTitle(request.getTitle());
        doctor.setDepartment(request.getDepartment());
        doctor.setAvatar(request.getAvatar());
        doctor.setExperience(request.getExperience());
        doctor.setSpecialties(request.getSpecialties());
        doctor.setIsActive(request.getIsActive());
    }
}
