# CHEKO Backend

High-level overview of the CHEKO restaurant management system backend.

## Core Technologies

- **Java 17**
- **Spring Boot**
- **PostgreSQL**

## Key Features

### 1. Database Failure Mechanism

The system implements a robust database connection retry mechanism that:
- Automatically detects database connection failures
- Implements exponential backoff retry strategy
- Provides health check endpoints for monitoring
- Logs connection status and recovery attempts

### 2. Aspect-Oriented Programming (AOP)

AOP is used to separate cross-cutting concerns from business logic:
- Logging aspects for method entry/exit and parameters
- Performance monitoring for method execution time

### 3. Exception Handling

Comprehensive exception handling framework:
- Centralized exception handling with GlobalExceptionHandler
- Custom exceptions for different error scenarios
- Consistent error response format
- Validation error handling

### 4. Rate Limiting

Protection against API abuse:
- Request rate limiting per client/IP
- Configurable rate limits for different endpoints
- Custom rate limit exceeded exceptions
- Header-based rate limit information

### 5. API Documentation

Interactive API documentation:
- Swagger UI integration at `/swagger-ui.html`
- OpenAPI specifications
- Detailed endpoint documentation
- Request/response examples

### 6. Health Checks

Comprehensive health monitoring:
- Application health endpoints
- Database connection health checks
- Detailed status information
- Integration with monitoring systems

### 7. State Design Pattern

The order processing system uses the State Design Pattern to:
- Model order status transitions (PENDING → PREPARING → READY → etc.)
- Encapsulate state-specific behavior in separate classes
- Allow for clean state transitions without complex conditional logic

## Environment Support

- Development
- Testing
- Production