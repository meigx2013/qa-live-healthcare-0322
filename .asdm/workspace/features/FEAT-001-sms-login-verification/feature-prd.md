# 功能 PRD：登录增加短信验证功能

## 基本信息

| 字段 | 值 |
|------|-----|
| 功能 ID | FEAT-001 |
| 功能名称 | 登录增加短信验证功能 |
| 创建日期 | 2026-04-21 |
| 状态 | 规划中 |
| 优先级 | 高 |
| 作者 | AI Planner |

---

## 1. 功能概述

为医生登录流程增加短信验证码（SMS OTP）作为第二认证因子，提升系统安全性。当前医生登录仅使用用户名+密码进行纯前端校验，存在严重安全隐患。本功能将在密码验证通过后，增加短信验证码环节，实现双因子认证（2FA）。

---

## 2. 背景与动机

### 当前问题

1. **纯前端认证**：当前登录流程完全在前端完成，密码明文存储在 JSON 文件中并在浏览器端比对，任何用户均可通过开发者工具查看所有医生密码。
2. **无后端验证**：Spring Boot 后端没有登录认证 API，`DoctorUserController` 仅提供 CRUD 操作。
3. **无会话管理**：登录状态仅存储在 Vue 响应式变量中，页面刷新即丢失，无 JWT/Session 机制。
4. **明文密码**：数据库中密码为明文存储，无 BCrypt 等加密。
5. **无短信基础设施**：项目中不存在任何 SMS 相关代码。

### 业务价值

- 医疗系统涉及患者隐私数据，必须具备更高的安全标准
- 短信验证码可作为身份确认的有效手段，防止账号被盗用
- 符合医疗信息系统安全合规要求

---

## 3. 目标用户

- **医生用户**：需要通过短信验证码完成安全登录的主要用户群体
- **系统管理员**：需要管理医生手机号信息的管理人员

---

## 4. 功能需求

### 4.1 核心需求

| 编号 | 需求 | 描述 | 优先级 |
|------|------|------|--------|
| FR-01 | 医生手机号管理 | 在 `doctor_user` 表中新增 `phone` 字段，支持录入和更新医生手机号 | P0 |
| FR-02 | 短信验证码发送 | 在登录页面增加"发送验证码"按钮，向医生绑定手机号发送 6 位数字验证码 | P0 |
| FR-03 | 验证码校验 | 登录时先验证用户名密码，密码正确后要求输入短信验证码，验证通过才允许登录 | P0 |
| FR-04 | 验证码安全策略 | 验证码 5 分钟有效；60 秒内不可重复发送；同一手机号每日最多发送 10 次 | P0 |
| FR-05 | 后端认证 API | 新增后端登录认证接口，包括密码验证、验证码发送、验证码校验 | P0 |
| FR-06 | JWT Token 认证 | 登录成功后签发 JWT Token，前端存储并在后续请求中携带 | P1 |

### 4.2 非功能需求

| 编号 | 需求 | 描述 |
|------|------|------|
| NFR-01 | 验证码安全 | 验证码为 6 位随机数字，使用安全随机数生成；同一验证码不可重复使用 |
| NFR-02 | 防暴力破解 | 验证码错误 5 次后锁定该账号 15 分钟 |
| NFR-03 | 短信服务商 | 集成阿里云短信服务（Aliyun SMS）作为短信发送渠道 |
| NFR-04 | 可配置性 | 短信服务商配置、验证码有效期、发送频率限制等参数应可通过 application.yml 配置 |
| NFR-05 | 日志审计 | 记录验证码发送和验证的日志，包括手机号脱敏、发送时间、验证结果 |

---

## 5. 用户故事

### US-01：医生短信验证登录

**作为** 一名医生
**我希望** 在输入用户名和密码后，还需要输入手机短信验证码才能登录
**以便** 确保即使密码泄露，他人也无法登录我的账号

**验收标准**：
- 输入正确用户名和密码后，页面跳转到验证码输入界面
- 点击"发送验证码"后，绑定手机号收到 6 位数字验证码
- 输入正确验证码后登录成功，跳转至诊室页面
- 输入错误验证码后提示"验证码错误"，允许重新输入
- 验证码 5 分钟内有效，过期需重新发送

