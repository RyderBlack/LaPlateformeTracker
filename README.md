# LaPlateformeTracker

A student tracking application for La Plateforme.

## Setup Instructions

### Database Setup

1. Create a new PostgreSQL database
2. Run the SQL script to create the necessary tables:
   ```sql
   psql -U your_username -d your_database -f src/main/resources/tracky_db.sql
   ```

### Configuration

1. Copy `src/main/resources/application.properties.example` to `src/main/resources/application.properties`
2. Update the database connection details in `application.properties`

### Creating an Admin User

After setting up the database, you'll need to create an admin user. You can do this by:

1. Using the application's registration feature (if implemented)
2. Or by running an SQL insert (make sure to hash the password with BCrypt first)

Example SQL (replace placeholders with actual values):
```sql
-- The password should be hashed with BCrypt before inserting
-- For example, 'admin123' becomes '$2a$10$N9qo8uLOickgx2ZMRZoMy...'
INSERT INTO admin (username, email, password) 
VALUES ('admin', 'admin@example.com', 'hashed_password_here');
```

### Running the Application

1. Build the project: `mvn clean install`
2. Run the application: `mvn spring-boot:run`

## Security Note

- Never commit sensitive information like passwords or API keys to version control
- Always use environment variables or configuration files that are in `.gitignore` for sensitive data
- The application should be run behind HTTPS in production
