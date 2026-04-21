# Feature PRD: 登录增加短信验证功能

**Feature ID**: FEAT-001-sms-login-verification
**Created Date**: 2026-04-21
**Status**: PLANNED
**Language**: zh-CN

## 1. Overview

### 1.1 Feature Summary

在现有医生用户登录流程中增加短信验证码功能。当前系统的登录认证完全在前端客户端完成（通过硬编码 JSON 文件比对用户名密码），没有任何后端认证机制。本功能将：

- **实现后端登录认证接口**：替代现有的前端客户端验证方式
- **增加短信验证码作为第二认证因子**：用户输入用户名密码后，还需输入手机短信验证码才能完成登录
- **对接短信服务**：集成短信发送服务，实现验证码的发送和校验

### 1.2 Objectives

- **目标 1**：建立后端登录认证接口，将认证逻辑从前端迁移到后端，消除客户端硬编码密码的安全隐患
- **目标 2**：实现短信验证码的发送与校验机制，为登录提供双因素认证保护
- **目标 3**：改造前端登录页面，支持短信验证码输入交互流程
- **目标 4**：建立基于 Token 的会话管理机制，替代当前纯前端内存状态管理

## 2. User Stories

### Story 1：医生用户通过短信验证码安全登录

**As a** 医生用户
**I want to** 在输入用户名密码后，通过手机短信接收验证码并输入以完成登录
**So that** 我的账号可以得到更安全的保护，防止密码泄露导致的未授权访问

**Acceptance Criteria**:
- 用户在登录页面输入用户名和密码后，点击"发送验证码"按钮
- 系统向该用户绑定的手机号发送 6 位数字验证码
- 验证码在 60 秒内有效，过期需重新发送
- 用户输入正确的验证码后，成功登录并跳转到医生工作台
- 验证码错误时提示"验证码错误"，允许重新输入（最多 5 次）
- 发送验证码后有 60 秒冷却时间，防止频繁发送

### Story 2：管理员配置短信服务

**As a** 系统管理员
**I want to** 在后台配置短信服务的相关参数
**So that** 系统可以正常发送短信验证码，且配置变更无需重启服务

**Acceptance Criteria**:
- 短信服务的 API Key、模板 ID 等参数通过配置文件管理
- 支持切换短信服务商（至少预留接口）
- 配置项在 application.properties 中可修改

## 3. Functional Requirements

### Requirement 1：后端登录认证接口
- **ID**: REQ-001
- **Description**: 提供后端 REST API 实现用户名密码验证，替代前端硬编码验证。包括登录请求接口和 Token 签发机制。
- **Priority**: High
- **Related Stories**: Story 1

### Requirement 2：短信验证码发送接口
- **ID**: REQ-002
- **Description**: 提供后端 REST API 接收手机号或用户标识，生成 6 位随机验证码，调用短信服务发送，并将验证码存储到 Redis 或内存缓存中（关联手机号，设置 60 秒 TTL）。
- **Priority**: High
- **Related Stories**: Story 1, Story 2

### Requirement 3：短信验证码校验与登录完成接口
- **ID**: REQ-003
- **Description**: 提供后端 REST API 校验用户输入的验证码是否与发送的验证码匹配，校验通过后签发 JWT Token 并返回用户信息，完成登录流程。
- **Priority**: High
- **Related Stories**: Story 1

### Requirement 4：短信服务集成
- **ID**: REQ-004
- **Description**: 集成短信服务商 SDK（如阿里云短信、腾讯云短信），实现验证码短信的发送功能。通过策略模式设计，支持切换短信服务商。
- **Priority**: High
- **Related Stories**: Story 2

### Requirement 5：前端登录页面改造
- **ID**: REQ-005
- **Description**: 改造 DoctorLogin.vue 登录页面，增加短信验证码输入步骤。登录流程变为：Step1 输入用户名密码 → Step2 输入短信验证码 → 登录成功。包含发送验证码按钮及 60 秒倒计时、验证码输入框、错误提示等交互。
- **Priority**: High
- **Related Stories**: Story 1