### US-02：验证码发送频率限制

**作为** 系统安全管理员
**我希望** 对短信验证码的发送频率进行限制
**以便** 防止短信轰炸和费用浪费

**验收标准**：
- 发送验证码后 60 秒内按钮置灰，显示倒计时
- 同一手机号每日最多发送 10 次验证码
- 超过限制后提示"今日验证码发送次数已达上限"

### US-03：医生手机号管理

**作为** 系统管理员
**我希望** 可以为医生账号绑定和更新手机号
**以便** 医生能够接收登录验证码

**验收标准**：
- `doctor_user` 表新增 `phone` 字段
- 创建和更新医生信息时可录入手机号
- 手机号格式校验（11 位中国大陆手机号）
- 手机号在 API 响应中脱敏显示（如 138****1234）

---

## 6. 技术方案

### 6.1 整体架构

```
┌──────────┐    1.用户名+密码     ┌──────────────────┐
│          │ ──────────────────► │                  │
│  前端     │    2.发送验证码请求   │  Spring Boot     │
│  Vue.js  │ ──────────────────► │  后端             │
│          │ ◄────────────────── │                  │
│          │    3.验证码输入       │  ┌────────────┐  │
│          │ ──────────────────► │  │ SMS Service │  │
│          │ ◄────────────────── │  │ (阿里云短信) │  │
│          │    4.JWT Token       │  └────────────┘  │
└──────────┘                     └──────────────────┘
                                         │
                                         ▼
                                  ┌──────────────┐
                                  │    MySQL      │
                                  │  验证码缓存    │
                                  │  + 用户数据    │
                                  └──────────────┘
```

### 6.2 数据库变更

#### `doctor_user` 表新增字段

```sql
ALTER TABLE doctor_user ADD COLUMN phone VARCHAR(20) COMMENT '手机号' AFTER password;
```

#### 新增 `sms_verification_code` 表

```sql
CREATE TABLE sms_verification_code (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    code VARCHAR(6) NOT NULL COMMENT '验证码',
    type VARCHAR(20) NOT NULL DEFAULT 'LOGIN' COMMENT '验证码类型',
    expired_at DATETIME NOT NULL COMMENT '过期时间',
    used_at DATETIME NULL COMMENT '使用时间',
    ip_address VARCHAR(50) NULL COMMENT '请求IP',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_phone_type (phone, type),
    INDEX idx_expired_at (expired_at)
);
```

