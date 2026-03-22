# QA Service User

## 项目概述

QA Service User 是医疗问答系统中的用户管理服务，基于 Spring Boot 3.5.7 构建。该服务提供用户相关的 API 接口，支持跨域访问，并集成了 Spring Boot Actuator 进行应用监控和健康检查。

- **项目名称**: qa-service-user
- **版本**: 0.0.1-SNAPSHOT
- **描述**: QA Service User - Healthcare QA System User Management Service
- **开发团队**: QA Healthcare Team
- **开发环境**: development

## 项目文件结构

```
qa-service-user/
├── .gitattributes                          # Git 属性配置文件
├── .gitignore                              # Git 忽略文件配置
├── .gitkeep                                # Git 空目录占位文件
├── HELP.md                                 # Spring Boot 帮助文档
├── mvnw                                    # Maven Wrapper 脚本 (Unix/Linux)
├── mvnw.cmd                                # Maven Wrapper 脚本 (Windows)
├── pom.xml                                 # Maven 项目配置文件
├── .mvn/                                   # Maven Wrapper 配置目录
│   └── wrapper/
│       └── maven-wrapper.properties       # Maven Wrapper 属性配置
└── src/                                    # 源代码目录
    ├── main/                               # 主要源代码
    │   ├── java/                           # Java 源代码
    │   │   └── com/leansofx/qaserviceuser/
    │   │       ├── QaServiceUserApplication.java    # Spring Boot 主启动类
    │   │       ├── config/                          # 配置类目录
    │   │       │   └── CorsConfig.java              # CORS 跨域配置类
    │   │       └── controller/                      # 控制器目录
    │   │           └── TestController.java          # 测试控制器，提供 CORS 测试接口
    │   └── resources/                      # 资源文件目录
    │       └── application.properties      # Spring Boot 应用配置文件
    └── test/                               # 测试代码
        └── java/                           # Java 测试代码
            └── com/leansofx/qaserviceuser/
                └── QaServiceUserApplicationTests.java  # Spring Boot 应用测试类
```

## 项目文档

本项目提供了详细的技术文档，请参考以下链接：

- [API 接口文档](docs/api.md) - 详细的 API 接口说明和使用示例
- [项目结构文档](docs/project-structure.md) - 项目架构和代码结构说明

## 项目技术栈信息

### 核心框架
- **Spring Boot**: 3.5.7
- **Java**: 17
- **Maven**: 项目构建和依赖管理工具

### 主要依赖
- **spring-boot-starter-web**: Web 应用开发，提供 RESTful API 支持
- **spring-boot-starter-actuator**: 应用监控和管理端点
- **spring-boot-starter-test**: 测试框架支持 (JUnit 5)

### 构建工具
- **Maven**: 使用 Maven Wrapper 进行项目构建
- **spring-boot-maven-plugin**: Spring Boot Maven 插件

## 应用管理脚本

项目提供了完整的应用生命周期管理脚本，位于项目根目录下，支持 Linux/macOS 和 Windows 系统：

### 脚本列表

| 脚本文件 | 功能描述 | Linux/macOS | Windows |
|---------|---------|-------------|---------|
| 启动应用 | 启动 Spring Boot 应用 | `./start.sh` | `start.cmd` |
| 停止应用 | 停止正在运行的应用 | `./stop.sh` | `stop.cmd` |
| 重启应用 | 重启应用服务 | `./restart.sh` | `restart.cmd` |
| 查看状态 | 查看应用运行状态 | `./status.sh` | `status.cmd` |

### Shell 脚本详细说明 (Linux/macOS)

#### start.sh
- **功能**: 启动 Spring Boot 应用
- **主要特性**:
  - 检查应用是否已在运行
  - 验证 JAR 文件是否存在
  - 自动创建日志目录
  - 使用 nohup 后台启动应用
  - 保存进程 PID 到文件
- **日志文件**: `logs/application.log`
- **PID 文件**: `qa-service-user.pid`

#### stop.sh
- **功能**: 停止正在运行的应用
- **主要特性**:
  - 读取 PID 文件获取进程 ID
  - 优雅终止进程（先发送 SIGTERM）
  - 等待进程正常结束（最多 10 秒）
  - 如需要则强制终止（SIGKILL）
  - 清理 PID 文件
- **超时机制**: 10 秒后自动强制终止

#### restart.sh
- **功能**: 重启应用服务
- **执行流程**:
  1. 调用 `stop.sh` 停止当前服务
  2. 等待 1 秒确保进程完全结束
  3. 调用 `start.sh` 启动服务

#### status.sh
- **功能**: 检查应用运行状态
- **输出信息**:
  - 应用运行状态
  - 进程 PID
  - 详细的进程信息（PID、PPID、命令、运行时间、CPU/内存使用率）
- **自动清理**: 发现无效 PID 文件时自动删除

### 使用示例

