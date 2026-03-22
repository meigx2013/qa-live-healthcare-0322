package com.leansofx.qaserviceuser.controller;

import com.leansofx.qaserviceuser.dto.request.DoctorUserRequest;
import com.leansofx.qaserviceuser.dto.response.DoctorUserResponse;
import com.leansofx.qaserviceuser.service.DoctorUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DoctorUserController {
    
    @Autowired
    private DoctorUserService doctorUserService;
    
    /**
     * 获取所有医生列表
     * GET /api/doctors
     */
    @GetMapping
    public ResponseEntity<List<DoctorUserResponse>> getAllDoctors() {
        List<DoctorUserResponse> doctors = doctorUserService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
    
    /**
     * 根据ID获取医生信息
     * GET /api/doctors/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorUserResponse> getDoctorById(@PathVariable String id) {
        DoctorUserResponse doctor = doctorUserService.getDoctorById(id);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctor);
    }
    
    /**
     * 根据用户名获取医生信息
     * GET /api/doctors/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<DoctorUserResponse> getDoctorByUsername(@PathVariable String username) {
        DoctorUserResponse doctor = doctorUserService.getDoctorByUsername(username);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctor);
    }
    
    /**
     * 获取所有激活的医生列表
     * GET /api/doctors/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<DoctorUserResponse>> getActiveDoctors() {
        List<DoctorUserResponse> doctors = doctorUserService.getActiveDoctors();
        return ResponseEntity.ok(doctors);
    }
    
    /**
     * 创建医生
     * POST /api/doctors
     */
    @PostMapping
    public ResponseEntity<DoctorUserResponse> createDoctor(@RequestBody DoctorUserRequest request) {
        DoctorUserResponse doctor = doctorUserService.createDoctor(request);
        return ResponseEntity.ok(doctor);
    }
    
    /**
     * 更新医生信息
     * PUT /api/doctors/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<DoctorUserResponse> updateDoctor(
            @PathVariable String id,
            @RequestBody DoctorUserRequest request) {
        DoctorUserResponse doctor = doctorUserService.updateDoctor(id, request);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctor);
    }
    
    /**
     * 删除医生
     * DELETE /api/doctors/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable String id) {
        doctorUserService.deleteDoctor(id);
        return ResponseEntity.ok().build();
    }
}