### 6.3 后端 API 设计

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/auth/login` | 用户名+密码登录验证，验证通过返回临时凭证 |
| POST | `/api/auth/sms/send` | 发送短信验证码（需携带临时凭证） |
| POST | `/api/auth/sms/verify` | 验证短信验证码，通过后签发 JWT |
| GET | `/api/auth/me` | 获取当前登录用户信息（需 JWT） |
| POST | `/api/auth/logout` | 登出（需 JWT） |

#### API 详细设计

**POST /api/auth/login**

请求：
```json
{
  "username": "dr-zhang-wei",
  "password": "123456"
}
```

成功响应（200）：
```json
{
  "tempToken": "uuid-temp-token",
  "phone": "138****1234",
  "message": "密码验证通过，请输入短信验证码"
}
```

失败响应（401）：
```json
{
  "error": "INVALID_CREDENTIALS",
  "message": "用户名或密码错误"
}
```

**POST /api/auth/sms/send**

请求：
```json
{
  "tempToken": "uuid-temp-token"
}
```

成功响应（200）：
```json
{
  "message": "验证码已发送",
  "expiresIn": 300,
  "resendAfter": 60
}
```

频率限制响应（429）：
```json
{
  "error": "RATE_LIMITED",
  "message": "发送过于频繁，请60秒后重试",
  "retryAfter": 45
}
```

**POST /api/auth/sms/verify**

请求：
```json
{
  "tempToken": "uuid-temp-token",
  "code": "123456"
}
```

成功响应（200）：
```json
{
  "accessToken": "jwt-access-token",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "doctor": {
    "id": "D001",
    "username": "dr-zhang-wei",
    "name": "张伟",
    "title": "主任医师",
    "department": "内科"
  }
}
```

失败响应（400）：
```json
{
  "error": "INVALID_CODE",
  "message": "验证码错误",
  "attemptsRemaining": 4
}
```

锁定响应（423）：
```json
{
  "error": "ACCOUNT_LOCKED",
  "message": "验证码错误次数过多，账号已锁定15分钟",
  "lockedUntil": "2026-04-21T10:30:00Z"
}
```

### 6.4 前端变更

#### 登录流程改造

当前登录流程：
```
用户名+密码 → 前端比对 JSON → 登录成功/失败
```

改造后登录流程：
```
Step 1: 输入用户名+密码 → 调用后端 /api/auth/login
Step 2: 密码验证通过 → 显示验证码输入界面
Step 3: 点击发送验证码 → 调用 /api/auth/sms/send
Step 4: 输入验证码 → 调用 /api/auth/sms/verify
Step 5: 验证通过 → 获取 JWT → 存储 Token → 跳转诊室
```

#### DoctorLogin.vue 改造要点

1. 增加登录步骤状态管理（`currentStep`: `'credentials'` | `'verification'`）
2. 增加验证码输入表单（6 位数字输入框）
3. 增加"发送验证码"按钮（60 秒倒计时）
4. 增加手机号脱敏显示
5. 密码加密传输（BCrypt 或 HTTPS 保证）

#### Store 改造要点

1. 移除前端密码比对逻辑
2. 新增后端 API 调用方法（`loginStep1`, `sendSmsCode`, `verifySmsCode`）
3. JWT Token 存储（localStorage + 响应式状态）
4. 请求拦截器：自动在请求头中携带 JWT
5. 响应拦截器：401 时自动跳转登录页

### 6.5 后端模块设计

```
server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/
├── controller/
│   └── AuthController.java          # 认证 API 控制器
├── dto/
│   ├── LoginRequest.java            # 登录请求 DTO
│   ├── LoginResponse.java           # 登录响应 DTO
│   ├── SmsSendRequest.java          # 发送验证码请求 DTO
│   ├── SmsVerifyRequest.java        # 验证验证码请求 DTO
│   └── AuthTokenResponse.java       # 认证令牌响应 DTO
├── entity/
│   ├── DoctorUser.java              # (修改) 新增 phone 字段
│   └── SmsVerificationCode.java     # 验证码实体
├── repository/
│   └── SmsVerificationCodeRepository.java  # 验证码 Repository
├── service/
│   ├── AuthService.java             # 认证服务接口
│   ├── impl/
│   │   └── AuthServiceImpl.java     # 认证服务实现
│   └── sms/
│       ├── SmsService.java          # 短信服务接口
│       └── impl/
│           └── AliyunSmsServiceImpl.java  # 阿里云短信实现
├── security/
│   ├── JwtTokenProvider.java        # JWT 生成与验证
│   ├── JwtAuthFilter.java           # JWT 认证过滤器
│   └── SecurityConfig.java          # Spring Security 配置
└── config/
    └── SmsConfig.java               # 短信服务配置类
```

### 6.6 新增依赖

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<!-- 阿里云短信 SDK -->
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>dysmsapi20170525</artifactId>
    <version>3.1.0</version>
</dependency>
```

### 6.7 配置项

```yaml
# application.yml
app:
  auth:
    jwt:
      secret: ${JWT_SECRET:your-256-bit-secret-key-here}
      expiration: 86400  # 24小时，单位秒
    sms:
      provider: aliyun
      code-length: 6
      code-expiration: 300  # 5分钟，单位秒
      resend-interval: 60   # 60秒
      daily-limit: 10       # 每日上限
      max-attempts: 5       # 最大验证次数
      lock-duration: 900    # 锁定15分钟，单位秒
  aliyun:
    sms:
      access-key-id: ${ALIYUN_SMS_AK:}
      access-key-secret: ${ALIYUN_SMS_SK:}
      sign-name: ${ALIYUN_SMS_SIGN:医疗健康平台}
      template-code: ${ALIYUN_SMS_TEMPLATE:SMS_123456789}
```

---

## 7. UI 交互设计

### 登录页面改造

**Step 1 - 账号密码输入**（保持现有界面不变）

**Step 2 - 短信验证码输入**（新增）

