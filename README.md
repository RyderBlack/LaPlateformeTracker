# Student Tracking System

A simple Java application for tracking students and their academic performance using a MySQL database.

## Features

- Manage administrators (CRUD operations)
- Manage students (CRUD operations)
- Manage study domains for students
- Search and filter functionality
- Secure authentication
- Connection pooling for database operations

## Prerequisites

- Java 21 or later
- PostgreSQL 12 or later
- Maven 3.6 or later

## Database Setup

1. Create a PostgreSQL database named `student_tracker`:
   ```sql
   CREATE DATABASE student_tracker;
   ```
2. Update the database configuration in `src/main/resources/application.properties` with your PostgreSQL credentials

## Installation

1. Clone the repository
2. Build the project:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   mvn exec:java -Dexec.mainClass="app.tracky.TrackyApp"
   ```

## Project Structure

```
src/main/java/app/tracky/
├── TrackyApp.java                 # Main application class
├── config/                       # Configuration classes
├── controller/                   # Controller classes (MVC)
├── dao/                          # Data Access Object interfaces
│   └── impl/                     # DAO implementations
├── database/                     # Database connection and utilities
├── exception/                    # Custom exceptions
├── model/                        # Model classes (entities)
├── service/                      # Service interfaces
│   └── impl/                     # Service implementations
└── util/                         # Utility classes

src/main/resources/
├── db/migration/                 # Database migration scripts
│   └── V1__Initial_schema.sql    # Initial database schema
└── application.properties        # Application configuration
```

## Usage

1. Start the application
2. The application will create the necessary tables and insert sample data
3. Use the provided services to interact with the application

## Example

```java
// Get service instances
AdminService adminService = ServiceFactory.getAdminService();
StudentService studentService = ServiceFactory.getStudentService();
StudyDomainService studyDomainService = ServiceFactory.getStudyDomainService();

// Create a new student
Student student = new Student("John", "Doe", 20, 15.5);
student = studentService.create(student);

// Add study domains
StudyDomain math = new StudyDomain("Mathematics", 16.0);
math.setStudent(student);
studyDomainService.create(math);

// Find students by grade range
List<Student> students = studentService.findByGradeRange(15.0, 17.0);
```

## Security

- Passwords should be hashed before storing in the database (currently stored as plaintext for simplicity)
- Use prepared statements to prevent SQL injection
- Connection pooling is implemented for better performance

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
