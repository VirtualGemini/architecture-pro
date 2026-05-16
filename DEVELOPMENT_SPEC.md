# Velox Development Spec

本文档约束 `velox-pro` 当前已经落地的四层结构，并明确后续新增模块的拆分规则。

## 1. 当前层级

当前项目采用四层：

- `velox-dependencies`
  - 统一第三方版本与模块版本管理
- `velox-common`
  - 业务共享返回、异常、多语言文案与 DDD 基类
- `velox-framework`
  - 只放可直接复用的能力 starter
  - 不依赖 `velox-common`
- `velox-bootstrap`
  - 只做产品启动装配与依赖聚合
  - 不写业务用例
- `velox-system`
  - 只放业务能力
- `velox-server`
  - 只放启动入口与应用配置

依赖方向必须保持单向：

`velox-dependencies`
-> `velox-common`
-> `velox-framework/*`
-> `velox-bootstrap`
-> `velox-system`
-> `velox-server`

禁止反向依赖、禁止循环依赖、禁止把业务代码重新塞回 framework 或 starter。

## 2. 当前已落地的 framework starters

当前收敛原则：

- `framework` 只保留“外部项目单独依赖就有明确价值”的能力模块
- 纯依赖治理统一并回 `velox-dependencies`
- 技术零件级拆分只允许存在于模块内部包结构，不再单独升格为 Maven 模块

当前 `velox-framework` 下实际存在：

- `velox-web-starter`
- `velox-security-starter`
- `velox-persistence-starter`
- `velox-file-starter`
- `velox-email-starter`
- `velox-id-generator-starter`
- `velox-redis-starter`

约束：

- framework 只能提供完整能力，不能只暴露半成品零件
- 一个 starter 可以在模块内部组合多项技术细节，但对外必须是单一能力入口
- 纯第三方依赖模块禁止继续以 `starter` 名义存在于 framework
- starter 不允许依赖 `velox-common`
- 禁止新增 `velox-core-starter`、`velox-all-starter` 这类无边界聚合模块
- 禁止为了技术拆分而新增 `velox-mysql-starter`、`velox-postgresql-starter`、`velox-redis-driver-starter` 这类对外无消费价值的模块

## 3. bootstrap 的职责

`velox-bootstrap` 当前是装配层，不是业务层。

当前职责：

- 聚合 framework 能力 starter
- 作为 system 的基础设施入口
- 保持 system 不直接拼接通用装配细节
- 当前仅保留一个产品级组合模块：
  - `velox-bootstrap-persistence`
- 不放 entity / mapper / business error code / business enum

bootstrap 模块只做三件事：

- 声明业务级扫描策略
- 组合 framework 原子能力并暴露当前产品默认入口
- 承载少量无法通用化的产品装配规则

## 4. system 的职责

`velox-system` 只放业务能力，当前包括：

- 认证登录
- 用户管理
- 角色权限
- 菜单能力
- 文件资源管理

包结构约束：

- `com.velox.module.system.auth`
- `com.velox.module.system.user`
- `com.velox.module.system.role`
- `com.velox.module.system.menu`
- `com.velox.module.system.permission`
- `com.velox.module.system.file`

禁止项：

- system 新增 framework 组合逻辑
- system 新增 starter 自动装配逻辑
- controller 直接依赖 Mapper
- 一个业务模块直接依赖另一个业务模块的实现类

## 5. 配置规则

当前代码已经完成的是模块拆分，不是配置命名空间迁移。

因此现阶段仍保留已有低层配置入口：

- `velox.datasource.*`
- `velox.security.*`
- `velox.email.*`
- `velox.file.*`

后续目标才是统一收口到：

```yaml
velox:
  infra:
    persistence:
    cache:
    lock:
    file:
    mail:
```

没有完成迁移前，不要在文档里把 `velox.infra.*` 写成“已实现”。

## 6. 自动装配规则

带运行时装配逻辑的 starter 才需要提供：

- 自己的 `pom.xml`
- 自己的 `AutoConfiguration`
- 自己的 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

纯依赖壳模块不允许继续留在 `velox-framework`。如果只是版本管理或三方依赖收口，统一放回 `velox-dependencies`。

禁止：

- 扫描整个 `com.velox`
- framework starter 互相做大范围 `@ComponentScan`
- 在自动装配类中写业务逻辑

当前实现中：

- framework 里的能力 starter 只装配自己
- `velox-bootstrap` 负责把 persistence 等能力组合成当前产品默认形态
- 组合配置统一放在 `com.velox.module.bootstrap.*`

## 7. 新增模块判定标准

判断一个新模块该放哪层：

放 framework：

- 它是不是一个对外可直接消费的完整能力
- 它能不能脱离业务独立复用

例如：

- Web 基础能力
- 安全基础能力
- 持久化能力
- 邮件发送
- 文件存储抽象
- Redis 缓存能力

放 bootstrap：

- 它是不是在做产品级装配和扫描策略

例如：

- Mapper 扫描与业务级持久化组合
- 当前产品默认能力接线方式

放 system：

- 它是不是业务用例
- 它是不是当前产品的数据模型或 provider 实现

例如：

- 登录注册
- 用户管理
- 角色菜单
- 文件资源管理

## 8. 当前重构边界

本次已经完成：

- `velox-common` 提升为顶层业务共享模块
- capability starter 平铺到 `velox-framework/*`
- `velox-bootstrap` 收敛为 `velox-bootstrap-persistence`
- `velox-file-starter` 只保留 SPI、通用 provider 与自动装配
- 数据库存储 provider 与 `FileContent` 实体/mapper 下沉到 `velox-system`
- `velox-server` 作为唯一启动模块
- `velox-system` 继续只承载业务实现

本次没有完成：

- `velox.infra.*` 配置命名空间迁移
- 将 system 中仍然直接使用的部分三方类进一步收口到 infra 门面

后续新增能力时，优先遵守一句话：

`common` 提供业务共享语言，`framework` 提供可插拔能力，`bootstrap` 负责产品装配，`system` 只做业务与产品实现。
