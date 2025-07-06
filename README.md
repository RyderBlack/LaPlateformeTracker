# Tracky - La Plateforme Student Management System

A comprehensive student tracking and management application for La Plateforme, built with JavaFX and PostgreSQL.

## ✨ Features

- **Secure Authentication**
  - User registration and login with email/username
  - Password hashing with salt
  - Session management
  - Role-based access control (coming soon)

- **Student Management**
  - View student profiles
  - Track student progress
  - Manage enrollments
  - Generate reports

- **User Experience**
  - Modern, responsive UI
  - Form validation
  - Helpful error messages
  - Smooth transitions and animations

## 🚀 Getting Started

### Prerequisites

- Java 21 or later
- Maven 3.6.3 or later
- PostgreSQL 14 or later
- Git (for version control)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/LaPlateformeTracker.git
   cd LaPlateformeTracker
   ```

2. **Set up the database**
   ```bash
   # Create a new PostgreSQL database
   createdb tracky_db
   
   # Run migrations (Flyway will handle this automatically on first run)
   # Or manually run the SQL script:
   # psql -U your_username -d tracky_db -f src/main/resources/db/migration/V1__initial_schema.sql
   ```

3. **Configure the application**
   ```bash
   # Copy the example config file
   cp src/main/resources/app/tracky/config/application.properties.example application.properties
   
   # Edit the configuration
   # nano application.properties
   ```

4. **Build and run**
   ```bash
   # Build the project
   mvn clean install
   
   # Run the application
   mvn javafx:run
   ```

## ⚙️ Configuration

The application looks for configuration in the following order:
1. `config.properties` in the current working directory
2. `config.properties` in the classpath
3. Environment variables (prefixed with `TRACKY_`)
4. Default values

### Important Configuration Options

```properties
# Database connection
db.url=jdbc:postgresql://localhost:5432/tracky_db
db.username=postgres
db.password=your_secure_password

# Connection pool settings
db.pool.minIdle=5
db.pool.maxIdle=10
db.pool.maxTotal=25
db.pool.maxWaitMillis=10000

# Application settings
app.name=Tracky
app.version=1.0.0

# Logging
logging.level=INFO
logging.file=logs/tracky.log
```

## 🛠 Development

### Project Structure

```
src/main/java/app/tracky/
├── Main.java                 # Application entry point
├── config/                   # Configuration classes
├── controller/               # JavaFX controllers
├── model/                    # Data models
├── service/                  # Business logic
├── util/                     # Utility classes
└── view/                     # FXML views
```

### Building

```bash
# Build the project
mvn clean install

# Run tests
mvn test

# Create a runnable JAR
mvn package
```

### Code Style

This project follows the Google Java Style Guide with the following exceptions:
- Line length: 120 characters
- Indentation: 4 spaces

## 📚 Documentation

- [JavaDoc](https://your-docs-url.com/javadoc)
- [API Documentation](https://your-docs-url.com/api)
- [Wiki](https://github.com/yourusername/LaPlateformeTracker/wiki)

## 🤝 Contributing

Contributions are welcome! Please read our [contributing guidelines](CONTRIBUTING.md) to get started.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [JavaFX](https://openjfx.io/)
- [PostgreSQL](https://www.postgresql.org/)
- [Maven](https://maven.apache.org/)
- [Flyway](https://flywaydb.org/)
- [DBCP2](https://commons.apache.org/proper/commons-dbcp/)

## 📧 Contact

For questions or support, please contact [your-email@example.com](mailto:your-email@example.com).

---

<div align="center">
  Made with ❤️ by Your Name
</div>
