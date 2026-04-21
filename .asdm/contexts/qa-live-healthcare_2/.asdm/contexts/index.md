# 工作区上下文索引

## 概述
本文档是 AI 模型理解和操作本工作区的索引和指南，提供工作区内容的结构化概览，并引导 AI 模型找到相关上下文。

## 工作区信息

### 基本信息
- **工作区名称**: QA Live Healthcare（在线医疗问诊平台）
- **描述**: 面向患者和医生的实时在线问答咨询平台，采用微服务后端（Spring Boot）和 Vue.js 前端。前端使用 Mock 数据独立运行，后端服务为最小化脚手架。
- **创建日期**: 2025 年
- **最后更新**: 2026-04-21

### 技术栈
- **后端语言**: Java 17
- **后端框架**: Spring Boot 3.5.7、Spring Data JPA
- **前端语言**: TypeScript 5.5.3
- **前端框架**: Vue 3.5.10、Ant Design Vue 4.2.6、Vue Router 4.6.3
- **构建工具**: Maven（后端）、Vite 5.4.8（前端）、npm（前端）
- **数据库**: MySQL 8.4（通过 Docker Compose 部署）
- **国际化**: vue-i18n 9.14.5（中文默认，英文部分支持）
- **部署平台**: Docker Compose（数据库）、Shell 脚本管理（应用服务）

### 业务上下文
- **业务领域**: 医疗健康 — 在线问诊
- **核心业务流程**:
  1. **患者认证**: 患者通过姓名 + 生日验证身份（新患者自动创建）
  2. **医生登录**: 医生通过用户名 + 密码登录进入诊室
  3. **提问流程**: 患者选择医生 → 提交问题 → 医生在诊室查看并回复
  4. **问题状态**: `pending`（待回答）→ `answered`（已回答）
- **业务规则**:
  - 医生可以标记问题为"已口述解答"
  - 诊室通过 URL 共享：`/consultation/{doctorUsername}`
  - 当前无 JWT/Session 认证机制，仅客户端状态管理
  - 测试账号：医生 `dr-zhang-wei` / `123456`

## 工作区结构

### 文件树
```
qa-live-healthcare-0322/
├── .asdm/                              # ASDM 配置和工具集
│   ├── contexts/                       # 上下文文件（本目录）
│   ├── docs/                           # ASDM 文档和指令
│   └── toolsets/                       # 已安装的工具集
├── .codebuddy/                         # CodeBuddy 命令和技能
├── docker-compose.yml                  # MySQL + phpMyAdmin 容器编排
├── package.json                        # 根级 npm 脚本（concurrently 启动所有服务）
├── server/                             # 后端微服务
│   ├── qa-service-user/                # 用户服务（端口 8080）— 唯一完整服务
│   │   ├── src/main/java/com/leansofx/qaserviceuser/
│   │   │   ├── config/                 # CORS 配置
│   │   │   ├── controller/             # DoctorUserController, TestController, DiagnosticController
│   │   │   ├── dto/request/            # DoctorUserRequest
│   │   │   ├── dto/response/           # DoctorUserResponse
│   │   │   ├── entity/                 # DoctorUser（JPA 实体）
│   │   │   ├── repository/             # DoctorUserRepository
│   │   │   └── service/impl/           # DoctorUserServiceImpl
│   │   ├── mysql/                      # SQL 脚本（建表、数据导入）
│   │   └── src/main/resources/         # application.properties
│   ├── qa-service-question/            # 问题服务（端口 8081）— 仅脚手架
│   │   └── src/main/.../QaServiceQuestionApplication.java
│   └── qa-service-statistic/           # 统计服务 — 仅 .gitkeep（规划中）
└── web/
    └── qa-web/                         # Vue 3 前端应用
        ├── src/
        │   ├── views/                  # 页面组件（6 个）
        │   ├── components/             # 通用组件（AppHeader, AppFooter）
        │   ├── store/                  # 响应式状态管理（reactive，非 Pinia/Vuex）
        │   ├── router/                 # 路由配置
        │   ├── data/                   # Mock JSON 数据
        │   ├── locales/                # i18n 翻译文件（zh-cn, en-us）
        │   └── i18n.ts                 # 国际化配置
        ├── vite.config.ts              # Vite 构建配置
        └── package.json                # 前端依赖和脚本
```