### Requirement 6：前端会话管理改造
- **ID**: REQ-006
- **Description**: 改造前端 Store，将认证状态从内存管理改为基于 JWT Token 的持久化管理。Token 存储在 localStorage 中，请求时通过 Authorization Header 携带 Token。实现路由守卫保护需登录的页面。
- **Priority**: Medium
- **Related Stories**: Story 1

### Requirement 7：手机号字段扩展
- **ID**: REQ-007
- **Description**: 在 `doctor_user` 表中增加 `phone` 字段（VARCHAR(20)），用于存储医生手机号。更新 Entity、DTO、数据库 Schema 及种子数据。
- **Priority**: High
- **Related Stories**: Story 1

### Requirement 8：验证码安全策略
- **ID**: REQ-008
- **Description**: 实现验证码安全策略：每日同一手机号发送上限（10 次/天）、同一 IP 发送频率限制、验证码错误次数限制（5 次后需重新发送）、验证码 60 秒有效期、60 秒发送冷却时间。
- **Priority**: Medium
- **Related Stories**: Story 1

## 4. Non-Functional Requirements

### 4.1 Performance
- 短信验证码发送接口响应时间 < 2 秒（不含短信服务商耗时）
- 登录接口响应时间 < 500ms
- 验证码校验接口响应时间 < 200ms

### 4.2 Security
- 验证码不得在日志中明文记录
- JWT Token 设置合理过期时间（建议 2 小时），支持刷新机制
- 密码在数据库中必须使用 BCrypt 加密存储（本功能实现时一并整改）
- 验证码在内存/缓存中加密存储或使用单向哈希
- 登录失败不得泄露用户是否存在的信息（统一错误提示）
- 接口需防护暴力破解（限流、锁定机制）

### 4.3 Scalability
- 短信服务采用策略模式，方便未来切换服务商
- 验证码存储抽象为接口，支持从内存缓存迁移到 Redis

### 4.4 Reliability
- 短信服务调用失败时返回明确错误信息，不阻塞其他流程
- 验证码服务降级时支持密码登录作为备选（可配置开关）

## 5. Technical Requirements

### 5.1 Architecture Considerations
- 后端采用 Spring Boot 现有架构，在 `qa-service-user` 模块中新增认证相关代码
- 新增 `auth` 包，包含 Controller、Service、DTO，与现有 `DoctorUser` CRUD 逻辑解耦
- 短信服务采用策略模式 + Spring 配置切换实现
- 验证码存储先使用 `ConcurrentHashMap` + 定时清理实现，预留 Redis 接口
- JWT 使用 `jjwt` 库实现 Token 签发和验证

### 5.2 Dependencies
- `spring-boot-starter-security`（新增）- 用于密码加密和基础安全配置
- `jjwt-api` / `jjwt-impl` / `jjwt-jackson`（新增）- JWT Token 处理
- 短信服务商 SDK（如 `aliyun-java-sdk-dysmsapi`）（新增）- 短信发送
- Vue.js 前端无需新增核心依赖，使用 Ant Design Vue 现有组件

### 5.3 Constraints
- 当前项目未引入 Redis，验证码缓存暂用内存实现（生产环境需迁移到 Redis）
- 当前 `doctor_user` 表无 `phone` 字段，需执行 DDL 变更
- 当前密码为明文存储，需在本功能中一并整改为 BCrypt 加密
- 短信服务商选择需根据实际采购情况确定，PRD 中以阿里云短信为默认方案

## 6. Success Criteria

- 登录流程必须经过后端认证，前端不再包含任何硬编码密码或客户端验证逻辑
- 用户输入用户名密码 + 短信验证码可成功登录，跳转到医生工作台
- 验证码在 60 秒内有效，过期自动失效
- 同一手机号 60 秒内不可重复发送验证码
- 验证码错误 5 次后验证码失效，需重新发送
- 密码在数据库中为 BCrypt 加密存储
- JWT Token 机制可维持登录状态，页面刷新后不丢失
- 未登录访问受保护页面自动跳转到登录页

