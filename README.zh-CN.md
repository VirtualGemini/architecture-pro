<h2 align="center" id="top">Arc Pro 后端</h2>
<p align="center">面向 Arc Pro 管理系统的 DDD 风格后端，覆盖认证鉴权、用户角色菜单管理、文件服务以及可插拔邮件能力。</p>
<div align="center"><a href="./README.md">English</a> | 简体中文</div>

<br />

## 项目概述

`velox-pro` 是 `velox` 项目的后端部分，采用分层 DDD 结构构建，为基于 `art-design-pro` 改造的管理后台前端提供真实 API 支撑。

当前后端覆盖：

- 登录认证与会话管理
- 用户、角色、菜单管理
- 后端驱动的权限控制
- 文件存储与文件配置管理
- 验证码与邮箱找回密码
- 统一返回、异常、日志与链路追踪基础设施

## 技术栈

- Java 25
- Spring Boot 3.4.5
- Maven 3.9+
- MyBatis-Plus 3.5.12
- MySQL 8
- Redis 7
- Sa-Token 1.40.0
- SpringDoc OpenAPI 2.8.6
- Spring Validation
- Spring AOP
- Hutool
- EasyCaptcha
- JavaMail
- AWS SDK S3 兼容客户端

## 模块结构

```text
velox-pro/
├── velox-dependencies    # 统一第三方版本管理
├── velox-common          # 业务共享返回、异常、多语言文案与 DDD 基类
├── velox-framework/
│   ├── velox-web-starter
│   ├── velox-security-starter
│   ├── velox-persistence-starter
│   ├── velox-file-starter
│   ├── velox-email-starter
│   ├── velox-id-generator-starter
│   └── velox-redis-starter
├── velox-bootstrap/
│   └── velox-bootstrap-persistence   # 产品级 Mapper 扫描与装配策略
├── velox-system          # 业务模块
└── velox-server          # Spring Boot 启动模块
```

## 功能全览

### 1. 通用基础设施

- 统一返回结构：`Result`、`PageResult`
- 错误码分层与 `ApiException`
- 全局异常处理
- Trace ID 链路追踪与请求日志
- 基于注解与 AOP 的操作日志

### 2. 认证与安全

- 图形验证码：`GET /api/auth/captcha`
- 登录与退出：`POST /api/auth/login`、`POST /api/auth/logout`
- 用户注册：`POST /api/auth/register`
- 邮箱验证码找回密码：
  - `POST /api/auth/forgot-password/code`
  - `POST /api/auth/forgot-password/reset`
- 基于 `Sa-Token` 的 Token 认证
- 密码策略与旧哈希自动升级支持
- 验证码使用 Redis 存储 HMAC 摘要，不落明文

### 3. 当前用户中心

- 获取当前用户信息：`GET /api/user/info`
- 更新个人资料：`PUT /api/user/profile`
- 修改密码：`PUT /api/user/password`
- 更新头像：`PUT /api/user/avatar`

### 4. 系统管理

- 用户管理：
  - `GET /api/user/list`
  - `POST /api/user`
  - `PUT /api/user/{userId}`
  - `DELETE /api/user/{userId}`
- 角色管理：
  - `GET /api/role/list`
  - `POST /api/role`
  - `PUT /api/role/{roleId}`
  - `DELETE /api/role/{roleId}`
  - `GET /api/role/{roleId}/menu-permissions`
  - `PUT /api/role/{roleId}/menu-permissions`
- 菜单管理：
  - `GET /api/v3/system/menus/simple`
  - `POST /api/v3/system/menus`
  - `PUT /api/v3/system/menus/{menuId}`
  - `DELETE /api/v3/system/menus/{menuId}`

### 5. 文件服务

- 后端直传：`POST /api/file/upload`
- 前端直传 + 预签名地址：
  - `GET /api/file/presigned-url`
  - `POST /api/file/create`
- 文件查询与删除：
  - `GET /api/file/get`
  - `GET /api/file/page`
  - `DELETE /api/file/delete`
  - `DELETE /api/file/delete-batch`
- 文件下载与临时访问：
  - `GET /api/file/{configId}/get/**`
  - `GET /api/file/presigned-get-url`

代码中已支持的文件存储实现：

- 本地存储
- 数据库存储
- S3 兼容对象存储

其中数据库存储 provider 实现位于 `velox-system`，`velox-file-starter` 只保留 SPI、通用 provider 与自动装配。

### 6. 文件配置管理

- 创建配置：`POST /api/file-config/create`
- 更新配置：`PUT /api/file-config/update`
- 设为主配置：`PUT /api/file-config/update-master`
- 启用或停用：`PUT /api/file-config/update-enabled`
- 配置查询：`GET /api/file-config/get`、`GET /api/file-config/page`
- 配置删除：`DELETE /api/file-config/delete`、`DELETE /api/file-config/delete-batch`
- 连通性测试：`GET /api/file-config/test`

### 7. 邮件模块

`velox-email-starter` 是一个可热插拔能力 starter。

能力包括：

- Spring Boot 自动配置
- SMTP 支持
- 异步发送
- 失败重试
- JDK 25 虚拟线程执行
- Builder 风格一行发送 API

## 架构说明

- 所有 REST 接口统一使用 `/api` 前缀
- 权限校验基于 `SaCheckPermission`
- 前端动态路由依赖后端返回的菜单数据
- Redis 用于图形验证码、重置密码验证码以及在线状态维护
- Swagger/OpenAPI 能力已接入，但在开发与生产环境默认关闭

## 配置说明

核心配置文件：

- `application.yml`：公共默认配置
- `application-dev.yml`：开发环境
- `application-test.yml`：测试环境
- `application-prod.yml`：生产环境

重要配置项：

- 服务端口：`8080`
- API 前缀：`/api`
- 默认环境：`dev`
- Token 请求头：`Authorization`
- 跨域来源：通过 `velox.web.cors` 配置

 
## 本地开发

环境要求：

- JDK 25+
- Maven 3.9+
- MySQL 8
- Redis 7

使用 Docker Compose 启动基础依赖：

```bash
cd script/docker
docker compose up -d
```

编译项目：

```bash
mvn clean compile
```

启动应用：

```bash
cd velox-pro/velox-server
mvn spring-boot:run
```

打包：

```bash
mvn clean package -DskipTests
```

## 前端对接

该后端默认与同仓库下的 `art-design-pro` 前端配套使用。

推荐本地端口：

- 后端：`http://localhost:8080`
- 前端：`http://localhost:3006`

前端通过 Vite 代理以 `/api` 前缀访问本后端。

## License

MIT

<br>
<div align="center"><a href="#top">回到顶部</a></div>
<br>
