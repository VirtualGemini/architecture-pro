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
| Infrastructure | architecture-pro-infrastructure | 配置、持久化实现、Web、安全 |
| Starter | architecture-pro-starter | 启动入口、配置文件 |

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

## 开发规范

详见 `DEVELOPMENT_SPEC.md`。

## License

MIT
