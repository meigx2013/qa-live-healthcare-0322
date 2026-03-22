-- Doctor User Data Import Script
-- Generated from doctor-user-list.json

USE healthcare;

-- 清空表（如果已存在数据）
TRUNCATE TABLE doctor_specialties;
TRUNCATE TABLE doctor_user;

-- 插入医生用户数据
INSERT INTO doctor_user (id, username, password, name, title, department, avatar, experience, is_active) VALUES
('doc001', 'dr-zhang-wei', '123456', '张伟医生', '主任医师', '心内科', 'https://images.pexels.com/photos/5215024/pexels-photo-5215024.jpeg?auto=compress&cs=tinysrgb&w=400', '15年临床经验', 1),
('doc002', 'dr-li-na', '123456', '李娜医生', '副主任医师', '儿科', 'https://images.pexels.com/photos/5327585/pexels-photo-5327585.jpeg?auto=compress&cs=tinysrgb&w=400', '10年临床经验', 1),
('doc003', 'dr-wang-qiang', '123456', '王强医生', '主治医师', '骨科', 'https://images.pexels.com/photos/5452293/pexels-photo-5452293.jpeg?auto=compress&cs=tinysrgb&w=400', '8年临床经验', 1),
('doc004', 'dr-liu-min', '123456', '刘敏医生', '主任医师', '妇产科', 'https://images.pexels.com/photos/5452201/pexels-photo-5452201.jpeg?auto=compress&cs=tinysrgb&w=400', '18年临床经验', 0),
('doc005', 'dr-chen-jie', '123456', '陈杰医生', '副主任医师', '消化内科', 'https://images.pexels.com/photos/5215024/pexels-photo-5215024.jpeg?auto=compress&cs=tinysrgb&w=400', '12年临床经验', 1);

-- 插入医生专长数据
INSERT INTO doctor_specialties (doctor_id, specialty) VALUES
-- doc001: 张伟医生 - 心内科
('doc001', '高血压'),
('doc001', '冠心病'),
('doc001', '心律失常'),
-- doc002: 李娜医生 - 儿科
('doc002', '儿童感冒'),
('doc002', '儿童发育'),
('doc002', '疫苗接种'),
-- doc003: 王强医生 - 骨科
('doc003', '骨折'),
('doc003', '关节炎'),
('doc003', '运动损伤'),
-- doc004: 刘敏医生 - 妇产科
('doc004', '孕期保健'),
('doc004', '妇科炎症'),
('doc004', '产后恢复'),
-- doc005: 陈杰医生 - 消化内科
('doc005', '胃炎'),
('doc005', '肠道疾病'),
('doc005', '肝病');

-- 验证数据
SELECT COUNT(*) AS '医生总数' FROM doctor_user;
SELECT COUNT(*) AS '专长总数' FROM doctor_specialties;
SELECT d.id, d.name, d.department, d.is_active, COUNT(s.specialty) AS specialty_count 
FROM doctor_user d 
LEFT JOIN doctor_specialties s ON d.id = s.doctor_id 
GROUP BY d.id, d.name, d.department, d.is_active;
