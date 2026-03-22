# 数据模型文档

## 概述

本文档详细描述了 QA Service User 服务中的数据模型设计，包括实体类（Entity）、数据传输对象（DTO）以及相应的数据库表结构。

## 实体模型

### DoctorUser（医生用户实体）

医生用户实体是本服务的核心数据模型，用于存储医生用户的基本信息。

#### 类定义
- **包路径**: `com.leansofx.qaserviceuser.entity.DoctorUser`
- **表名**: `doctor_user`
- **类型**: JPA Entity

#### 字段说明

| 字段名 | 类型 | 数据库列名 | 约束 | 长度 | 说明 |
|--------|------|-----------|------|------|------|
| id | String | id | PRIMARY KEY | 20 | 医生用户唯一标识符 |
| username | String | username | NOT NULL, UNIQUE | 50 | 登录用户名，唯一且不可为空 |
| password | String | password | NOT NULL | - | 登录密码（加密存储） |
| name | String | name | NOT NULL | 50 | 医生真实姓名 |
| title | String | title | - | 30 | 职称（如：主任医师、副主任医师等） |
| department | String | department | - | 30 | 所属科室 |
| avatar | String | avatar | - | TEXT | 头像图片URL或Base64编码 |
| experience | String | experience | - | 50 | 从业经验描述 |
| specialties | List&lt;String&gt; | - | - | - | 医生专长列表（存储在关联表中） |
| isActive | Boolean | is_active | NOT NULL | - | 账户是否激活，默认值为 true |

#### 特殊注解

- `@Entity`: 标识这是一个JPA实体类
- `@Table(name = "doctor_user")`: 指定数据库表名
- `@Id`: 标识主键字段
- `@ElementCollection`: 标识 specialties 字段为元素集合
- `@CollectionTable`: 定义 specialties 的存储表


## DTO 模型

### DoctorUserRequest（请求DTO）

用于接收客户端请求的数据传输对象。

#### 类定义
- **包路径**: `com.leansofx.qaserviceuser.dto.request.DoctorUserRequest`
- **用途**: 创建和更新医生用户时接收请求数据

#### 字段说明

| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 否 | 医生用户ID（更新时必填） |
| username | String | 是 | 登录用户名 |
| password | String | 是 | 登录密码 |
| name | String | 是 | 医生真实姓名 |
| title | String | 否 | 职称 |
| department | String | 否 | 所属科室 |
| avatar | String | 否 | 头像图片URL |
| experience | String | 否 | 从业经验描述 |
| specialties | List&lt;String&gt; | 否 | 医生专长列表 |
| isActive | Boolean | 否 | 账户激活状态，默认为 true |

### DoctorUserResponse（响应DTO）

用于返回给客户端的数据传输对象。

#### 类定义
- **包路径**: `com.leansofx.qaserviceuser.dto.response.DoctorUserResponse`
- **用途**: 查询医生用户信息时返回数据

#### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 医生用户唯一标识符 |
| username | String | 登录用户名 |
| name | String | 医生真实姓名 |
| title | String | 职称 |
| department | String | 所属科室 |
| avatar | String | 头像图片URL |
| experience | String | 从业经验描述 |
| specialties | List&lt;String&gt; | 医生专长列表 |
| isActive | Boolean | 账户激活状态 |

**注意**: 响应DTO不包含 password 字段，确保敏感信息安全。

## 数据库表结构

### doctor_user 表

医生用户基本信息表。

```sql
CREATE TABLE doctor_user (
    id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    title VARCHAR(30),
    department VARCHAR(30),
    avatar TEXT,
    experience VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);
```

### doctor_specialties 表

医生专长关联表，存储医生的专长信息。

```sql
CREATE TABLE doctor_specialties (
    doctor_id VARCHAR(20) NOT NULL,
    specialty VARCHAR(255),
    PRIMARY KEY (doctor_id, specialty),
    FOREIGN KEY (doctor_id) REFERENCES doctor_user(id) ON DELETE CASCADE
);
```

