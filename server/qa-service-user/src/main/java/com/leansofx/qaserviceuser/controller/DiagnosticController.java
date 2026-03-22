package com.leansofx.qaserviceuser.controller;

import com.leansofx.qaserviceuser.repository.DoctorUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class DiagnosticController {
    
    @Autowired
    private DoctorUserRepository doctorUserRepository;
    
    @GetMapping("/db-count")
    public ResponseEntity<Map<String, Object>> getDatabaseCount() {
        Map<String, Object> result = new HashMap<>();
        try {
            long count = doctorUserRepository.count();
            result.put("success", true);
            result.put("count", count);
            result.put("message", "Database connection successful");
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("exception", e.getClass().getName());
        }
        return ResponseEntity.ok(result);
    }
}
