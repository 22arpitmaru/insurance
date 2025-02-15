# Insurance Claim Service

## Overview
This is a Spring Boot microservice for managing customers and their insurance claims. It provides features such as:
- Creating, updating, and retrieving customer details.
- Managing and tracking insurance claims.
- Secure and efficient database integration with MySQL.
- Well-documented APIs with Swagger UI.

## Technologies Used
- **Java 17**
- **Spring Boot 3.x**
- **MySQL**
- **JUnit 5** (for testing)
- **Mockito** (for mocking)
- **SLF4J** (for logging)
- **Postman collection** (for manual API testing)

## Prerequisites
Before running this application, ensure you have the following installed:
- **Java 17+**
- **Maven**
- **MySQL**
- **An IDE** (IntelliJ, Eclipse, or Spring Tool Suite recommended)

## Installation and Setup
### 1. Clone the Repository
```bash
git clone https://github.com/22arpitmaru/insurance.git
cd insurance
```

### 2. Import the Project
- Open your favorite IDE (IntelliJ, Eclipse, STS).
- Import the cloned project as a **Maven Project**.

### 3. Set Up the Database
- Import the provided **database dump** into MySQL.
- Update **`src/main/resources/application.properties`** with your MySQL credentials:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/insurance_db
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  ```

### 4. Build and Run the Application
```bash
mvn clean install
mvn spring-boot:run
```


## Running Tests
To execute unit tests:
```bash
mvn test
```

## Support
For any issues, please open an issue on [GitHub](https://github.com/22arpitmaru/insurance/issues).
