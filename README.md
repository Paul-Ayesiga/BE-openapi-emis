# EMIS Agent Application - Setup & Running Guide ( PREFERRED IDE -- IntelliJ IDEA )

## 📋 Table of Contents
- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Project Setup](#project-setup)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Development Tools](#development-tools)
- [Project Structure](#project-structure)
- [Spring Boot References](#spring-boot-references)

## 🚀 Project Overview

The EMIS (Education Management Information System) Agent Application is a Spring Boot 3.5.3 application built with Java 21. It provides RESTful APIs for managing educational data including learners and schools, with integrated Kafka messaging for agentic functionality.

**Key Features:**
- RESTful API for learner and school management
- PostgreSQL database integration with JPA/Hibernate
- Apache Kafka messaging for agent tasks
- OpenAPI/Swagger documentation
- Multi-profile configuration (dev, test, prod)
- Database seeding functionality
- Health monitoring with Spring Actuator

## 🔧 Prerequisites

Before setting up the project, ensure you have the following installed:

### Required Software
- **Java 21** or higher
  ```bash
  java -version
  # Should show Java 21 or higher
  ```
- **Apache Maven 3.6+**
  ```bash
  mvn -version
  ```
- **PostgreSQL 12+**
  ```bash
  psql --version
  ```
- **Apache Kafka** (for messaging functionality)
- **Git** (for version control)

### Optional but Recommended
- **IntelliJ IDEA** or **Eclipse** IDE
- **Postman** or **Insomnia** for API testing
- **Docker** and **Docker Compose** (for containerized setup)

## 🛠 Project Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd <parent directory>
```

### 2. Verify Java Version
```bash
# Check Java version
java -version

# If using SDKMAN (recommended for Java version management)
sdk install java 21.0.2-open
sdk use java 21.0.2-open
```

### 3. Maven Setup
The project includes Maven wrapper, so you can use either:
```bash
# Using Maven wrapper (recommended)
./mvnw clean install

# Or using system Maven
mvn clean install
```

## 🗄 Database Setup

### PostgreSQL Setup

1. **Install PostgreSQL** (if not already installed)
   ```bash
   # macOS (using Homebrew)
   brew install postgresql
   brew services start postgresql
   
   # Ubuntu/Debian
   sudo apt update
   sudo apt install postgresql postgresql-contrib
   sudo systemctl start postgresql
   
   # Windows
   # Download from https://www.postgresql.org/download/windows/
   ```

2. **Create Database and User**
   ```bash
   # Connect to PostgreSQL
   psql -U postgres
   
   # Create database
   CREATE DATABASE emis_db;
   
   # Create user (optional, or use existing postgres user)
   CREATE USER emis_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE emis_db TO emis_user;
   
   # Exit psql
   \q
   ```

3. **Verify Database Connection**
   ```bash
   psql -U postgres -d emis_db -c "SELECT version();"
   ```

### Alternative: H2 Database (Development Only)
For quick development/testing, you can use H2 in-memory database by uncommenting the H2 configuration in `application.yml` under the dev profile.

## ⚙️ Configuration

### Application Configuration
The application uses `application.yml` for configuration with multiple profiles:

1. **Database Configuration** (Update if needed)
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/emis_db
       username: postgres
       password: < password >  # Update this password
   ```

2. **Profile-Specific Settings**
   - `dev` - Development profile with debug logging
   - `test` - Testing profile with H2 database
   - `prod` - Production profile with minimal logging

### Environment Variables (Optional)
You can override configuration using environment variables:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/emis_db
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
export SPRING_PROFILES_ACTIVE=dev
```

### Kafka Configuration (Optional)
If using Kafka functionality, ensure Kafka is running:
```bash
# Start Kafka (example using Kafka installation)
# Start Zookeeper first
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka server
bin/kafka-server-start.sh config/server.properties
```

## 🚀 Running the Application

### Option 1: Using Maven Wrapper (Recommended)
```bash
# Run with default profile (dev)
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Option 2: Using Java JAR
```bash
# Build the JAR
./mvnw clean package

# Run the JAR
java -jar target/emis_app-0.0.1-SNAPSHOT.jar

# Run with specific profile
java -jar target/emis_app-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### Option 3: Using IDE
1. Import the project into your IDE
2. Run the `EmisAppApplication.java` main class
3. Set VM options if needed: `-Dspring.profiles.active=dev`

### Verify Application is Running
```bash
# Check application health
curl http://localhost:8080/actuator/health

# Expected response:
# {"status":"UP"}
```

## 📚 API Documentation

### Swagger UI
Once the application is running, access the interactive API documentation:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Available Endpoints
- **Learners API**: `/api/learners`
- **Schools API**: `/api/schools`
- **System API**: `/api/system`
- **Health Check**: `/actuator/health`

### Example API Calls
```bash
# Get all learners
curl http://localhost:8080/api/learners

# Get all schools
curl http://localhost:8080/api/schools

# Create a new learner
curl -X POST http://localhost:8080/api/learners \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john.doe@example.com"}'
```

## 🧪 Testing

### Running Tests
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=LearnerControllerTest

# Run tests with coverage
./mvnw test jacoco:report
```

### Test Profiles
- Tests automatically use the `test` profile
- H2 in-memory database is used for testing
- Test data is created and destroyed for each test

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. Database Connection Issues
```bash
# Check if PostgreSQL is running
pg_ctl status

# Restart PostgreSQL
brew services restart postgresql  # macOS
sudo systemctl restart postgresql  # Linux
```

#### 2. Port Already in Use
```bash
# Check what's using port 8080
lsof -i :8080

# Kill the process or change port in application.yml
server:
  port: 8081
```

#### 3. Java Version Issues
```bash
# Check Java version
java -version

# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/java21
```

#### 4. Maven Build Issues
```bash
# Clean and rebuild
./mvnw clean install -U

# Skip tests if needed
./mvnw clean install -DskipTests
```

#### 5. Database Schema Issues
- The application uses `ddl-auto: create-drop` in dev mode
- Database schema is recreated on each startup
- For production, change to `ddl-auto: validate`

### Logs and Debugging
- Application logs are written to `logs/emis-app.log`
- Console logs show SQL queries in dev mode
- Increase logging level in `application.yml` if needed

## 🛠 Development Tools

### Useful Maven Commands
```bash
# Clean build
./mvnw clean compile

# Run without tests
./mvnw spring-boot:run -DskipTests

# Debug mode
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Generate dependency tree
./mvnw dependency:tree

# Check for updates
./mvnw versions:display-dependency-updates
```

### Database Tools
```bash
# Connect to database
psql -U postgres -d emis_db

# Show tables
\dt

# Describe table structure
\d learners
\d schools
```

### Monitoring Endpoints
- **Health**: http://localhost:8080/actuator/health
- **Info**: http://localhost:8080/actuator/info
- **Metrics**: http://localhost:8080/actuator/metrics
- **Environment**: http://localhost:8080/actuator/env

## 📁 Project Structure

```
emis_app/
├── src/main/java/com/emis_app/emis_app/
│   ├── EmisAppApplication.java          # Main application class
│   ├── GlobalExceptionHandler.java     # Global error handling
│   ├── config/
│   │   └── OpenApiConfig.java          # Swagger/OpenAPI configuration
│   ├── controller/                     # REST controllers
│   │   ├── LearnerController.java      # Learner management APIs
│   │   ├── SchoolController.java       # School management APIs
│   │   └── SystemController.java       # System/utility APIs
│   ├── dto/                           # Data Transfer Objects
│   ├── entity/                        # JPA entities
│   ├── repository/                    # Data access layer
│   ├── service/                       # Business logic layer
│   ├── kafka/                         # Kafka producers/consumers
│   └── seeder/                        # Database seeding
├── src/main/resources/
│   ├── application.yml                # Application configuration
│   ├── static/                        # Static web content
│   └── templates/                     # Template files
├── src/test/                          # Test files
├── logs/                              # Application logs
├── target/                            # Build output
├── pom.xml                            # Maven configuration
└── HELP.md                            # This file
```

## 📝 Additional Notes

### Development Best Practices
1. **Use Profiles**: Always specify the appropriate profile for your environment
2. **Database Migrations**: Consider using Flyway or Liquibase for production
3. **Security**: Add Spring Security for production deployments
4. **Monitoring**: Use the actuator endpoints for application monitoring
5. **Testing**: Write tests for all new features

### Production Deployment
- Change `ddl-auto` to `validate` or use database migrations
- Configure appropriate logging levels
- Set up proper database connection pooling
- Configure security settings
- Use environment-specific configuration files

### Getting Help
- Check the application logs in `logs/emis-app.log`
- Use the Swagger UI for API documentation
- Review Spring Boot documentation for framework-specific issues
- Check PostgreSQL logs for database-related issues

## 📚 Spring Boot References

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.3/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.3/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.3/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/3.5.3/reference/messaging/kafka.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.3/reference/using/devtools.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

---

**Version**: 0.0.1-SNAPSHOT  
**Spring Boot**: 3.5.3  
**Java**: 21  
**Last Updated**: September 2025
