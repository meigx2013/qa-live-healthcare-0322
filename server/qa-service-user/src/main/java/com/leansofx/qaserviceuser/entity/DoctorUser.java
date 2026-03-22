package com.leansofx.qaserviceuser.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctor_user")
public class DoctorUser {
    
    @Id
    @Column(length = 20)
    private String id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(length = 30)
    private String title;
    
    @Column(length = 30)
    private String department;
    
    @Column(columnDefinition = "TEXT")
    private String avatar;
    
    @Column(length = 50)
    private String experience;
    
    @ElementCollection
    @CollectionTable(name = "doctor_specialties", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "specialty")
    private List<String> specialties = new ArrayList<>();
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    // 默认构造函数
    public DoctorUser() {
    }
    
    // 全参构造函数
    public DoctorUser(String id, String username, String password, String name, 
                     String title, String department, String avatar, 
                     String experience, List<String> specialties, Boolean isActive) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.title = title;
        this.department = department;
        this.avatar = avatar;
        this.experience = experience;
        this.specialties = specialties;
        this.isActive = isActive;
    }
    
    // Getter 和 Setter 方法
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getExperience() {
        return experience;
    }
    
    public void setExperience(String experience) {
        this.experience = experience;
    }
    
    public List<String> getSpecialties() {
        return specialties;
    }
    
    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @Override
    public String toString() {
        return "DoctorUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", department='" + department + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