### 关键目录说明
- **`server/qa-service-user/`**: 唯一完整实现的后端服务，包含医生 CRUD API
- **`web/qa-web/src/store/`**: 前端核心状态管理，定义了所有数据模型和业务方法
- **`web/qa-web/src/data/`**: Mock JSON 数据，当前前端所有数据的来源
- **`docker-compose.yml`**: MySQL 数据库和 phpMyAdmin 的容器配置

## 开发指南

### 构建和编译
```bash
# 启动所有服务（前端 + 后端）
npm run dev

# 仅启动前端（独立运行，无需后端）
cd web/qa-web && npm run dev

# 启动后端用户服务
cd server/qa-service-user && ./mvnw spring-boot:run

# 启动后端问题服务（脚手架，无业务逻辑）
cd server/qa-service-question && ./mvnw spring-boot:run

# 前端生产构建
cd web/qa-web && npm run build
```

### 数据库
```bash
# 启动 MySQL + phpMyAdmin
docker-compose up -d

# MySQL 连接信息
# 主机: localhost:3301  数据库: healthcare  用户: user  密码: healthcare123

# phpMyAdmin
# 地址: http://localhost:8001
```

### 测试
```bash
# 后端测试（用户服务）
cd server/qa-service-user && ./mvnw test

# 前端构建检查（含 TypeScript 类型检查）
cd web/qa-web && npm run build

# CORS 测试
curl http://localhost:8080/api/test/cors
curl -X POST http://localhost:8080/api/test/cors
```

### 代码质量
- **后端**: Spring Boot Actuator 监控端点（health, info, metrics, env, beans, loggers）
- **前端**: `vue-tsc` TypeScript 编译检查 + Vite 构建
- **无 ESLint/Prettier 配置**

## 上下文文件参考

本工作区在 `.asdm/contexts/` 目录下提供以下上下文文件：

1. **[项目结构规范](./standard-project-structure.md)** - 标准项目结构和组织方式
2. **[编码风格规范](./standard-coding-style.md)** - 编码标准和风格指南
3. **[数据模型](./data-models.md)** - 数据模型、关系和图表
4. **[部署配置](./deployment.md)** - 部署配置和流程
5. **[API 文档](./api.md)** - API 定义、端点和文档
6. **[架构概览](./architecture.md)** - 系统架构和设计决策

## AI 模型指南

### 如何使用本上下文
1. **从本索引开始**，了解工作区整体结构
2. **根据任务查阅具体上下文文件**
3. **遵循开发指南**进行构建、测试和部署
4. **保持与现有模式一致**

### 常见任务
- **添加新服务**: 在 `server/` 下创建目录，复制 Maven Wrapper，设置端口，添加 CORS 配置
- **添加新页面**: 在 `views/` 创建组件，在 `router/index.ts` 添加路由，更新 `AppHeader.vue` 导航
- **修改 Mock 数据**: 编辑 `src/data/*.json`（支持热更新），需与 `store/index.ts` 中的接口匹配
- **添加新 API**: 在后端添加 Controller/Service/Repository，前端 store 中添加对应调用方法
- **数据库变更**: 更新 `mysql/create-tables.sql` 和 JPA 实体

### 当前限制与注意事项
- **前后端未集成**: 前端使用 JSON Mock 数据，未调用后端 API
- **问题服务为空壳**: qa-service-question 仅有启动类，问题逻辑全部在前端
- **统计服务未开发**: qa-service-statistic 仅为占位
- **无认证机制**: 医生登录仅客户端校验，无 JWT/Session
- **CORS 双重配置**: `application.properties` 和 `CorsConfig.java` 同时配置 CORS
- **国际化不完整**: 仅 Header 和 Home 页面使用 i18n，其他视图为硬编码中文
- **新增端点必须配置 CORS**

## 版本历史
| 版本 | 日期 | 变更 | 作者 |
|------|------|------|------|
| 1.0.0 | 2026-04-21 | 初始上下文创建 | ASDM Context Builder |

---

*本上下文文件由 Context Builder 工具集维护。工作区发生变更时，请使用 `/asdm-context-update` 更新。*
