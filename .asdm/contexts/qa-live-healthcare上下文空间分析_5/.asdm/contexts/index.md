# QA Healthcare - 项目上下文索引

## 项目概览

- **项目名称**: qa-live-healthcare（医疗问答系统）
- **项目描述**: 基于微服务架构的前后端分离医疗问答系统，提供用户管理、问题管理和统计分析等核心功能
- **技术栈**: Spring Boot 3.5.7 + Java 17 + Vue.js 3.5.10 + TypeScript 5.5.3 + Vite 5.4.8 + MySQL 8.4
- **模块总数**: 4 个
- **总代码量**: 约 8,245 行（含文档）

## 模块索引表

| 模块名称 | 路径 | 文件数 | 代码行数 | 主要类型 | 上下文状态 |
|---------|------|--------|---------|---------|-----------|
| qa-service-user | server/qa-service-user | 28 | 3,263 | .java, .md, .sh, .sql | 未生成 |
| qa-service-question | server/qa-service-question | 9 | 666 | .java, .xml, .properties | 未生成 |
| qa-service-statistic | server/qa-service-statistic | 0 | 0 | - | 未生成（待开发） |
| qa-web | web/qa-web | 36 | 4,316 | .vue, .ts, .json | 未生成 |

## 模块详情

### 后端微服务

- **qa-service-user** — 用户管理服务（端口 8080）
  - Spring Boot 3.5.7 + Spring Data JPA + MySQL
  - 包含完整的分层架构：Controller / Service / Repository / Entity / DTO
  - 含 CORS 配置、诊断接口、数据库连接测试

- **qa-service-question** — 问题管理服务（端口 8081）
  - Spring Boot 3.5.7 + Actuator
  - 当前为脚手架状态，仅含启动类和测试配置

- **qa-service-statistic** — 统计分析服务（待开发）
  - 空模块，仅含 .gitkeep 占位文件

### 前端应用

- **qa-web** — Web 前端（端口 5173）
  - Vue 3 + TypeScript + Vite + Ant Design Vue + Vue Router + vue-i18n
  - 10 个 Vue 组件，含 5 个页面视图（Home、About、Consultation、DoctorRoom、Doctors、DoctorLogin）
  - 支持中英文国际化（zh-cn / en-us）

## 快速导航

### 按模块名称

- [qa-service-user](server/qa-service-user/) — 用户管理微服务
- [qa-service-question](server/qa-service-question/) — 问题管理微服务
- [qa-service-statistic](server/qa-service-statistic/) — 统计分析微服务（待开发）
- [qa-web](web/qa-web/) — Web 前端应用

### 按模块类型

**后端微服务（Java/Spring Boot）**:
- qa-service-user | qa-service-question | qa-service-statistic

**前端应用（Vue.js/TypeScript）**:
- qa-web

## 使用指南

### 生成单个模块上下文

```
/asdm-cpc-module-build <模块名>
```

示例：`/asdm-cpc-module-build qa-service-user`

### 批量生成所有模块上下文

```
/asdm-cpc-all-build
```

### 更新上下文

当模块代码发生变更后，重新运行对应的生成命令即可更新上下文。

## 统计摘要

| 指标 | 数值 |
|------|------|
| 模块总数 | 4 |
| 已生成上下文 | 0 |
| 未生成上下文 | 4 |
| 生成中 | 0 |
| 总文件数 | 73 |
| 总代码行数 | ~8,245 |
