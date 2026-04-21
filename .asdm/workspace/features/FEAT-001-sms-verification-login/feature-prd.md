# Feature PRD: 登录增加短信验证码功能

**Feature ID**: FEAT-001-sms-verification-login
**Created Date**: 2026-04-21
**Status**: PLANNED
**Language**: zh

## 1. Overview

### 1.1 Feature Summary

为医生登录流程增加短信验证码（SMS OTP）作为二次验证因子。当前医生登录仅使用用户名+密码的单一认证方式，安全性较低。增加短信验证码后，医生在输入用户名和密码后，需要输入手机收到的6位数字验证码才能完成登录，形成双因素认证（2FA），提升系统安全性。

- **功能**：在现有用户名+密码登录流程中，增加短信验证码发送和验证步骤
- **必要性**：医疗系统涉及敏感患者数据，单因素认证存在安全风险，需要双因素认证保障
- **受益者**：医生（账号安全性提升）、患者（数据安全保障）、平台运营方（合规要求）

### 1.2 Objectives

- Objective 1: 实现短信验证码的发送和验证功能，作为医生登录的二次验证
- Objective 2: 在后端建立验证码生成、存储、过期和验证的完整机制
- Objective 3: 在前端登录页面增加验证码输入交互流程
- Objective 4: 为 DoctorUser 实体增加手机号字段，支持验证码发送目标
- Objective 5: 保持前端可独立运行（mock 模式）的能力

## 2. User Stories

### Story 1: 医生使用短信验证码登录

**As a** 医生
**I want to** 在输入用户名和密码后，通过手机短信接收验证码并输入完成登录
**So that** 我的账号得到双重保护，即使密码泄露也不会被他人登录

**Acceptance Criteria**:
- 医生输入正确的用户名和密码后，系统显示验证码输入界面
- 系统向医生绑定的手机号发送6位数字验证码
- 医生在60秒内输入正确的验证码后成功登录
- 验证码超过5分钟未使用自动失效
- 同一手机号60秒内只能请求一次验证码

### Story 2: 医生绑定手机号

**As a** 医生
**I want to** 在我的账号中绑定手机号
**So that** 我可以接收登录验证短信

**Acceptance Criteria**:
- DoctorUser 实体新增 phone 字段
- 已有医生数据需补充手机号信息
- 手机号格式校验（中国大陆手机号格式）

### Story 3: 验证码发送频率限制

**As a** 系统管理员
**I want to** 限制验证码发送频率
**So that** 防止恶意刷短信和短信轰炸攻击

**Acceptance Criteria**:
- 同一手机号60秒内只能发送1次验证码
- 同一手机号每天最多发送10次验证码
- 同一IP每天最多发送20次验证码

## 3. Functional Requirements

### Requirement 1
- **ID**: REQ-001
- **Description**: DoctorUser 实体增加 phone 字段，支持存储和查询医生手机号
- **Priority**: High
- **Related Stories**: Story 2

### Requirement 2
- **ID**: REQ-002
- **Description**: 后端实现验证码生成接口 `POST /api/sms/send`，接收手机号参数，生成6位随机数字验证码，存储到服务端并设置5分钟过期时间，调用短信服务发送验证码
- **Priority**: High
- **Related Stories**: Story 1

### Requirement 3
- **ID**: REQ-003
- **Description**: 后端实现验证码验证接口 `POST /api/sms/verify`，接收手机号和验证码参数，验证验证码是否正确且未过期
- **Priority**: High
- **Related Stories**: Story 1

### Requirement 4
- **ID**: REQ-004
- **Description**: 后端实现医生登录接口 `POST /api/auth/login`，先验证用户名密码，再验证短信验证码，两步验证均通过后返回登录成功
- **Priority**: High
- **Related Stories**: Story 1

### Requirement 5
- **ID**: REQ-005
- **Description**: 后端实现验证码发送频率限制，同一手机号60秒间隔限制、每日10次上限，同一IP每日20次上限
- **Priority**: High
- **Related Stories**: Story 3

### Requirement 6
- **ID**: REQ-006
- **Description**: 前端 DoctorLogin.vue 增加短信验证码交互流程：密码验证通过后显示验证码输入区域，包含发送验证码按钮和验证码输入框，发送按钮带60秒倒计时
- **Priority**: High
- **Related Stories**: Story 1

### Requirement 7
- **ID**: REQ-007
- **Description**: 前端 store 增加短信验证码相关状态和方法，支持 mock 模式下独立运行（验证码固定为 123456）
- **Priority**: Medium
- **Related Stories**: Story 1

### Requirement 8
- **ID**: REQ-008
- **Description**: 数据库迁移脚本：doctor_user 表增加 phone 字段，更新已有种子数据补充手机号
- **Priority**: High
- **Related Stories**: Story 2

## 4. Non-Functional Requirements

### 4.1 Performance
- 验证码发送接口响应时间 < 2秒（不含第三方短信服务耗时）
- 验证码验证接口响应时间 < 500ms
- 验证码存储使用内存缓存（如 ConcurrentHashMap），避免数据库频繁读写

