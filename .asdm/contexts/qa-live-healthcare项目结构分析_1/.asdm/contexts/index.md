# QA Healthcare - 项目上下文索引

## 项目概览

| 属性 | 信息 |
|------|------|
| **项目名称** | QA Healthcare - 医疗问答系统 |
| **项目描述** | 基于微服务架构的前后端分离医疗问答系统，提供用户管理、问题管理和统计分析等核心功能 |
| **业务领域** | 医疗健康 - 问答咨询平台 |
| **模块总数** | 4 个 |
| **总代码量** | 约 7,561 行（含文档） |

### 技术栈

| 分类 | 技术 |
|------|------|
| **后端框架** | Spring Boot 3.5.7 |
| **后端语言** | Java 17 (OpenJDK Temurin-17.0.14+7) |
| **后端构建** | Maven 3.x |
| **前端框架** | Vue.js 3.5.10 |
| **前端语言** | TypeScript 5.5.3 |
| **前端构建** | Vite 5.4.8 |
| **UI 组件库** | Ant Design Vue 4.2.6 |
| **路由** | Vue Router 4.6.3 |
| **部署** | Docker (docker-compose) |

## 模块索引表

| 模块名称 | 路径 | 类型 | 代码量 | 上下文状态 | 上下文链接 |
|---------|------|------|--------|-----------|-----------|
| qa-service-user | `server/qa-service-user/` | 后端微服务 | 25 文件 / 3,119 行 | 未生成 | - |
| qa-service-question | `server/qa-service-question/` | 后端微服务 | 7 文件 / 152 行 | 未生成 | - |
| qa-service-statistic | `server/qa-service-statistic/` | 后端微服务（待开发） | 0 文件 / 0 行 | 未生成 | - |
| qa-web | `web/qa-web/` | Web 前端 | 34 文件 / 4,290 行 | 未生成 | - |

## 模块详情

### qa-service-user（用户管理服务）

- **路径**: `server/qa-service-user/`
- **技术栈**: Spring Boot 3.5.7 + Java 17 + Maven
- **端口**: 8080
- **功能**: 用户注册、登录、权限管理、医生用户 CRUD
- **代码统计**:
  - `.java`: 13 文件, 905 行
  - `.xml`: 1 文件, 71 行
  - `.properties`: 2 文件, 49 行
  - `.sql`: 2 文件, 67 行
  - `.md`: 7 文件, 2,027 行
- **主要类**: `DoctorUserController`, `DoctorUserService`, `DoctorUserRepository`, `DoctorUser`

### qa-service-question（问题管理服务）

- **路径**: `server/qa-service-question/`
- **技术栈**: Spring Boot 3.5.7 + Java 17 + Maven
- **端口**: 8081
- **功能**: 医疗问题发布、回答、管理（当前为初始脚手架，待开发）
- **代码统计**:
  - `.java`: 4 文件, 47 行
  - `.xml`: 1 文件, 68 行
  - `.properties`: 2 文件, 37 行
- **状态**: 初始脚手架，仅包含应用入口和默认测试

### qa-service-statistic（统计分析服务）

- **路径**: `server/qa-service-statistic/`
- **技术栈**: 待定
- **端口**: 待配置
- **功能**: 数据统计、分析、报表功能（规划中，尚未开发）
- **代码统计**: 无代码文件
- **状态**: 规划中，目录仅含 `.gitkeep`

### qa-web（前端应用）

- **路径**: `web/qa-web/`
- **技术栈**: Vue.js 3.5.10 + TypeScript 5.5.3 + Vite 5.4.8
- **端口**: 5173
- **功能**: 用户界面、交互逻辑、页面展示
- **代码统计**:
  - `.vue`: 10 文件, 2,432 行
  - `.ts`: 6 文件, 251 行
  - `.json`: 10 文件, 344 行
  - `.css`: 1 文件, 36 行
  - `.html`: 1 文件, 13 行
  - `.md`: 3 文件, 1,065 行

## 快速导航

### 按模块名称

- [qa-service-user](#qa-service-user用户管理服务)
- [qa-service-question](#qa-service-question问题管理服务)
- [qa-service-statistic](#qa-service-statistic统计分析服务)
- [qa-web](#qa-web前端应用)

### 按模块类型

**后端微服务 (Spring Boot)**:
- qa-service-user（用户管理）
- qa-service-question（问题管理）
- qa-service-statistic（统计分析 - 规划中）

**前端应用 (Vue.js)**:
- qa-web（Web 前端）

## 使用指南

### 生成单个模块上下文

```bash
/asdm-cpc-module-build <模块名>
```

示例：
```bash
/asdm-cpc-module-build qa-service-user
/asdm-cpc-module-build qa-web
```

### 批量生成所有模块上下文

```bash
/asdm-cpc-all-module-context-build
```

### 更新上下文

当模块代码发生变更时，重新运行对应的构建命令即可更新上下文。

## 版本历史

| 版本 | 日期 | 变更说明 |
|------|------|---------|
| 1.0.0 | 2026-04-21 | 初始上下文索引创建 |

---

*此上下文文件由 Context Builder 工具集维护。当工作区结构发生变化时，运行 `/asdm-cpc-index-init` 更新。*