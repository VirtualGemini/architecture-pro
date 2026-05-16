<h2 align="center" id="top">Arc Pro Backend</h2>
<p align="center">A DDD-oriented backend for the Arc Pro admin system, covering authentication, authorization, user-role-menu management, file services, and pluggable email capabilities.</p>
<div align="center">English | <a href="./README.zh-CN.md">简体中文</a></div>

<br />

## Overview

`velox` is the backend of the `arc-pro` project. It is built around a layered DDD structure and provides the real API foundation for the admin frontend derived from `art-design-pro`.

It includes:

- authentication and session management
- user, role, and menu management
- backend-driven permission control
- file storage and file configuration management
- captcha and email-based password recovery
- unified response, exception, logging, and tracing infrastructure

## Tech Stack

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
- AWS SDK S3 compatible client

## Module Structure

```text
velox-pro/
├── velox-common          # Common result, exception, enums, logging annotations
├── velox-domain          # DDD domain model, repository contracts, domain services, events
├── velox-email           # Pluggable email auto-configuration and sending module
├── velox-infrastructure  # Web, persistence, security, file, logging, integration layer
└── velox-starter         # Spring Boot startup module
```

## Functional Coverage

### 1. Common Infrastructure

- Unified response wrappers: `Result`, `PageResult`
- Error code hierarchy and `ApiException`
- Global exception handling
- Trace ID propagation and request logging
- Operation logging via annotation and AOP

### 2. Authentication and Security

- Captcha generation: `GET /api/auth/captcha`
- Login and logout: `POST /api/auth/login`, `POST /api/auth/logout`
- Registration: `POST /api/auth/register`
- Password recovery by email verification code:
  - `POST /api/auth/forgot-password/code`
  - `POST /api/auth/forgot-password/reset`
- Token-based authentication via `Sa-Token`
- Password policy and legacy hash upgrade support
- Redis-backed verification code storage using HMAC digest instead of plaintext

### 3. Current User Center

- Get current user info: `GET /api/user/info`
- Update profile: `PUT /api/user/profile`
- Update password: `PUT /api/user/password`
- Update avatar: `PUT /api/user/avatar`

### 4. System Management

- User management:
  - `GET /api/user/list`
  - `POST /api/user`
  - `PUT /api/user/{userId}`
  - `DELETE /api/user/{userId}`
- Role management:
  - `GET /api/role/list`
  - `POST /api/role`
  - `PUT /api/role/{roleId}`
  - `DELETE /api/role/{roleId}`
  - `GET /api/role/{roleId}/menu-permissions`
  - `PUT /api/role/{roleId}/menu-permissions`
- Menu management:
  - `GET /api/v3/system/menus/simple`
  - `POST /api/v3/system/menus`
  - `PUT /api/v3/system/menus/{menuId}`
  - `DELETE /api/v3/system/menus/{menuId}`

### 5. File Services

- Backend upload flow: `POST /api/file/upload`
- Frontend direct upload flow with presigned URL:
  - `GET /api/file/presigned-url`
  - `POST /api/file/create`
- File querying and deletion:
  - `GET /api/file/get`
  - `GET /api/file/page`
  - `DELETE /api/file/delete`
  - `DELETE /api/file/delete-batch`
- File download and temporary access:
  - `GET /api/file/{configId}/get/**`
  - `GET /api/file/presigned-get-url`

Supported storage implementations in the codebase:

- local storage
- database storage
- S3-compatible object storage

### 6. File Configuration Management

- Create config: `POST /api/file-config/create`
- Update config: `PUT /api/file-config/update`
- Set master config: `PUT /api/file-config/update-master`
- Enable or disable config: `PUT /api/file-config/update-enabled`
- Query config: `GET /api/file-config/get`, `GET /api/file-config/page`
- Delete config: `DELETE /api/file-config/delete`, `DELETE /api/file-config/delete-batch`
- Connectivity test: `GET /api/file-config/test`

### 7. Email Module

The `velox-email` module is designed as a pluggable dependency.

Features:

- Spring Boot auto-configuration
- SMTP support
- async sending
- retry policy
- virtual-thread execution on JDK 25
- one-line builder-based sending API
 
## Architecture Notes

- REST APIs use the unified prefix `/api`
- Permission checks are based on `SaCheckPermission`
- The frontend consumes backend-provided menu data for dynamic routing
- Redis is used for captcha, password reset verification codes, and active user status
- Swagger/OpenAPI support exists but is disabled by default in development and production profiles

## Configuration

Key configuration areas:

- `application.yml`: shared defaults 
- `application-dev.yml`: development
- `application-test.yml`: test
- `application-prod.yml`: production

Important backend settings:

- server port: `8080`
- API prefix: `/api`
- default profile: `dev`
- token header: `Authorization`
- CORS origins configurable through `velox.security.cors`
 
## Local Development

Requirements:

- JDK 25+
- Maven 3.9+
- MySQL 8
- Redis 7

Start infrastructure with Docker Compose:

```bash
cd script/docker
docker compose up -d
```

Build the project:

```bash
mvn clean compile
```

Run the application:

```bash
cd velox-pro/velox-starter
mvn spring-boot:run
```

Package the application:

```bash
mvn clean package -DskipTests
```

## Frontend Integration

This backend is intended to work directly with the `art-design-pro` frontend inside the same repository.

Recommended local ports:

- Backend: `http://localhost:8080`
- Frontend: `http://localhost:3006`

The frontend uses Vite proxying and calls this backend through `/api`.

## License

MIT

<br>
<div align="center"><a href="#top">Back to Top</a></div>
<br>
