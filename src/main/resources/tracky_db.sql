-- Create tables
CREATE TABLE IF NOT EXISTS admin (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS student (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    grade DECIMAL(4,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS study_domain (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    grade DECIMAL(4,2) NOT NULL,
    student_id INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

-- To create an admin user, run the following SQL with your own secure password:
-- IMPORTANT: Replace 'your_secure_password' with a strong password and 'your_email@example.com' with the admin's email
-- The password should be hashed using BCrypt before being stored in the database
-- Example (password will be hashed in the application code):
-- INSERT INTO admin (username, email, password) 
-- VALUES ('admin', 'your_email@example.com', 'your_secure_password');
