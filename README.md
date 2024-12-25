# Todo Service Application

Welcome to the **Todo Service Application**, a Spring Boot based RESTful API for managing Todo items. This application allows users to create, read, update, and delete Todo items while leveraging features like caching, exception handling, API performance monitoring, and Swagger documentation.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Exception Handling](#exception-handling)
- [Performance Monitoring](#performance-monitoring)
- [Swagger Documentation](#swagger-documentation)
- [Contact](#contact)

---

## Features
- **CRUD Operations**: Create, Read, Update, and Delete Todo items.
- **Caching**: Uses Redis to cache Todo items for faster retrieval.
- **Exception Handling**: Global exception handler for consistent error responses.
- **Performance Monitoring**: Logs API execution time for better performance tracking.
- **Validation**: Jakarta Bean Validation (JSR 303) to ensure valid input.
- **Swagger Documentation**: Interactive API documentation using Swagger UI.

---

## Technologies Used
- **Spring Boot 3.4.1**: For building the RESTful API.
- **PostgreSQL**: For persistent data storage.
- **Redis**: For caching data and improving performance.
- **Jakarta Validation**: For input validation on Todo request DTO.
- **Swagger OpenAPI**: For API documentation and testing.
- **JUnit 5 & Mockito**: For unit and integration testing.

---

## Setup and Installation

### Prerequisites:
- JDK 21 or higher
- Maven (if you prefer using Maven instead of the `mvnw` wrapper)
- PostgreSQL (for persistent data storage)
- Redis (for caching)

### 1. Clone the repository:
```bash

git clone https://github.com/thedillip/todo-service.git 
```
### 2. Navigate into the project directory:
```bash

cd todo-service
```
### 3. Set up your environment variables for PostgreSQL and Redis:

- PostgreSQL URL, username, and password.
- Redis connection details.

You can configure these settings in ``` application.properties ``` or ``` application.yml ``` under ``` src/main/resources. ```

### 4. Build the Project:
```bash

./mvnw clean install
```
### 5. Run the application:
```bash

./mvnw spring-boot:run
```
Alternatively, you can run the application from your IDE (e.g., IntelliJ IDEA).

---

## API Endpoints

### 1. Create Todo

- ***Endpoint:*** ``` POST /api/v1/todos ```
- ***RequestBody:***
```json
 {
    "title": "Sample Todo",
    "description": "Description of the task",
    "status": "PENDING"
 }
```
- ***Response:*** ``` 201 Created ``` with the created Todo item.

### 2. Get Todo by ID

- ***Endpoint:*** ``` GET /api/v1/todos/{todoId} ```
- ***Response:*** ``` 200 OK ``` with the Todo item.

### 3. Get All Todos

- ***Endpoint:*** ``` GET /api/v1/todos ```
- ***Response:*** ``` 200 OK ``` with a list of all Todo items.

### 4. Update Todo
- ***Endpoint:*** ``` PUT /api/v1/todos/{todoId} ```
- ***Request Body:***

```json
 {
    "title": "Updated Todo Title",
    "description": "Updated description",
    "status": "COMPLETED"
 }
```
- ***Response:*** ``` 200 OK ``` with the updated Todo item.

### 5. Delete Todo
- ***Endpoint:*** ``` DELETE /api/v1/todos/{todoId} ```
- ***Response:*** ``` 204 No Content ``` after successfully deleting the Todo item.

---

## Testing

### Unit Tests
The project includes unit tests to ensure the proper functioning of services, controllers, and repositories. These tests are written using **JUnit 5** and **Mockito** for mocking dependencies.

### Integration Tests
Integration tests ensure that the entire application works together as expected. We test the real interaction between controllers, services, and the database.

### Code Coverage
Jacoco is used to track code coverage during the testing process. You can view the code coverage report after running the tests.

To generate the Jacoco report:
```bash

./mvnw clean test jacoco:report
```

---

## Exception Handling
The application uses a ***Global Exception Handler*** to manage all exceptions and return consistent error responses. Common exceptions include:

- ***BadRequestException:*** 400 Bad Request for invalid input.
- ***NotFoundException:*** 404 Not Found when resources are not found.
- ***MethodArgumentTypeMismatchException:*** 400 Bad Request for invalid parameter types.
- ***InternalServerError:*** 500 Internal Server Error for unexpected issues.

Here is an example response for a ``` BadRequestException ```:

```json
 {
    "error": "Bad Request",
    "message": "The provided data is invalid."
 }
```

---

## Performance Monitoring
API execution times are logged for every request, helping to track performance issues. The time taken for each request is logged in milliseconds.

Example logs:

```text
2024-12-25 17:43:14.967 INFO  [nio-8080-exec-6] c.t.interceptor.ResponseTimeInterceptor : Incoming request: [GET /api/v1/todos/abc123]
2024-12-25 17:43:14.973 INFO  [nio-8080-exec-6] c.t.interceptor.ResponseTimeInterceptor : API [GET /api/v1/todos/abc123] executed in 6 ms
```

---

## Swagger Documentation
Swagger UI is available to interact with the API. To access the documentation:

1. Run the application.
2. Open your browser and go to: ``` http://localhost:8080/swagger-ui.html ```

You will see all the available API endpoints and can test them directly from the browser.

---

## Contact

For any issues, suggestions, or inquiries, feel free to reach out to me:

- **Email**: [jrdillip@gmail.com](mailto:jrdillip@gmail.com)
- **GitHub**: [@thedillip](https://github.com/thedillip)

I'm always happy to connect and help!