## 7. Task Breakdown Principles

### 7.1 Granularity
- 每个任务应限制为人类开发者 1-2 小时的工作量
- 任务应聚焦且可管理

### 7.2 Independence
- 尽量减少任务之间的依赖
- 在可能的情况下启用并行执行

### 7.3 Testability
- 每个任务应有明确的验收标准
- 确保任务可以独立验证

### 7.4 Task Categories
任务可分为以下类别：
- 分析与设计
- 代码实现
- 测试（单元）

### 7.5 Task Count Limitation
- 本功能理想情况下应不超过 10 个任务
- 如超过 10 个任务，应分解为更小的子功能

## 8. Implementation Notes

- **数据库变更**：需新增 `phone` 字段到 `doctor_user` 表，并更新种子数据。需新增迁移 SQL 脚本。同时需将现有明文密码更新为 BCrypt 哈希值。
- **短信服务商选择**：PRD 以阿里云短信为默认方案，但架构上需支持切换。开发阶段可使用 Mock 短信服务（验证码输出到日志）。
- **JWT 密钥管理**：JWT 签名密钥应通过配置文件管理，不硬编码在代码中。
- **验证码存储**：初期使用内存 ConcurrentHashMap，需抽象为接口便于后续迁移到 Redis。
- **前端流程变更**：登录从单步变为两步，需注意用户体验，验证码输入步骤应允许返回上一步。
- **向后兼容**：新增短信验证为强制功能，不做开关控制（除非降级场景）。

## 9. Risks and Mitigations

### Risk 1：短信服务商延迟或不可用
- **Description**: 短信服务可能出现发送延迟或服务不可用
- **Impact**: High
- **Mitigation**: 设计降级机制，短信服务不可用时可配置允许仅密码登录；开发阶段使用 Mock 服务避免依赖外部服务

### Risk 2：验证码被拦截或泄露
- **Description**: 验证码可能在传输过程中被拦截
- **Impact**: Medium
- **Mitigation**: 验证码有效期限制为 60 秒；限制发送频率和每日上限；不在日志中明文记录验证码

### Risk 3：数据库密码明文整改影响现有数据
- **Description**: 将明文密码迁移为 BCrypt 哈希可能影响现有测试数据
- **Impact**: Medium
- **Mitigation**: 提供密码迁移脚本，一次性更新所有现有用户密码；确保种子数据同步更新

### Risk 4：前端登录流程变更导致用户体验下降
- **Description**: 增加验证码步骤使登录流程变长
- **Impact**: Low
- **Mitigation**: 优化交互设计，验证码输入步骤简洁明了；提供倒计时和重发按钮；允许返回上一步修改用户名密码

## 10. Appendix

### 10.1 References
- 项目上下文索引：`.asdm/contexts/qa-live-healthcare项目结构分析_1/.asdm/contexts/index.md`
- 现有登录页：`web/qa-web/src/views/DoctorLogin.vue`
- 现有前端 Store：`web/qa-web/src/store/index.ts`
- 现有后端控制器：`server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/DoctorUserController.java`
- 数据库 Schema：`server/qa-service-user/mysql/create-tables.sql`

### 10.2 Glossary
- **SMS Verification Code**: 短信验证码，通过手机短信发送的一次性数字验证码
- **JWT (JSON Web Token)**: 基于JSON的开放标准令牌，用于在各方之间安全地传输信息
- **BCrypt**: 一种自适应哈希算法，常用于密码加密存储
- **2FA (Two-Factor Authentication)**: 双因素认证，需要两种不同的认证因素来验证用户身份
- **TTL (Time To Live)**: 生存时间，数据在缓存中的有效期
