# 开发规范（DDD 聚合项目）

本文档用于统一本项目（`Architecture Pro` 多模块、DDD 分层）的开发方式、代码组织、接口与数据规范，降低协作成本与架构腐化风险。

> 适用范围：`architecture-pro-common` / `architecture-pro-domain` / `architecture-pro-infrastructure` / `architecture-pro-starter` 全模块。

---

## 1. 总体原则

1. **先建模再编码**：优先明确领域概念、聚合边界与不变量，再落地接口与表结构。
2. **分层清晰、依赖单向**：避免跨层“偷懒式”直连导致耦合扩散。
3. **领域规则内聚**：业务规则优先沉淀在聚合根/领域服务中；应用服务负责编排。
4. **接口稳定、实现可替换**：领域层定义仓储接口，基础设施层实现（DB/缓存/第三方）。
5. **统一返回与异常**：Controller 只返回 `Result`；业务错误通过 `ApiException` + `ErrorCode` 表达。
6. **可观测与可审计**：日志、traceId、操作日志可用且不泄漏敏感信息。

---

## 2. 模块分层与依赖规则

### 2.1 模块职责

- `architecture-pro-common`：通用能力（`Result`、异常/错误码、日志注解/常量、通用枚举/工具）。
- `architecture-pro-domain`：领域层（聚合/实体/值对象、领域服务接口、仓储接口、领域事件）。
- `architecture-pro-infrastructure`：基础设施层（Web、配置、MyBatisPlus、Mapper、三方集成、安全、AOP）。
- `architecture-pro-starter`：启动模块（Spring Boot 启动类、配置文件、日志配置、测试依赖）。

### 2.2 依赖方向（必须遵守）

建议保持如下单向依赖（当前工程即为该结构）：

`starter` → `infrastructure` → `domain` → `common`

禁止项：

- `domain` 依赖 `infrastructure`
- `common` 依赖 `domain` / `infrastructure` / `starter`
- `controller` 直接依赖 Mapper（绕过仓储/服务）

---

## 3. 业务域与包结构规范

随着业务增长，请按 **有界上下文（Bounded Context）** 拆分包结构。建议目录示例（仅示意，可按实际域命名）：

- 领域层：`com.architecturepro.domain.<bc>`
  - `model`：聚合根/实体/值对象
  - `repository`：仓储接口
  - `service`：领域服务（仅接口，若实现不依赖基础设施可放领域层）
    - `impl`：领域服务实现
  - `event`：领域事件
- 基础设施层：`com.architecturepro.infrastructure.<bc>`
  - `persistence`：Mapper、DO/PO（如有）、仓储实现
  - `web`：Controller、DTO、应用服务（UseCase）
  - `client`：三方 API/SDK 适配
  - `config`：配置类、Properties

说明：

- DTO（入参/出参）统一放在 `...web.<bc>.dto` 下，不与领域模型混用。
- 单体阶段可以先用包隔离，后续再上升为独立模块（如 `architecture-pro-<bc>-domain`）。

---

## 4. 领域建模与聚合规范（DDD 核心）

### 4.1 聚合边界

- **一个聚合 = 一个一致性边界**：在一次事务中必须保持强一致的对象应归入同一聚合。
- 跨聚合引用：**只允许通过对方聚合根的 ID 引用**，禁止持有对象引用，避免级联加载与跨边界修改。
- 聚合根（继承 `BaseAggregate`）对外暴露行为方法，内部实体只能通过聚合根访问和变更。

### 4.2 不变量（必须）

- 任何需要长期维持的业务约束（如状态机、配额、唯一性规则）都要在聚合根内校验并封装为行为方法。
- 禁止在 Controller / Mapper 层“散落式校验”核心不变量（会导致规则分裂）。

### 4.3 值对象（Value Object）

推荐用于表达不可变概念（如手机号、金额、邮箱、地址等）：

- 不可变（只读）；
- 通过构造/工厂方法校验合法性；
- `equals/hashCode` 基于值语义。

### 4.4 领域事件（可选但推荐）

当出现跨聚合/跨模块协作时，优先使用领域事件进行解耦：

- 事件命名：`<Something>Created` / `<Something>Changed` / `<Something>Deleted`
- 事件承载：只包含必要字段（ID、关键属性、时间戳），避免大对象透传
- 事件处理：基础设施层消费事件并完成通知、异步任务、审计等

---

## 5. 应用服务（Use Case）规范