### 4.2 Security
- 验证码必须为6位随机数字，不可预测
- 验证码验证后立即失效，不可重复使用
- 验证码在服务端存储时不得明文记录到日志
- 手机号传输和存储需脱敏处理（日志中显示 138****1234 格式）
- 登录失败不提示具体是用户名、密码还是验证码错误，统一提示"登录信息错误"
- 同一账号连续5次登录失败，锁定账号15分钟

### 4.3 Scalability
- 验证码存储机制应便于后续迁移到 Redis 等分布式缓存
- 短信发送服务应抽象为接口，便于切换不同短信服务商

### 4.4 Reliability
- 短信服务调用失败时返回明确错误信息，支持用户重新发送
- 验证码过期后用户可重新请求发送

## 5. Technical Requirements

### 5.1 Architecture Considerations
- 后端新增短信验证码相关代码放在 `qa-service-user` 服务中，与用户管理同属一个微服务
- 验证码存储当前使用内存（ConcurrentHashMap），预留 Redis 迁移接口
- 短信发送服务抽象为 `SmsService` 接口，当前实现为日志打印（开发阶段），便于后续对接真实短信服务商
- 前端登录流程改为两步：第一步验证用户名密码，第二步验证短信验证码

### 5.2 Dependencies
- Spring Boot 内置缓存机制（当前无需引入 Redis）
- 前端 Ant Design Vue 的 Countdown 组件（倒计时功能）
- 短信服务商 SDK（当前阶段使用日志模拟，不引入真实 SDK）

### 5.3 Constraints
- 前端必须保持 mock 模式独立运行能力，不依赖后端也可完成登录流程演示
- 数据库变更需提供增量 SQL 脚本，不破坏现有数据
- 兼容现有 CORS 配置

## 6. Success Criteria

- 医生可通过用户名+密码+短信验证码三步完成登录
- 验证码发送后在5分钟内有效，验证后立即失效
- 同一手机号60秒内不可重复发送验证码
- 前端 mock 模式下可使用固定验证码 123456 完成登录
- 后端验证码相关接口通过单元测试
- 前端登录页面交互流畅，发送按钮倒计时正常

## 7. Task Breakdown Principles

### 7.1 Granularity
- 每个任务应控制在人工 1-2 小时工作量（AI 模型约 5-10 分钟）
- 任务应聚焦单一职责，便于独立验证

### 7.2 Independence
- 尽量减少任务间依赖，支持并行执行
- 后端任务和前端任务可并行开发

### 7.3 Testability
- 每个任务有明确的验收标准
- 后端任务需包含单元测试

### 7.4 Task Categories
- 数据模型变更（数据库+实体）
- 后端接口实现（验证码生成/验证/登录）
- 前端交互实现（登录页面改造）
- 测试

### 7.5 Task Count Limitation
- 预计任务数量：8-10 个，符合10个以内的限制

## 8. Implementation Notes

- 短信服务当前阶段仅打印日志模拟发送，不对接真实短信网关，避免产生费用和依赖外部服务
- 验证码存储使用 `ConcurrentHashMap<String, VerificationCode>` 实现，VerificationCode 包含 code、phone、createTime、expireTime 字段
- 前端登录流程交互设计：密码验证成功后，页面平滑过渡到验证码输入界面，显示绑定的手机号（脱敏），用户点击发送验证码，输入6位数字完成验证
- DoctorUser 实体增加 phone 字段后，需同步更新 DoctorUserRequest/DoctorUserResponse DTO
- 前端 Doctor 接口和 mock 数据需同步增加 phone 字段

## 9. Risks and Mitigations

### Risk 1
- **Description**: 短信服务对接可能产生额外费用
- **Impact**: Medium
- **Mitigation**: 当前阶段使用日志模拟发送，仅在日志中输出验证码内容供开发调试；生产环境对接时再做短信服务商选型

### Risk 2
- **Description**: 内存存储验证码在服务重启后丢失
- **Impact**: Low
- **Mitigation**: 服务重启频率低，验证码有效期仅5分钟，影响范围有限；后续可迁移至 Redis

### Risk 3
- **Description**: 前端 mock 模式与后端接口模式行为不一致
- **Impact**: Medium
- **Mitigation**: 前端 store 中通过环境判断切换 mock/real 模式，mock 模式固定验证码 123456，确保两端交互流程一致

### Risk 4
- **Description**: 验证码接口可能被恶意攻击（短信轰炸）
- **Impact**: High
- **Mitigation**: 实施频率限制（手机号级别和 IP 级别），验证码发送前需先通过用户名密码验证，增加图形验证码作为后续增强

## 10. Appendix

### 10.1 References
- 项目架构文档：`server/qa-service-user/docs/arch.md`
- 前端编码规范：`web/qa-web/docs/coding-style.md`
- 后端编码规范：`server/qa-service-user/docs/coding-style.md`
- 数据模型文档：`server/qa-service-user/docs/data-models.md`
- API 文档：`server/qa-service-user/docs/api.md`

### 10.2 Glossary
- SMS OTP: Short Message Service One-Time Password，短信一次性密码
- 2FA: Two-Factor Authentication，双因素认证
- Mock 模式: 前端不依赖后端，使用本地 JSON 数据模拟的运行模式
