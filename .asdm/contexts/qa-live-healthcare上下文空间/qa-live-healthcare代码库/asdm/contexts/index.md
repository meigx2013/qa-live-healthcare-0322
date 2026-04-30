# 工作空间上下文索引

## 概述

本文档是 AI 模型理解和使用当前工作空间的索引与指南，提供工作空间内容的结构化概览，帮助 AI 模型快速定位相关上下文。

## 工作空间信息

### 基本信息
- **工作空间名称**: QA Healthcare（医疗问答系统）
- **描述**: 基于微服务架构的医疗问答咨询平台，提供用户管理、问题管理和统计分析等核心功能
- **创建日期**: 2026年
- **最后更新**: 2026-04-30

### 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| **后端框架** | Spring Boot | 3.5.7 |
| **后端语言** | Java (OpenJDK Temurin) | 17 |
| **后端构建** | Maven (Maven Wrapper) | 3.x |
| **ORM** | Spring Data JPA | Spring Boot 管理 |
| **数据库** | MySQL | 8.4 |
| **连接池** | HikariCP | Spring Boot 默认 |
| **监控** | Spring Boot Actuator | Spring Boot 管理 |
| **前端框架** | Vue.js | ^3.5.10 |
| **前端语言** | TypeScript | ^5.5.3 |
| **前端构建** | Vite | ^5.4.8 |
| **UI 组件库** | Ant Design Vue | ^4.2.6 |
| **路由** | Vue Router | ^4.6.3 |
| **国际化** | vue-i18n | ^9.14.5 |
| **容器化** | Docker + Docker Compose | - |
| **并发管理** | concurrently | ^8.2.2 |

### 业务上下文
- **业务领域**: 医疗健康 — 在线问诊咨询
- **核心业务流程**:
  1. 患者注册/验证 → 选择医生 → 提交问诊问题
  2. 医生登录 → 查看待处理问题 → 回复/标记已回答
  3. 数据统计分析（规划中）
- **业务规则**:
  - 医生有在线/离线状态，只有在线医生可被咨询
  - 患者通过姓名+生日验证身份
  - 问题有"待处理"和"已回答"两种状态
  - 医生有科室、职称、专长等属性

## 工作空间结构

### 文件树（含导航注释）

