package com.leansofx.qaserviceuser.repository;

import com.leansofx.qaserviceuser.entity.DoctorUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorUserRepository extends JpaRepository<DoctorUser, String> {
    
    /**
     * 根据用户名查找医生
     */
    Optional<DoctorUser> findByUsername(String username);
    
    /**
     * 根据激活状态查找医生列表
     */
    List<DoctorUser> findByIsActive(Boolean isActive);
    
    /**
     * 根据科室查找医生列表
     */
    List<DoctorUser> findByDepartment(String department);
    
    /**
     * 根据职称查找医生列表
     */
    List<DoctorUser> findByTitle(String title);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
}
