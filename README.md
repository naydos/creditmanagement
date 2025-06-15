# Credit Management API

A Spring Boot project for managing loans, installments, and payments for customers.

### Prerequisites

- Java 17+
- Maven 3.6+
- IntelliJ IDEA or any preferred IDE

### Run the application

```bash
mvn spring-boot:run
```

### Access H2 Console

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:test`
- User: `sa`
- Password: (leave empty)

---

##  API Documentation

This project provides RESTful APIs for managing customer loans, payments, and installments.

###  Swagger UI

You can explore and test the API endpoints interactively via Swagger:

🔗 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

##  Postman Collection

You can import the ready-to-use Postman collection to test the API endpoints:

📄 [CreditManagement.postman_collection.json](./CreditManagement.postman_collection.json)


### Steps:

1. Open Postman.
2. Click **Import** > **Upload Files**.
3. Choose the file: `CreditManagement.postman_collection.json`.
4. Collection name: **Credit**
5. Use `admin` / `admin123` as credentials for basic auth (already included).

---

##  Security

- Basic HTTP authentication is enabled.
- Two in-memory users are defined:
  - `admin` / `admin123` (ROLE_ADMIN)
  - `customer` / `cust123` (ROLE_CUSTOMER)

> Role-based access control can be extended for customer-level access control.

---

##  Tests

You can run all tests with:

```bash
mvn clean test
```

---

##  Technologies

- Spring Boot 3.2.5
- Spring Security
- Spring Data JPA
- H2 Database
- Swagger (springdoc-openapi)
- MapStruct
- Lombok