```bash
# 启动应用
./start.sh

# 检查状态
./status.sh

# 停止应用
./stop.sh

# 重启应用
./restart.sh
```

### 注意事项
- 使用前确保已执行 `mvn clean package` 构建项目
- 脚本会自动处理 PID 文件和日志目录
- 所有脚本都包含错误处理和状态检查

### CMD 脚本详细说明 (Windows)

#### start.cmd
- **功能**: 启动 Spring Boot 应用
- **主要特性**:
  - 检查应用是否已在运行
  - 验证 JAR 文件是否存在
  - 自动创建日志目录
  - 使用 PowerShell 后台启动应用
  - 保存进程 PID 到文件
- **日志文件**: `logs\application.log`
- **PID 文件**: `qa-service-user.pid`

#### stop.cmd
- **功能**: 停止正在运行的应用
- **主要特性**:
  - 读取 PID 文件获取进程 ID
  - 优雅终止进程（使用 taskkill）
  - 等待进程正常结束（最多 10 秒）
  - 如需要则强制终止（/F 参数）
  - 清理 PID 文件
- **超时机制**: 10 秒后自动强制终止

#### restart.cmd
- **功能**: 重启应用服务
- **执行流程**:
  1. 调用 `stop.cmd` 停止当前服务
  2. 等待 1 秒确保进程完全结束
  3. 调用 `start.cmd` 启动服务

#### status.cmd
- **功能**: 检查应用运行状态
- **输出信息**:
  - 应用运行状态
  - 进程 PID
  - 详细的进程信息
- **自动清理**: 发现无效 PID 文件时自动删除

### Windows 使用示例

```cmd
REM 启动应用
start.cmd

REM 检查状态
status.cmd

REM 停止应用
stop.cmd

REM 重启应用
restart.cmd
```

### Windows 注意事项
- 确保系统已安装 PowerShell 5.1 或更高版本
- 使用前确保已执行 `mvnw.cmd clean package` 构建项目
- 如遇端口占用，请先停止占用进程或修改端口配置

## 开发调试

### 启动应用
```bash
# 使用 Maven Wrapper 启动
./mvnw spring-boot:run

# 或者使用 Maven 启动
mvn spring-boot:run
```

### 应用配置
- **服务端口**: 8080
- **应用名称**: qa-service-user
- **基础包路径**: com.leansofx.qaserviceuser

### CORS 配置
项目已配置跨域访问支持：
- **允许的源**: 所有源 (*)
- **允许的方法**: GET, POST, PUT, DELETE, OPTIONS
- **允许的头部**: 所有头部 (*)
- **允许凭证**: true
- **最大缓存时间**: 3600 秒

### API 接口
当前提供的测试接口：
- `GET /api/test/cors` - CORS 配置测试接口
- `POST /api/test/cors` - CORS POST 请求测试接口
- `OPTIONS /api/test/cors` - CORS 预检请求处理

## 测试和监控信息

### 单元测试
- **测试框架**: JUnit 5 (通过 spring-boot-starter-test)
- **测试类**: `QaServiceUserApplicationTests.java`
- **测试内容**: Spring Boot 应用上下文加载测试

### 运行测试
```bash
# 运行所有测试
./mvnw test

# 或者使用 Maven
mvn test
```

### 应用监控 (Actuator)

#### 监控端点配置
- **基础路径**: `/actuator`
- **启用的端点**: health, info, metrics, env, beans, loggers
- **健康检查**: 显示详细信息和组件状态

#### 可用的监控端点
- `GET /actuator/health` - 应用健康状态检查
- `GET /actuator/info` - 应用信息展示
- `GET /actuator/metrics` - 应用指标数据
- `GET /actuator/env` - 环境变量信息
- `GET /actuator/beans` - Spring Bean 信息
- `GET /actuator/loggers` - 日志配置信息

#### 应用信息配置
- **应用名称**: qa-service-user
- **应用描述**: QA Service User - Healthcare QA System User Management Service
- **版本**: 0.0.1-SNAPSHOT
- **编码**: UTF-8
- **Java 版本**: 17
- **开发团队**: QA Healthcare Team
- **环境**: development
- **构建时间**: 2025-11-03
- **功能特性**: CORS, Actuator, Health Checks, User Management

### 健康检查
应用启动后，可通过以下方式检查服务状态：
```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 查看应用信息
curl http://localhost:8080/actuator/info

# 测试 CORS 配置
curl http://localhost:8080/api/test/cors
```

## 构建和部署

### 构建项目
```bash
# 编译项目
./mvnw compile

# 打包项目
./mvnw package

# 清理并重新构建
./mvnw clean package
```

### 生成的构建产物
- **JAR 文件**: `target/qa-service-user-0.0.1-SNAPSHOT.jar`
- **可执行 JAR**: 包含所有依赖的独立可执行文件

### 运行打包后的应用
```bash
java -jar target/qa-service-user-0.0.1-SNAPSHOT.jar
```