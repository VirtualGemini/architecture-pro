# architecture-pro - DDD Architecture

基于 **Spring Boot 3.4.5** + **JDK 25** 的 DDD（领域驱动设计）聚合项目架构。

## 项目结构

```
architecture-pro/
├── architecture-pro-common/              # 通用基础模块
│   └── com.architecturepro.common
│       ├── result/              # 统一返回机制 (Result, PageResult)
│       ├── exception/           # 异常体系 (ErrorCode, ApiException, i18n)
│       ├── log/                 # 日志注解与常量
│       └── enums/               # 通用枚举
│
├── architecture-pro-domain/              # 领域层
│   └── com.architecturepro.domain
│       ├── model/              # 领域模型 (BaseEntity, BaseAggregate, BaseValueObject)
│       ├── repository/         # 仓储接口 (BaseRepository)
│       ├── service/            # 领域服务接口
│       └── event/              # 领域事件 (BaseDomainEvent)
│
├── architecture-pro-email/              # 邮件模块（可热插拔）
│   └── com.architecturepro.email
│       ├── autoconfigure/      # 自动配置
│       ├── config/             # 邮件配置属性
│       ├── core/               # EmailBuilder / EmailSender
│       └── channel/            # SMTP 通道实现
│
├── architecture-pro-infrastructure/     # 基础设施层
│   └── com.architecturepro.infrastructure
│       ├── config/             # 配置 (MyBatisPlus, Application, Properties)
│       ├── persistence/        # 持久化 (BaseMapperExt)
│       ├── web/                # Web层 (GlobalExceptionHandler, TraceIdFilter, OperationLogAspect)
│       └── log/                # 日志拦截器
│
└── architecture-pro-starter/            # 启动模块
    └── com.architecturepro.starter
        └── ArchitectureProApplication  # 启动入口
```

## 核心设计

### 1. 统一返回机制 (Result)

```java
// 成功响应
Result.ok()
Result.ok(data)

// 失败响应
Result.fail()
Result.fail(ErrorCode)
Result.fail(BusinessErrorCode.USER_NOT_FOUND)
```

### 2. 异常处理体系

- **ErrorCode 接口**: 所有错误码的统一契约，支持 i18n
- **分级错误码**:
  - `CommonErrorCode` (0-1): 通用成功/失败
  - `InternalErrorCode` (0-99): 框架/基础设施错误
  - `ClientErrorCode` (100-999): 客户端请求错误
  - `BusinessErrorCode` (10000-99999): 业务逻辑错误（按模块分段）
- **ApiException**: 统一业务异常，携带 ErrorCode + i18n + payload
- **GlobalExceptionHandler**: 全局异常拦截，自动转换为 Result

### 3. 日志体系

- **Logback 异步日志**: 按级别分文件（info/error），异步写入
- **TraceId 链路追踪**: 每个请求自动生成 traceId，贯穿 MDC
- **@OperationLog 注解**: AOP 操作日志，自动记录方法执行信息
- **RequestLogInterceptor**: 请求日志拦截器，记录请求/响应摘要

### 4. DDD 分层

| 层 | 模块 | 职责 |
|---|---|---|
| Common | architecture-pro-common | 通用工具、Result、异常、日志注解 |
| Domain | architecture-pro-domain | 领域模型、仓储接口、领域服务、领域事件 |
| Email | architecture-pro-email | 邮件自动配置、发送器、虚拟线程异步发送 |
| Infrastructure | architecture-pro-infrastructure | 配置、持久化实现、Web、安全 |
| Starter | architecture-pro-starter | 启动入口、配置文件 |

### 5. 认证与找回密码

- 登录、注册接口位于 `POST /api/auth/login`、`POST /api/auth/register`
- 忘记密码已接入邮件验证码重置流程：
  - `POST /api/auth/forgot-password/code`：发送邮箱验证码
  - `POST /api/auth/forgot-password/reset`：校验验证码并重置密码
- 重置密码依赖 `sys_user.email` 字段，用户必须先绑定邮箱
- 邮件验证码默认 10 分钟有效，60 秒内限制重复发送

### 6. 邮件模块

- 采用独立子模块 `architecture-pro-email`，可随依赖启停，尽量保持热插拔
- 默认使用 SMTP 通道
- 基于 JDK 25 虚拟线程执行异步发送任务
- 业务侧可直接注入 `EmailBuilder` 实现一行发送：

```java
@Resource
private EmailBuilder emailBuilder;

emailBuilder.to("user@example.com")
        .subject("Test")
        .text("hello")
        .send();
```

## 快速开始

### 环境要求

- JDK 25+
- Maven 3.9+

### 编译

```bash
mvn clean compile
```

### 运行

```bash
cd architecture-pro-starter
mvn spring-boot:run
```

### 打包

```bash
mvn clean package -DskipTests
```

## 配置说明

- `application.yml`: 主配置
- `application-dev.yml`: 开发环境
- `application-test.yml`: 测试环境
- `application-prod.yml`: 生产环境
- `logback-spring.xml`: 日志配置

### 邮件配置

默认配置前缀为 `vg.lite-email`，典型示例：

```yml
vg:
  lite-email:
    enabled: true
    sender: ${MAIL_SENDER:}
    password: ${MAIL_PASSWORD:}
    ssl: true
    async:
      enabled: true
      virtual-threads: true
      concurrency-limit: 256
      thread-name-prefix: vg-email-
    retries:
      global-retries: 1
      max-retries: 3
      initial-delay: 1000
    logging:
      enabled: true
      level: INFO
```

说明：

- `enabled`：是否启用邮件模块
- `sender` / `password`：发件邮箱和授权码
- `ssl`：QQ 邮箱等 `465` 端口场景通常为 `true`
- `virtual-threads`：开启后使用 JDK 25 虚拟线程执行异步发信
- 未显式配置 `host/port` 时，会按邮箱域名自动推断 SMTP 服务

## 开发规范

详见 `DEVELOPMENT_SPEC.md`。

## License

MIT
