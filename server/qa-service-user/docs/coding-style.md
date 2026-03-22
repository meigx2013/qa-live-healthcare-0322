# QA Service User 项目编码规范

## 1. 前言
- **目的**：统一代码风格,提高代码质量与可维护性,降低协作成本。
- **适用范围**：本项目所有后端Java代码、配置文件及测试代码。
- **基本原则**：清晰性、一致性、简单性、可测试性。

---

## 2. 项目结构与包命名

### 2.1 整体结构
```
src/main/java/com/leansofx/qaserviceuser/
├── QaServiceUserApplication.java     # Spring Boot启动类
├── config/                           # 配置类
│   ├── CorsConfig.java              # CORS跨域配置
│   └── DatabaseConfig.java          # 数据库配置
├── controller/                       # 控制器层
│   ├── advice/                      # 全局异常处理
│   └── TestController.java          # 测试控制器
├── service/                          # 业务逻辑层
│   ├── impl/                        # 实现类
│   └── DoctorUserService.java       # 服务接口
├── repository/                       # 数据访问层(JPA)
│   └── DoctorUserRepository.java    # 数据仓库接口
├── entity/                           # 实体类(与数据库对应)
│   └── DoctorUser.java              # 医生用户实体
├── dto/                              # 数据传输对象
│   ├── request/                     # 请求DTO
│   │   └── DoctorUserRequest.java
│   └── response/                    # 响应DTO
│       └── DoctorUserResponse.java
├── exception/                        # 自定义异常
│   └── BusinessException.java       # 业务异常
├── enums/                            # 枚举类
├── util/                             # 工具类
├── constant/                         # 常量类
└── aspect/                           # AOP切面
```

### 2.2 包命名
- 所有包名采用 **全小写**,单词间用点分隔,不出现下划线。
- 根包名: `com.leansofx.qaserviceuser`
- 示例: `com.leansofx.qaserviceuser.controller`, `com.leansofx.qaserviceuser.service.impl`

---

## 3. 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类(Class) | 大驼峰(PascalCase) | `DoctorUserService`, `DoctorUserController` |
| 接口(Interface) | 大驼峰,避免使用`I`前缀 | `DoctorUserRepository`, `DoctorUserService` |
| 方法(Method) | 小驼峰(camelCase) | `getDoctorById`, `createDoctorUser` |
| 变量(Variable) | 小驼峰 | `doctorName`, `userList` |
| 常量(Constant) | 全大写,下划线分隔 | `MAX_RETRY_COUNT` |
| 枚举(Enum) | 全大写,下划线分隔 | `USER_STATUS_ACTIVE` |
| 抽象类 | 以`Abstract`开头 | `AbstractBaseService` |
| 测试类 | 以`Test`结尾 | `DoctorUserServiceTest` |
| DTO请求类 | 以`Request`结尾 | `DoctorUserRequest` |
| DTO响应类 | 以`Response`结尾 | `DoctorUserResponse` |
| Service实现类 | 以`Impl`结尾 | `DoctorUserServiceImpl` |

---

## 4. 代码格式规范

### 4.1 基本格式
- **缩进**: 使用4个空格,禁止使用Tab。
- **行长度**: 每行不超过120字符,超出换行并对齐。
- **大括号**: 采用"埃及括号"(左括号不换行),右括号独占一行。

```java
if (condition) {
    // do something
} else {
    // do something else
}
```

### 4.2 空行规则
- 方法之间、不同逻辑块之间加空行。
- 类成员变量与构造函数/方法之间加空行。
- import语句分组后加空行。

---

## 5. SpringBoot 实践规范

### 5.1 Controller 层
- **注解**: 使用`@RestController`,不使用`@Controller`+`@ResponseBody`。
- **请求映射**:
  - 使用`@GetMapping`、`@PostMapping`等明确注解,避免直接用`@RequestMapping`。
  - URL使用小写字母+连字符: `/api/v1/doctor-users`
- **参数校验**: 使用`@Valid` + `@NotNull`、`@NotBlank`等JSR-303注解。
- **返回值**: 统一使用DTO对象,不直接返回Entity。


### 5.2 Service 层
- **接口与实现**: 面向接口编程,接口定义方法,实现类放在`impl`包下,命名`XxxServiceImpl`。
- **事务**: 在Service层使用`@Transactional`,注意`propagation`和`rollbackFor`。
- **业务异常**: 抛出自定义业务异常(如`BusinessException`),由全局异常处理器统一处理。

### 5.3 Repository 层
- **JPA**: 继承`JpaRepository`,方法命名遵循规范。
- **禁止**: 直接在Controller中调用Repository。
- **自定义查询**: 使用`@Query`注解或方法命名规范。


### 5.4 配置类
- **配置类**: 使用`@Configuration`,配置属性使用`@ConfigurationProperties`,避免`@Value`散落各处。
- **环境区分**: 使用`application-{profile}.properties`管理不同环境配置。

---

## 6. 异常处理

### 6.1 统一异常处理
使用`@RestControllerAdvice` + `@ExceptionHandler`进行全局异常处理。


### 6.2 自定义异常
继承`RuntimeException`,包含错误码和错误信息。

