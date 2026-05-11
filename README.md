# jwt-auth-core

A production-ready REST API built to demonstrate a robust **JSON Web Token (JWT)** authentication architecture using **Spring Boot** and **Spring Security**. Designed as a reference implementation for developers who want to understand how to implement stateless authentication with access/refresh token rotation, role-based access control, and secure password recovery.

---

## What this project demonstrates

- **Stateless authentication** using signed JWTs (no server-side sessions)
- **Access token + Refresh token** strategy with rotation on every refresh
- **Refresh token reuse detection** — if a revoked token is reused, all user sessions are invalidated
- **Logout** (single session) and **Logout-all** (all active sessions) flows
- **Role-based access control (RBAC)** using Spring Security's `@PreAuthorize` with `USER` and `ADMIN` roles
- **Password recovery via email** — code-based flow with expiration, attempt limits, and a short-lived JWT reset token
- **Hexagonal architecture (Ports & Adapters)** — the domain is fully decoupled from infrastructure
- **Clean separation of concerns** across domain, application, and infrastructure layers

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.4 |
| Security | Spring Security 6 |
| JWT | jjwt (io.jsonwebtoken) 0.11.5 |
| Persistence | Spring Data JPA + PostgreSQL |
| Email | Spring Boot Mail (JavaMailSender) |
| Validation | Jakarta Bean Validation |
| Testing | JUnit 5 + Mockito + Spring Security Test |
| Build | Gradle |

---

## Architecture

The project follows **Hexagonal Architecture** (also known as Ports & Adapters):

```
src/main/java/com/jwtauthcore/
├── domain/
│   ├── model/          # Pure domain entities (User, RefreshToken, PasswordResetCode, Role)
│   └── ports/          # Interfaces that define what the domain needs
├── application/
│   ├── usecase/        # One class per use case (single responsibility)
│   ├── service/        # Shared application services (ServiceToken, ServiceRandomCode)
│   ├── dto/            # Request and response DTOs
│   └── exception/      # Domain-level exceptions
└── infrastructure/
    ├── controllers/    # REST endpoints (AuthController, UserController)
    ├── security/       # JWT filter, JwtService, UserPrincipal, LogoutService
    ├── adapters/       # Port implementations (JwtAdapter, UserRepositoryAdapter, etc.)
    ├── jpa/            # JPA entities and Spring Data repositories
    ├── config/         # Spring Security config, bean wiring
    └── exception/      # Global exception handler
```

The domain layer has **zero dependencies** on Spring or any framework. All framework-specific code lives in the infrastructure layer and is wired through the ports.

---

## Authentication flow

### Register / Login
```
POST /auth/register   →  Returns access token + refresh token
POST /auth/login      →  Returns access token + refresh token
```

### Token refresh (rotation)
```
POST /auth/refresh    →  Validates refresh token, issues new pair, revokes old one
```
Every refresh invalidates the previous refresh token and issues a new one. If a revoked token is reused, **all sessions for that user are revoked** (reuse detection).

### Logout
```
POST /auth/logout      →  Revokes the current refresh token (Authorization header)
POST /auth/logout-all  →  Revokes all active refresh tokens for the user
```

### Password recovery
```
POST /auth/password-reset/request        →  Sends a 6-digit code to the user's email
POST /auth/password-reset/validate-code  →  Validates the code, returns a short-lived reset JWT
POST /auth/password-reset/new-password   →  Uses the reset JWT to set a new password
```
The reset code expires in 15 minutes and is blocked after 3 failed attempts.

---

## Protected endpoints (RBAC example)

```
PATCH /api/users/update/password   →  Requires ROLE_USER
PATCH /api/users/update/me         →  Requires ROLE_USER
```

Access control is enforced via `@PreAuthorize("hasRole('USER')")` at the method level, enabled by `@EnableMethodSecurity`.

---

## Running locally

### Prerequisites
- Java 21
- PostgreSQL running (or update `application.properties` to use H2 for local dev)
- SMTP credentials for email (password recovery)

### Configuration
Copy `.env.example` to `.env` and fill in:
```
JWT_SECRET=<base64-encoded-secret-min-256-bits>
DB_URL=jdbc:postgresql://localhost:5432/jwtauthcore
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password
MAIL_HOST=smtp.example.com
MAIL_USERNAME=your@email.com
MAIL_PASSWORD=your_email_password
```

### Build and run
```bash
./gradlew bootRun
```

### Run tests
```bash
./gradlew test
```

---

## Key design decisions

**Why refresh token rotation?**
A stolen refresh token can only be used once. The moment the legitimate user refreshes, the attacker's copy becomes invalid. If the attacker refreshes first, the reuse detection kicks in and invalidates all sessions.

**Why store refresh tokens hashed?**
The token stored in the database is a SHA-256 hash of the actual token. Even if the database is compromised, the raw tokens cannot be extracted and replayed.

**Why hexagonal architecture?**
It makes the JWT and security logic independently testable without spinning up a full Spring context. The use cases are plain Java classes that depend only on interfaces.