> 本模板中可将用例服务放在 `architecture-pro-infrastructure`（例如 `...web.<bc>.service`）作为“应用层”，负责编排流程。

应用服务职责：

- 编排用例：校验入参 → 加载聚合 → 调用聚合行为 → 保存 → 发布事件/返回 DTO。
- 控制事务边界：需要强一致的用例在应用服务方法上使用 `@Transactional`。
- 不承载复杂领域规则：复杂规则回归聚合根/领域服务。

禁止项：

- 在应用服务直接拼装 SQL 或写复杂查询逻辑（应下沉到仓储/Mapper）
- 在应用服务返回领域实体给 Controller（应转换为 DTO）

---

## 6. 仓储与持久化规范（MyBatis Plus）

### 6.1 仓储接口（领域层）

- 仓储接口定义在 `architecture-pro-domain`（继承/参考 `BaseRepository`）。
- 方法命名体现领域语言：`findById` / `save` / `deleteById` 等。
- 查询结果为空的处理：允许返回 `null`，但应用服务必须显式处理并抛出业务错误码。

### 6.2 Mapper（基础设施层）

- Mapper 统一放在 `architecture-pro-infrastructure` 的 `persistence` 包下。
- Mapper 命名：`<Aggregate>Mapper`，只做数据访问，不承载业务含义。
- XML 位置：`classpath*:mapper/**/*.xml`（如使用 XML）。

### 6.3 表与字段约定

- 表名：`<业务前缀>_<实体>`，示例：`sys_user`
- 主键：`id`（BIGINT 自增或雪花，保持全局一致）
- 审计字段：`create_time` / `update_time` / `create_by` / `update_by`
- 逻辑删除：`deleted`（配合 MyBatisPlus 逻辑删除配置）
- 字段命名：数据库使用 `snake_case`，Java 使用 `camelCase`（由 MyBatis 配置转换）

### 6.4 领域模型与持久化模型

本项目允许“领域模型直接作为持久化实体”以提升开发效率，但需要遵守：

- 持久化注解（如 `@TableName`）保持最小化，不把复杂映射逻辑污染到领域行为方法里；
- 领域行为与持久化操作必须分离：领域对象不直接调用 Mapper。

若业务复杂度上升，建议引入 `DO/PO` 与领域对象分离，并通过仓储实现完成转换。

---

## 7. Web/API 规范

### 7.1 Controller 规范

- Controller 只做：鉴权/校验/调用应用服务/返回 `Result`。
- 入参 DTO：
  - `Command`：写操作（创建/更新/删除）
  - `Query`：读操作（列表/详情/检索）
- 出参 DTO：
  - `DTO`：对外展示结构
  - `PageResult<T>`：分页返回（如项目已提供）
- 统一使用 `@Valid`/`jakarta.validation` 进行参数校验。
- Swagger 注解：`@Tag`、`@Operation`、`@Schema` 为公开接口补齐描述与示例。

### 7.2 URL 与方法约定（REST）

- 资源名用复数：`/users`、`/roles`
- 方法语义：
  - `GET /resources/{id}`：查询
  - `POST /resources`：创建
  - `PUT /resources/{id}`：全量更新（若采用）
  - `PATCH /resources/{id}`：部分更新（推荐）
  - `DELETE /resources/{id}`：删除

### 7.3 返回体规范

所有接口返回 `Result<T>`：

- 成功：`Result.ok(data)` / `Result.ok()`
- 失败：通过抛出 `ApiException`，由 `GlobalExceptionHandler` 统一转成 `Result.fail(...)`

禁止项：

- Controller 中大量 `try/catch` 并手工拼 `Result.fail`、以及 Service 业务逻辑
- 直接暴露异常栈给客户端

---

## 8. 异常与错误码规范（强制）

### 8.1 使用方式

- 业务失败统一抛 `ApiException(ErrorCode, args...)`。
- 错误码定义在 `architecture-pro-common` 的枚举中（`BusinessErrorCode` 等）。
- 新增错误码时必须同步新增 i18n 文案：
  - `architecture-pro-infrastructure/src/main/resources/i18n/messages.properties`
  - `architecture-pro-infrastructure/src/main/resources/i18n/messages_en.properties`

### 8.2 分段规则（建议遵循 README 约定）

- `200/400`：通用成功/失败（`CommonErrorCode`）
- `0-99`：内部错误（`InternalErrorCode`）
- `100-999`：客户端错误（`ClientErrorCode`）
- `10000-99999`：业务错误（`BusinessErrorCode`，按模块分段）