```
┌──────────────────────────────┐
│        短信验证码验证          │
│                              │
│  验证码已发送至 138****1234    │
│                              │
│  ┌──┐ ┌──┐ ┌──┐ ┌──┐ ┌──┐ ┌──┐ │
│  │  │ │  │ │  │ │  │ │  │ │  │ │
│  └──┘ └──┘ └──┘ └──┘ └──┘ └──┘ │
│                              │
│  [发送验证码 (58s)]           │
│                              │
│  [    验证并登录    ]         │
│                              │
│  返回账号密码登录             │
└──────────────────────────────┘
```

---

## 8. 异常场景处理

| 场景 | 处理方式 |
|------|----------|
| 手机号未绑定 | 密码验证通过后提示"该账号未绑定手机号，请联系管理员"，提供管理员联系方式 |
| 短信发送失败 | 提示"验证码发送失败，请稍后重试"，允许重新发送 |
| 验证码过期 | 提示"验证码已过期，请重新发送" |
| 验证码错误 | 提示"验证码错误，还剩 N 次机会" |
| 账号锁定 | 提示"验证码错误次数过多，账号已锁定，请 N 分钟后重试" |
| 手机号格式错误 | 前端实时校验，提示"请输入正确的手机号" |
| JWT 过期 | 自动跳转至登录页，提示"登录已过期，请重新登录" |
| 临时 Token 过期 | 返回 Step 1，提示"操作超时，请重新登录" |

---

## 9. 安全考虑

1. **密码加密存储**：后端密码使用 BCrypt 加密存储，登录验证使用 BCrypt 比对
2. **验证码安全**：使用 `SecureRandom` 生成验证码；验证码使用后立即标记为已使用
3. **临时 Token**：密码验证通过后颁发的临时 Token 有效期 10 分钟，仅用于验证码流程
4. **HTTPS**：生产环境必须启用 HTTPS，确保传输安全
5. **日志脱敏**：所有日志中手机号必须脱敏处理
6. **SQL 注入防护**：使用 JPA 参数化查询
7. **验证码不回传**：API 响应中不包含验证码明文

---

## 10. 实施范围与边界

### 本期实施范围

- 医生登录增加短信验证码（双因子认证）
- 后端认证 API（登录、发送验证码、验证验证码）
- JWT Token 签发与验证
- 医生手机号管理
- 阿里云短信服务集成
- BCrypt 密码加密

### 不在本期范围

- 患者端短信验证（患者当前无密码认证体系）
- 短信验证码用于其他场景（注册、修改密码等）
- 短信服务商多通道支持
- OAuth2 / 第三方登录
- 密码找回功能
- IP 风控与设备指纹

---

## 11. 验收标准

1. 医生输入正确用户名+密码后，必须输入短信验证码才能登录
2. 验证码发送到医生绑定手机号，5 分钟内有效
3. 60 秒内不可重复发送验证码
4. 同一手机号每日最多发送 10 次验证码
5. 验证码错误 5 次后锁定账号 15 分钟
6. 登录成功后签发 JWT Token，后续 API 请求携带 Token
7. Token 过期后自动跳转登录页
8. 密码在数据库中 BCrypt 加密存储
9. 手机号在 API 响应中脱敏显示
10. 前端登录页面支持两步验证流程，UI 符合现有设计风格

---

## 12. 任务分解建议

本功能建议分解为以下任务类别：

| 类别 | 任务 | 优先级 |
|------|------|--------|
| 数据库 | 新增 phone 字段、sms_verification_code 表 | P0 |
| 后端 - 认证核心 | AuthController + AuthService 实现 | P0 |
| 后端 - 短信服务 | SmsService 接口 + AliyunSmsServiceImpl | P0 |
| 后端 - JWT | JwtTokenProvider + JwtAuthFilter + SecurityConfig | P1 |
| 后端 - 密码加密 | BCrypt 集成 + 数据迁移 | P0 |
| 前端 - 登录页面 | DoctorLogin.vue 两步验证 UI | P0 |
| 前端 - Store | API 调用、Token 管理、拦截器 | P0 |
| 前端 - 路由守卫 | Router beforeEach 鉴权 | P1 |
| 测试 | 后端单元测试 + 集成测试 + 前端 E2E 测试 | P1 |