```
qa-live-healthcare-0322/
├── .asdm/                                    # ASDM 配置与工具集
│   ├── contexts/                             # ⭐ 上下文文件目录（AI 模型参考）
│   └── toolsets/context-builder-7/           # Context Builder 工具集
├── server/                                   # 后端微服务目录
│   ├── qa-service-user/                      # ⭐ 用户管理服务（已实现）
│   │   ├── src/main/java/com/leansofx/qaserviceuser/
│   │   │   ├── QaServiceUserApplication.java         # 启动类
│   │   │   ├── config/CorsConfig.java                # CORS 跨域配置
│   │   │   ├── controller/                           # REST 控制器
│   │   │   │   ├── DoctorUserController.java         # 医生用户 CRUD API
│   │   │   │   ├── DiagnosticController.java         # 数据库诊断 API
│   │   │   │   └── TestController.java               # CORS 测试 API
│   │   │   ├── dto/                                  # 数据传输对象
│   │   │   │   ├── request/DoctorUserRequest.java    # 请求 DTO
│   │   │   │   └── response/DoctorUserResponse.java  # 响应 DTO（不含密码）
│   │   │   ├── entity/DoctorUser.java                # JPA 实体（医生用户）
│   │   │   ├── repository/DoctorUserRepository.java  # JPA 仓库
│   │   │   └── service/                             # 业务逻辑层
│   │   │       ├── DoctorUserService.java            # 服务接口
│   │   │       └── impl/DoctorUserServiceImpl.java   # 服务实现
│   │   ├── src/main/resources/
│   │   │   └── application.properties                # 应用配置（端口8080、MySQL）
│   │   ├── mysql/                                    # 数据库脚本
│   │   │   ├── create-tables.sql                     # 建表脚本
│   │   │   └── import-doctor-data.sql                # 医生数据初始化
│   │   ├── docs/                                     # 服务级文档
│   │   │   ├── api.md / arch.md / coding-style.md
│   │   │   ├── data-models.md / deployment.md / project-structure.md
│   │   │   └── README.md
│   │   └── pom.xml                                   # Maven 依赖配置
│   ├── qa-service-question/                  # ⚠️ 问题管理服务（仅脚手架）
│   │   ├── src/main/java/com/leansofx/qaservicequestion/
│   │   │   └── QaServiceQuestionApplication.java     # 启动类（无业务代码）
│   │   ├── src/main/resources/
│   │   │   └── application.properties                # 应用配置（端口8081）
│   │   └── pom.xml                                   # 无 JPA/MySQL 依赖
│   └── qa-service-statistic/                 # ❌ 统计分析服务（空，仅有 .gitkeep）
├── web/
│   └── qa-web/                               # ⭐ 前端 Vue.js 应用
│       ├── src/
│       │   ├── main.ts                       # 应用入口（Ant Design + Router + i18n）
│       │   ├── App.vue                       # 根组件（Header + RouterView + Footer）
│       │   ├── router/index.ts               # 路由配置（7个路由）
│       │   ├── store/index.ts                # 状态管理（reactive，非 Pinia/Vuex）
│       │   ├── i18n.ts                       # 国际化配置（中/英）
│       │   ├── components/                   # 公共组件
│       │   │   ├── AppHeader.vue             # 顶部导航栏（含语言切换、医生登录）
│       │   │   └── AppFooter.vue             # 底部信息栏
│       │   ├── views/                        # 页面视图
│       │   │   ├── Home.vue                  # 首页（统计面板、在线诊室）
│       │   │   ├── Consultation.vue          # 患者问诊页（登录+提问）
│       │   │   ├── Doctors.vue               # 医生列表页
│       │   │   ├── DoctorLogin.vue           # 医生登录页
│       │   │   ├── DoctorRoom.vue            # 医生诊室（回复问题）
│       │   │   └── About.vue                 # 关于页面
│       │   ├── data/                         # ⚠️ Mock 数据（JSON文件）
│       │   │   ├── doctor-user-list.json     # 医生列表（5名医生）
│       │   │   ├── patient-user.json         # 患者列表（5名患者）
│       │   │   └── question-list.json        # 问诊问题（7条问题）
│       │   └── locales/                      # 国际化翻译文件
│       │       ├── zh-cn.json                # 中文（默认）
│       │       └── en-us.json                # 英文
│       ├── package.json                      # 前端依赖配置
│       └── vite.config.ts                    # Vite 构建配置
├── docker-compose.yml                        # Docker 编排（MySQL + phpMyAdmin）
├── package.json                              # 根目录 npm 脚本（concurrently）
└── README.md                                 # 项目说明文档
```

### 关键目录说明
- **`server/qa-service-user/`**: 已实现的后端微服务 — 医生用户 CRUD，连接 MySQL
- **`server/qa-service-question/`**: 脚手架状态 — 仅有启动类，无业务逻辑
- **`web/qa-web/src/store/`**: 前端核心状态管理 — 包含问题/患者的本地逻辑（尚未对接后端 API）
- **`web/qa-web/src/data/`**: Mock JSON 数据 — 前端当前数据源（未使用后端 API）

## 开发指南

### 构建与编译
```bash
# 安装根目录依赖
npm install

# 安装前端依赖
npm run install:all

# 启动所有服务（前端 + 后端）
npm run dev

# 单独启动前端（http://localhost:5173）
npm run dev:web

# 单独启动用户管理服务（http://localhost:8080）
npm run dev:user

# 单独启动问题管理服务（http://localhost:8081）
npm run dev:question

# 构建前端
npm run build

# 清理所有构建文件
npm run clean
```

### 启动基础设施
```bash
# 启动 MySQL + phpMyAdmin
docker-compose up -d

# MySQL 连接信息
# 主机: localhost:3301
# 数据库: healthcare
# 用户: user / 密码: healthcare123
# Root: root / 密码: root123

# phpMyAdmin: http://localhost:8001
```

