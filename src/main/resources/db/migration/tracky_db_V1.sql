-- Create tables
CREATE TABLE IF NOT EXISTS admin (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create a function to update the updated_at column
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create student table
CREATE TABLE IF NOT EXISTS student (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    grade DECIMAL(4,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create study_domain table
CREATE TABLE IF NOT EXISTS study_domain (
    id SERIAL PRIMARY KEY,
    study_domain_name VARCHAR(100) NOT NULL,
    study_domain_grade DECIMAL(4,2) NOT NULL,
    student_id INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student
        FOREIGN KEY (student_id) 
        REFERENCES student(id) 
        ON DELETE CASCADE
);

-- Create triggers to update updated_at
CREATE TRIGGER update_admin_updated_at
BEFORE UPDATE ON admin
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_student_updated_at
BEFORE UPDATE ON student
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_study_domain_updated_at
BEFORE UPDATE ON study_domain
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert sample admin user (password: admin123)
INSERT INTO admin (username, email, password) 
VALUES ('admin', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi')
ON CONFLICT (username) DO UPDATE 
SET email = EXCLUDED.email, 
    password = EXCLUDED.password,
    updated_at = CURRENT_TIMESTAMP;