### 6.3 错误码定义
定义统一错误码枚举,如`COMMON_0001`、`USER_1001`。

---

## 7. 日志规范

### 7.1 日志框架
使用SLF4J + Logback。

### 7.2 日志级别
- `ERROR`: 系统错误、重要异常
- `WARN`: 降级处理、不期望但可恢复
- `INFO`: 关键流程、启动信息、接口入参/出参(生产环境谨慎)
- `DEBUG`: 调试信息

### 7.3 打印要求
- 禁止使用`System.out.println`。
- 使用占位符,避免字符串拼接。

### 7.4 异常打印
必须输出堆栈信息。

---

## 8. 注释规范

### 8.1 类注释
每个类都要有Javadoc,说明类的功能和作者。

### 8.2 方法注释
公共方法必须包含Javadoc,说明参数、返回值和异常。

### 8.3 复杂逻辑注释
必须添加行内注释,说明为什么这么写。

---

## 9. 安全规范

### 9.1 敏感信息管理
- 密码、Token、密钥等绝对禁止硬编码。
- 使用配置文件或环境变量管理敏感信息。
- 使用`application-{profile}.properties`区分环境。

### 9.2 SQL注入防护
- 使用JPA的参数化查询,禁止字符串拼接。
- 使用`@Query`时使用`:param`占位符,禁止使用字符串拼接。

```java
// 正确示例
@Query("SELECT d FROM DoctorUser d WHERE d.email = :email")
Optional<DoctorUser> findByEmail(@Param("email") String email);

// 错误示例(禁止)
@Query("SELECT d FROM DoctorUser d WHERE d.email = '" + email + "'")
```

### 9.3 XSS防护
- 前端输出时转义。
- 后端可配置`XssFilter`。
- 对用户输入进行校验和过滤。

### 9.4 接口鉴权
- 使用Spring Security进行统一拦截。
- 禁止在Controller中重复鉴权。
- 使用JWT或Session管理用户会话。

---

## 10. 依赖与版本管理

### 10.1 统一版本管理
在`pom.xml`的`<properties>`中统一管理第三方依赖版本。

```xml
<properties>
    <java.version>17</java.version>
    <spring-boot.version>3.5.7</spring-boot.version>
    <mysql-connector.version>8.0.33</mysql-connector.version>
</properties>
```

### 10.2 SpringBoot版本
- 使用稳定正式版,避免使用SNAPSHOT。
- 当前项目使用Spring Boot 3.5.7。

### 10.3 依赖范围
- 合理使用`provided`、`test`等scope。
- 避免将测试依赖打包到生产环境。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 10.4 核心依赖
本项目主要依赖:
- `spring-boot-starter-web`: Web应用开发
- `spring-boot-starter-data-jpa`: JPA数据访问
- `spring-boot-starter-actuator`: 应用监控
- `mysql-connector-j`: MySQL驱动

---

## 11. 测试规范

### 11.1 单元测试
- 测试类以`Test`结尾,放在`src/test/java`对应包下。
- 使用JUnit 5 + Mockito进行测试。
- 测试覆盖率要求达到80%以上。

```java
@ExtendWith(MockitoExtension.class)
class DoctorUserServiceTest {
    
    @Mock
    private DoctorUserRepository repository;
    
    @InjectMocks
    private DoctorUserServiceImpl service;
    
    @Test
    void testCreateDoctorUser() {
        // given
        DoctorUserRequest request = new DoctorUserRequest();
        // when
        DoctorUserResponse response = service.createDoctorUser(request);
        // then
        assertNotNull(response);
    }
}
```

### 11.2 集成测试
- 使用`@SpringBootTest`进行集成测试。
- 使用H2内存数据库或Testcontainers进行测试。

---

## 12. 数据库规范

### 12.1 Entity类规范
- 使用`@Entity`注解标记实体类。
- 使用`@Table`指定表名。
- 主键使用`@Id`和`@GeneratedValue`。

```java
@Entity
@Table(name = "doctor_users")
public class DoctorUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    // other fields, getters, setters
}
```

### 12.2 字段命名
- 使用小写字母+下划线: `doctor_name`, `created_at`
- 避免使用数据库保留字。

### 12.3 索引规范
- 为常用查询字段添加索引。
- 为外键字段添加索引。
- 避免过多索引影响写入性能。

---

## 13. API接口规范

### 13.1 URL规范
- 使用RESTful风格。
- 使用小写字母和连字符。
- 版本号放在URL中: `/api/v1/doctor-users`

### 13.2 HTTP方法
- `GET`: 查询资源
- `POST`: 创建资源
- `PUT`: 更新资源(全量更新)
- `PATCH`: 更新资源(部分更新)
- `DELETE`: 删除资源

### 13.3 响应格式
统一使用JSON格式响应。

```json
{
  "code": "SUCCESS",
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "张医生",
    "email": "doctor@example.com"
  }
}
```

---

## 14. 版本控制规范

### 14.1 分支管理
- `main`: 主分支,生产环境代码
- `develop`: 开发分支
- `feature/*`: 功能分支
- `hotfix/*`: 热修复分支

### 14.2 提交信息
使用规范的提交信息格式:

```
<type>(<scope>): <subject>

<body>

<footer>
```

**type类型**:
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具相关


