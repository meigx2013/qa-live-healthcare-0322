package com.leansofx.qaserviceuser.service;

import com.leansofx.qaserviceuser.dto.request.DoctorUserRequest;
import com.leansofx.qaserviceuser.dto.response.DoctorUserResponse;
import com.leansofx.qaserviceuser.entity.DoctorUser;

import java.util.List;

public interface DoctorUserService {
    
    /**
     * 获取所有医生列表
     */
    List<DoctorUserResponse> getAllDoctors();
    
    /**
     * 根据ID获取医生信息
     */
    DoctorUserResponse getDoctorById(String id);
    
    /**
     * 根据用户名获取医生信息
     */
    DoctorUserResponse getDoctorByUsername(String username);
    
    /**
     * 获取所有激活的医生列表
     */
    List<DoctorUserResponse> getActiveDoctors();
    
    /**
     * 创建医生
     */
    DoctorUserResponse createDoctor(DoctorUserRequest request);
    
    /**
     * 更新医生信息
     */
    DoctorUserResponse updateDoctor(String id, DoctorUserRequest request);
    
    /**
     * 删除医生
     */
    void deleteDoctor(String id);
    
    /**
     * 将Entity转换为Response DTO
     */
    DoctorUserResponse convertToResponse(DoctorUser doctor);
    
    /**
     * 将Request DTO转换为Entity
     */
    DoctorUser convertToEntity(DoctorUserRequest request);
}