---

## 9. 日志、链路追踪与操作审计规范

### 9.1 统一要求

- 使用 SLF4J：`private static final Logger log = LoggerFactory.getLogger(...)`
- 不记录敏感信息：密码、验证码答案、Token、身份证号等（必要时做脱敏/摘要）。
- 正常流程用 `info`，异常兜底用 `error`，可预期业务异常可用 `warn`。

### 9.2 traceId

- `TraceIdFilter` 负责为每个请求生成/透传 `X-Trace-Id` 并写入 MDC（key：`LogConstants.TRACE_ID`）。
- 业务日志输出必须确保能关联 traceId（依赖 logback pattern + MDC）。

### 9.3 操作日志

- 对关键用例方法（如创建/更新/删除）使用 `@OperationLog` 标注。
- `OperationLogAspect` 负责记录操作描述、类型、耗时、成功与否等。

---

## 10. 安全规范（Sa-Token & 账号安全）

### 10.1 Token

- 客户端通过 `Authorization` 传递 token（见配置 `sa-token.token-name`）。
- Controller 不直接操作 token 细节，统一通过认证框架能力获取登录信息。

### 10.2 密码存储（强制）

- 生产环境禁止使用纯 MD5；必须使用带盐的强哈希（如 BCrypt/Argon2/PBKDF2）。
- 禁止把明文密码、哈希结果写入日志或返回给客户端。

> 注：示例代码中出现的 MD5 仅用于模板演示，落地业务时必须替换。

---

## 11. 数据库与配置管理

- 配置分环境：`application-dev.yml` / `application-test.yml` / `application-prod.yml`
- 配置项新增需提供默认值与说明（必要时通过 `ArchitectureProProperties` 管理）
- SQL 变更需要：
  - 保持向后兼容（尽量不删字段，采用新增字段 + 灰度迁移）
  - 明确索引与约束（唯一性、外键是否需要）
  - 提供初始化或升级脚本（`sql/` 目录）

---

## 12. 测试规范

- 单元测试：聚合根/领域服务优先，覆盖不变量与状态流转。
- 集成测试：Controller + 应用服务 + DB（可选 TestContainers）。
- 测试命名：`*Test`；一个测试方法只验证一个行为点。
- 断言风格：Arrange / Act / Assert（AAA）。

---

## 13. Git 协作与提交规范

### 13.1 分支

- `main`：稳定可发布
- `dev`（如采用）：日常集成
- `feat/<bc>-<topic>`：功能开发
- `fix/<topic>`：缺陷修复

### 13.2 Commit message（建议）

推荐采用简洁前缀：

- `feat: ...` 新功能
- `fix: ...` 修复
- `refactor: ...` 重构（无行为变更）
- `test: ...` 测试
- `docs: ...` 文档
- `chore: ...` 构建/脚本/依赖

### 13.3 Code Review（必须）

- 关键检查项：分层是否破坏、是否引入跨聚合修改、错误码/i18n 是否齐全、日志是否泄密、接口是否稳定。

---

## 14. 代码风格与可维护性

- 命名：类名 `UpperCamelCase`，方法/变量 `lowerCamelCase`，常量 `UPPER_SNAKE_CASE`。
- 空值处理：对外接口与仓储返回结果必须明确空值语义，禁止“默认吞掉错误”。
- DTO 与领域对象分离：DTO 不复用领域对象；领域对象不承担序列化视图职责。
- 禁止在代码中硬编码：
  - magic number（无语义的常量）
  - 环境相关配置（域名、端口、密钥）
- 实体类：禁止使用 Lombok 注解，建议手写 getter/setter 以明确领域行为与持久化属性的边界。

---

## 15. 新增一个聚合的落地清单（Checklist）

1. 在 `architecture-pro-domain` 定义聚合根（`BaseAggregate`）与不变量行为方法。
2. 定义仓储接口（领域层），必要时定义领域服务与领域事件。
3. 在 `architecture-pro-infrastructure` 新增 Mapper / 仓储实现 / 必要的配置与适配器。
4. 在 `architecture-pro-infrastructure` 新增应用服务（UseCase）与 DTO。
5. 新增 Controller 并补齐校验与 Swagger 注解。
6. 新增/更新错误码（`BusinessErrorCode` 等）并补齐 i18n 文案。
7. 为关键用例补齐测试与操作日志。
8. 更新 SQL 脚本（`sql/`）与必要文档。
