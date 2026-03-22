# QA Service User API 文档

## 概述

QA Service User 是医疗问答系统的用户管理服务，基于 Spring Boot 3.5.7 构建。

**服务信息：**
- 服务名称：qa-service-user
- 版本：0.0.1-SNAPSHOT
- 端口：8080
- 基础路径：http://localhost:8080

## API 端点列表

### DoctorUserController

**文件位置：** [../src/main/java/com/leansofx/qaserviceuser/controller/DoctorUserController.java]

医生用户管理控制器，提供医生信息的增删改查功能。

| 方法 | 端点 | 描述 | 参数 | 请求体 | 响应 |
|--------|----------|-------------|------------|--------------|----------|
| GET | `/api/doctors` | 获取所有医生列表 | 无 | 无 | List<DoctorUserResponse> |
| GET | `/api/doctors/{id}` | 根据ID获取医生信息 | id (路径参数) | 无 | DoctorUserResponse |
| GET | `/api/doctors/username/{username}` | 根据用户名获取医生信息 | username (路径参数) | 无 | DoctorUserResponse |
| GET | `/api/doctors/active` | 获取所有激活的医生列表 | 无 | 无 | List<DoctorUserResponse> |
| POST | `/api/doctors` | 创建医生 | 无 | DoctorUserRequest | DoctorUserResponse |
| PUT | `/api/doctors/{id}` | 更新医生信息 | id (路径参数) | DoctorUserRequest | DoctorUserResponse |
| DELETE | `/api/doctors/{id}` | 删除医生 | id (路径参数) | 无 | 无内容 |

#### 数据结构示例

**DoctorUserRequest（医生创建/更新请求）**
```json
{
  "id": "doctor001",
  "username": "dr_zhang",
  "password": "securePassword123",
  "name": "张医生",
  "title": "主任医师",
  "department": "内科",
  "avatar": "https://example.com/avatars/dr_zhang.jpg",
  "experience": "20年临床经验",
  "specialties": ["心血管疾病", "高血压", "糖尿病"],
  "isActive": true
}
```

**DoctorUserResponse（医生信息响应）**
```json
{
  "id": "doctor001",
  "username": "dr_zhang",
  "name": "张医生",
  "title": "主任医师",
  "department": "内科",
  "avatar": "https://example.com/avatars/dr_zhang.jpg",
  "experience": "20年临床经验",
  "specialties": ["心血管疾病", "高血压", "糖尿病"],
  "isActive": true
}
```

**医生列表响应示例（List<DoctorUserResponse>）**
```json
[
  {
    "id": "doctor001",
    "username": "dr_zhang",
    "name": "张医生",
    "title": "主任医师",
    "department": "内科",
    "avatar": "https://example.com/avatars/dr_zhang.jpg",
    "experience": "20年临床经验",
    "specialties": ["心血管疾病", "高血压", "糖尿病"],
    "isActive": true
  },
  {
    "id": "doctor002",
    "username": "dr_li",
    "name": "李医生",
    "title": "副主任医师",
    "department": "外科",
    "avatar": "https://example.com/avatars/dr_li.jpg",
    "experience": "15年临床经验",
    "specialties": ["骨科手术", "创伤治疗"],
    "isActive": true
  }
]
```

### TestController

**文件位置：** [../src/main/java/com/leansofx/qaserviceuser/controller/TestController.java](../src/main/java/com/leansofx/qaserviceuser/controller/TestController.java)

测试控制器，主要用于 CORS 配置测试和服务健康检查。

| 方法 | 端点 | 描述 | 参数 | 请求体 | 响应 |
|--------|----------|-------------|------------|--------------|----------|
| GET | `/api/test/cors` | 测试 CORS 配置（GET 请求） | 无 | 无 | TestResponse |
| POST | `/api/test/cors` | 测试 CORS 配置（POST 请求） | 无 | Map<String, Object>（可选） | TestResponse |
| OPTIONS | `/api/test/cors` | 处理 CORS 预检请求 | 无 | 无 | 无内容 |

#### 数据结构示例

**TestResponse（测试响应）**
```json
{
  "message": "CORS configuration is working!",
  "timestamp": 1699000000000,
  "service": "qa-service-user",
  "receivedData": {
    "key": "value"
  }
}
```

**请求体示例（POST /api/test/cors）**
```json
{
  "testData": "example",
  "userId": 123,
  "action": "test"
}
```

## Spring Boot Actuator 端点

项目集成了 Spring Boot Actuator，提供了以下监控和管理端点：

