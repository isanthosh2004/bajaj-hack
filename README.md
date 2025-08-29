# Webhook Solver - Spring Boot Application (Even Questions )
##Santhosh Kumar S P 
## 22BCE3084
## EVEN REG NO 
## Jar file is in the realease - do check it out 
A Spring Boot 3.x application that automatically solves SQL problems for even registration numbers and submits solutions via webhooks on startup.

## Features

- **Automatic Execution**: Runs automatically on application startup using `ApplicationRunner`
- **Webhook Integration**: Generates webhooks and submits solutions via REST APIs
- **Even Question Only**: Specifically designed for even registration numbers (Question 2)
- **SQL Problem Solving**: Loads SQL solution from classpath resources
- **Data Persistence**: Stores submissions in H2 database with JPA
- **Error Handling**: Comprehensive error handling with retry mechanisms
- **Secure Logging**: Masks sensitive information in logs (tokens, names)

## Architecture

- **Spring Boot 3.2.0** with Java 17
- **WebFlux** for reactive HTTP client operations
- **Spring Data JPA** for data persistence
- **H2 Database** for in-memory storage
- **Lombok** for reducing boilerplate code

## Project Structure

```
src/main/java/com/example/webhooksolver/
├── config/           # Configuration classes and properties
├── dto/             # Data Transfer Objects for API requests/responses
├── entity/          # JPA entities (Submission)
├── repository/      # Data access layer
├── service/         # Business logic services
├── runner/          # Application startup runner
└── WebhookSolverApplication.java
```

## Configuration

The application is configured via `application.yml`:

```yaml
app:
  candidate:
    name: "John Doe"
    regNo: "REG12347"
    email: "john@example.com"
  
  endpoints:
    generate-webhook: "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA"
    default-submit: "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA"
```

## Workflow

1. **Startup** → Application starts and triggers workflow
2. **Generate Webhook** → POST request to generate webhook endpoint
3. **Question Assignment** → Automatically assigned to Question 2 (EVEN)
4. **SQL Solution** → Loads SQL from classpath resources
5. **Data Persistence** → Saves submission to H2 database
6. **Solution Submission** → POSTs final SQL query to webhook URL
7. **Status Update** → Updates submission status based on response

## SQL Answer File

- `src/main/resources/answers/even.sql` - Solution for Question 2 (even registration numbers)

## Question 2 Details

- **Google Drive Link**: https://drive.google.com/file/d/143MR5cLFrlNEuHzzWJ5RHnEWuijuM9X/view?usp=sharing
- **Assignment Type**: EVEN
- **Target**: Registration numbers ending with even digits

## Problem Statement

Calculate the number of employees who are younger than each employee, grouped by their respective departments.

### Tables:
1. **DEPARTMENT**: Contains department details (DEPARTMENT_ID, DEPARTMENT_NAME)
2. **EMPLOYEE**: Contains employee details (EMP_ID, FIRST_NAME, LAST_NAME, DOB, GENDER, DEPARTMENT)
3. **PAYMENTS**: Contains salary payment records (PAYMENT_ID, EMP_ID, AMOUNT, PAYMENT_TIME)

### Output Format:
- EMP_ID: The ID of the employee
- FIRST_NAME: The first name of the employee
- LAST_NAME: The last name of the employee
- DEPARTMENT_NAME: The name of the department the employee belongs to
- YOUNGER_EMPLOYEES_COUNT: The number of employees who are younger than the respective employee in their department

### Solution:
The SQL query uses self-joins to compare employee ages within the same department and counts younger employees for each employee.

## Build and Run

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build

```bash
mvn clean compile
```

### Run

```bash
mvn spring-boot:run
```

### Run with JAR

```bash
mvn clean package
java -jar target/webhook-solver-1.0.0.jar
```

## Database Access

- **H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

## Logging

The application provides detailed logging for each stage:

- Webhook generation
- Question assignment (EVEN)
- SQL solution loading
- Database operations
- Solution submission
- Status updates

Sensitive information (tokens, names) is masked in logs for security.

## Error Handling

- **Retry Mechanism**: Automatic retry with exponential backoff
- **Fallback SQL**: Default `SELECT 1;` if answer file is missing
- **Graceful Degradation**: Continues execution even if some steps fail

## Customization

### Modifying SQL Solution

Edit the SQL file in `src/main/resources/answers/even.sql` directory.

### Changing Configuration

Modify `application.yml` or create environment-specific profiles.

## API Endpoints

The application doesn't expose any REST endpoints - it only makes outbound calls to:

- Generate webhook endpoint
- Submit solution endpoint

## Security Features

- JWT token handling in Authorization header
- Token masking in logs
- No sensitive data exposure in application endpoints

## Monitoring

- Application metrics via Spring Boot Actuator (if added)
- H2 database console for data inspection
- Comprehensive logging for debugging

## Troubleshooting

### Common Issues

1. **Network Errors**: Check internet connectivity and endpoint availability
2. **File Not Found**: Ensure even.sql exists in resources directory
3. **Database Errors**: Verify H2 database configuration
4. **JWT Issues**: Check token format and validity

### Debug Mode

Enable debug logging by setting log level to DEBUG in `application.yml`.

## License

This project is for educational and demonstration purposes.