### 测试
```bash
# 后端测试（用户服务）
cd server/qa-service-user && ./mvnw test

# 后端测试（问题服务，使用 Testcontainers）
cd server/qa-service-question && ./mvnw test

# 前端暂无测试框架
```

### 服务端口一览

| 服务 | 端口 | 访问地址 | 状态检查 |
|------|------|----------|----------|
| 前端开发服务器 | 5173 | http://localhost:5173 | 页面访问 |
| 用户管理服务 | 8080 | http://localhost:8080 | `/actuator/health` |
| 问题管理服务 | 8081 | http://localhost:8081 | `/actuator/health` |
| MySQL | 3301 | localhost:3301 | `mysqladmin ping` |
| phpMyAdmin | 8001 | http://localhost:8001 | 页面访问 |

## 当前架构状态与差距

> 以下记录了当前工作空间的关键差距，AI 模型在进行开发任务时应注意这些事项。

| 编号 | 差距项 | 说明 | 影响 |
|------|--------|------|------|
| 1 | 前后端未连通 | 前端使用本地 JSON 数据，无 API 调用层（无 Axios/fetch、无 Vite 代理配置） | 需要创建 API 服务层并对接后端 |
| 2 | 问题服务仅脚手架 | qa-service-question 无业务代码、无数据库配置 | 需要完整实现 Entity/Repository/Service/Controller |
| 3 | 统计服务未启动 | qa-service-statistic 仅有 .gitkeep | 规划中，暂不影响 |
| 4 | 后端缺少 Patient 实体 | 前端有患者数据，后端无对应实体和 API | 需要在 qa-service-user 或新服务中添加 |
| 5 | 后端缺少 Question 实体 | 前端有问题数据，后端无对应实体和 API | 需要在 qa-service-question 中实现 |
| 6 | 无认证机制 | 医生登录在前端本地 JSON 比对明文密码，无 JWT/Spring Security | 安全风险，需实现认证机制 |
| 7 | 密码明文存储 | SQL 导入和 JSON 数据中密码为明文 `123456`，架构文档建议 BCrypt 但未实现 | 安全风险 |
| 8 | CORS 重复配置 | 用户服务 CORS 在三处配置：`application.properties`、`CorsConfig.java`、`@CrossOrigin` | 应统一为一处 |
| 9 | 后端未容器化 | docker-compose.yml 仅有 MySQL 和 phpMyAdmin | 需要添加后端服务 Dockerfile |
| 10 | i18n 部分覆盖 | 仅首页和导航栏有翻译，其他页面硬编码中文 | 需要补充其他页面的翻译 key |

## 上下文文件引用

本工作空间在 `.asdm/contexts/` 目录下提供以下上下文文件：

1. **[standard-project-structure.md](./standard-project-structure.md)** - 标准项目结构与组织规范
2. **[standard-coding-style.md](./standard-coding-style.md)** - 编码规范与风格指南
3. **[data-models.md](./data-models.md)** - 数据模型、关系与 ER 图
4. **[deployment.md](./deployment.md)** - 部署配置与流程
5. **[api.md](./api.md)** - API 定义、端点与文档
6. **[architecture.md](./architecture.md)** - 系统架构与设计决策

## AI 模型使用指南

### 如何使用本上下文
1. **从本索引开始**，了解工作空间整体结构
2. **根据任务查阅对应上下文文件**，获取详细信息
3. **遵循开发指南**进行构建、测试和部署
4. **保持与现有模式一致**，参考编码规范和架构决策
5. **注意架构差距**，在进行开发任务时考虑前后端连通等未完成项

### 常见任务指引
- **添加新功能**: 先查看架构和数据模型上下文
- **修改 API**: 参照 API 文档上下文并同步更新
- **数据库变更**: 更新数据模型和迁移脚本
- **部署更新**: 遵循部署流程文档

## 版本历史

| 版本 | 日期 | 变更内容 | 作者 |
|------|------|----------|------|
| 1.0.0 | 2026-04-30 | 初始上下文创建 | ASDM Context Builder |

---

*本上下文文件由 Context Builder 工具集维护。当工作空间发生变更时，使用 `/asdm-context-update` 更新。*