| 方法 | 端点 | 描述 | 参数 | 请求体 | 响应 |
|--------|----------|-------------|------------|--------------|----------|
| GET | `/actuator/health` | 应用健康检查 | 无 | 无 | HealthResponse |
| GET | `/actuator/info` | 应用信息 | 无 | 无 | InfoResponse |
| GET | `/actuator/metrics` | 应用指标 | 无 | 无 | MetricsResponse |
| GET | `/actuator/env` | 环境信息 | 无 | 无 | EnvironmentResponse |
| GET | `/actuator/beans` | Spring Bean 信息 | 无 | 无 | BeansResponse |
| GET | `/actuator/loggers` | 日志配置信息 | 无 | 无 | LoggersResponse |

#### Actuator 数据结构示例

**HealthResponse（健康检查响应）**
```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 123456789012,
        "threshold": 10485760,
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

**InfoResponse（应用信息响应）**
```json
{
  "app": {
    "name": "qa-service-user",
    "description": "QA Service User - Healthcare QA System User Management Service",
    "version": "0.0.1-SNAPSHOT",
    "encoding": "UTF-8",
    "java": {
      "version": "17"
    }
  },
  "team": "QA Healthcare Team",
  "environment": "development",
  "build": {
    "timestamp": "2025-11-03"
  },
  "features": "CORS,Actuator,Health Checks,User Management",
  "java": {
    "version": "17.0.x",
    "vendor": "Eclipse Adoptium"
  },
  "os": {
    "name": "Linux",
    "version": "x.x.x",
    "arch": "amd64"
  }
}
```

## CORS 配置

服务已配置跨域资源共享（CORS），支持以下配置：

- **允许的源**：`*`（所有源）
- **允许的方法**：`GET, POST, PUT, DELETE, OPTIONS`
- **允许的头部**：`*`（所有头部）
- **允许凭证**：`false`
- **最大缓存时间**：`3600` 秒

## 错误响应格式

当 API 调用出现错误时，服务会返回标准的错误响应格式：

```json
{
  "timestamp": "2025-11-03T10:15:30.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "详细错误信息",
  "path": "/api/test/cors"
}
```

## 使用示例

### 医生管理接口示例

**获取所有医生列表：**
```bash
curl -X GET http://localhost:8080/api/doctors
```

**根据ID获取医生信息：**
```bash
curl -X GET http://localhost:8080/api/doctors/doctor001
```

**根据用户名获取医生信息：**
```bash
curl -X GET http://localhost:8080/api/doctors/username/dr_zhang
```

**获取所有激活的医生列表：**
```bash
curl -X GET http://localhost:8080/api/doctors/active
```

**创建医生：**
```bash
curl -X POST http://localhost:8080/api/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "doctor003",
    "username": "dr_wang",
    "password": "securePassword456",
    "name": "王医生",
    "title": "主治医师",
    "department": "儿科",
    "avatar": "https://example.com/avatars/dr_wang.jpg",
    "experience": "10年临床经验",
    "specialties": ["儿童常见病", "新生儿护理"],
    "isActive": true
  }'
```

**更新医生信息：**
```bash
curl -X PUT http://localhost:8080/api/doctors/doctor003 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "王医生",
    "title": "副主任医师",
    "department": "儿科",
    "experience": "12年临床经验",
    "specialties": ["儿童常见病", "新生儿护理", "儿童保健"],
    "isActive": true
  }'
```

**删除医生：**
```bash
curl -X DELETE http://localhost:8080/api/doctors/doctor003
```

### 测试 CORS 配置

**GET 请求示例：**
```bash
curl -X GET http://localhost:8080/api/test/cors
```

**POST 请求示例：**
```bash
curl -X POST http://localhost:8080/api/test/cors \
  -H "Content-Type: application/json" \
  -d '{"testData": "example", "userId": 123}'
```

**健康检查示例：**
```bash
curl -X GET http://localhost:8080/actuator/health
```

## 注意事项

1. 医生管理接口提供完整的CRUD功能
2. 所有 API 端点都支持 CORS
3. Actuator 端点提供了丰富的监控和管理功能
4. 建议在生产环境中限制 CORS 配置和 Actuator 端点的访问权限
5. 创建和更新医生信息时，密码字段建议进行加密处理
6. DoctorUserResponse 中不包含密码字段，确保敏感信息安全

## 版本历史

- **v0.0.1-SNAPSHOT**：
  - 初始版本，包含基础的 CORS 测试功能和 Actuator 监控
  - 新增医生用户管理功能，提供完整的 CRUD 接口
  - 支持按ID、用户名查询医生信息
  - 支持查询激活状态的医生列表