#### 表关系说明
- `doctor_specialties.doctor_id` 外键关联到 `doctor_user.id`
- 采用一对多关系：一个医生可以有多个专长
- 级联删除：删除医生时自动删除其专长记录

## 数据模型关系图

```
┌─────────────────────┐
│   DoctorUser        │
│   (Entity)          │
├─────────────────────┤
│ - id: String        │◄─────────────┐
│ - username: String  │              │
│ - password: String  │              │
│ - name: String      │              │
│ - title: String     │              │
│ - department: String│              │
│ - avatar: String    │              │
│ - experience: String│              │
│ - specialties: List │──────────────┤
│ - isActive: Boolean │              │
└─────────────────────┘              │
                                     │
         ┌───────────────────────────┘
         │
         ▼
┌─────────────────────┐
│ doctor_specialties  │
│    (关联表)          │
├─────────────────────┤
│ doctor_id: String   │
│ specialty: String   │
└─────────────────────┘
```

## 数据流转示意

### 创建医生用户流程

```
Client Request
      │
      ▼
DoctorUserRequest (DTO)
      │
      ├─ username: "doctor001"
      ├─ password: "******"
      ├─ name: "张医生"
      ├─ title: "主任医师"
      ├─ department: "心内科"
      ├─ specialties: ["心血管疾病", "高血压"]
      └─ isActive: true
      │
      ▼
DoctorUser (Entity)
      │
      ├─ 保存到 doctor_user 表
      └─ 保存到 doctor_specialties 表
```

### 查询医生用户流程

```
Database Tables
      │
      ├─ doctor_user 表
      └─ doctor_specialties 表
      │
      ▼
DoctorUser (Entity)
      │
      ▼
DoctorUserResponse (DTO)
      │
      ├─ id: "D001"
      ├─ username: "doctor001"
      ├─ name: "张医生"
      ├─ title: "主任医师"
      ├─ department: "心内科"
      ├─ specialties: ["心血管疾病", "高血压"]
      └─ isActive: true
      │
      ▼
Client Response (不含 password)
```

## 数据验证规则

### 创建/更新时的字段验证

| 字段 | 验证规则 |
|------|---------|
| id | 可选；更新时必填；最大长度20 |
| username | 必填；最大长度50；全局唯一 |
| password | 必填；建议最小长度6位 |
| name | 必填；最大长度50 |
| title | 可选；最大长度30 |
| department | 可选；最大长度30 |
| avatar | 可选；TEXT类型可存储长字符串 |
| experience | 可选；最大长度50 |
| specialties | 可选；列表元素个数不限 |
| isActive | 可选；布尔值；默认true |

### 业务规则

1. **用户名唯一性**: username 字段在数据库层面设置为 UNIQUE，不允许重复
2. **密码安全**: password 字段应存储加密后的密码，不应明文存储
3. **软删除机制**: 通过 isActive 字段实现软删除，而非物理删除记录
4. **专长列表**: specialties 列表可以为空，表示该医生尚未设置专长信息

## 索引建议

### 主键索引
- `doctor_user.id`: 主键自动创建索引

### 唯一索引
- `doctor_user.username`: 唯一约束自动创建索引

### 查询优化索引
- `doctor_user.department`: 建议添加索引，用于按科室查询
- `doctor_user.is_active`: 建议添加索引，用于过滤激活/未激活账户

### 外键索引
- `doctor_specialties.doctor_id`: 外键自动创建索引

## 数据迁移建议

### 初始化数据
建议准备初始化SQL脚本，包含：
1. 创建表结构
2. 插入默认医生用户数据
3. 插入对应的专长数据

### 数据备份
定期备份策略：
1. `doctor_user` 表数据
2. `doctor_specialties` 表数据
3. 保持两个表数据的关联完整性

## 版本历史

| 版本 | 日期 | 变更说明 | 作者 |
|------|------|---------|------|
| 1.0.0 | 2025-03-22 | 初始版本，定义医生用户数据模型 | QA Healthcare Team |

## 相关文档

- [API接口文档](api.md)
- [项目结构文档](project-structure.md)
- [编码规范](coding-style.md)
