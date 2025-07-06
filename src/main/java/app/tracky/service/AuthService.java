package app.tracky.service;

import app.tracky.database.DatabaseConnection;
import app.tracky.util.PasswordUtils;

import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AuthService {
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    private final Connection connection;
    
    public AuthService() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }
    
    public boolean register(String username, String email, String password) {
        // Basic validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        
        String salt = PasswordUtils.generateSalt();
        String hashedPassword = PasswordUtils.hashPassword(password, salt);
        
        String sql = "INSERT INTO admin (username, email, password, salt) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, salt);
            
            logger.info("Attempting to register user: " + username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Successfully registered user: " + username);
                return true;
            } else {
                logger.warning("No rows affected when registering user: " + username);
                return false;
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error during registration for user: " + username, e);
            if (e.getMessage().contains("duplicate key")) {
                if (e.getMessage().contains("username")) {
                    throw new IllegalArgumentException("Username already exists");
                } else if (e.getMessage().contains("email")) {
                    throw new IllegalArgumentException("Email already registered");
                }
            }
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during registration for user: " + username, e);
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }
    
    public boolean login(String identifier, String password) {
        logger.info("Login attempt for: " + identifier);
        String sql = "SELECT id, username, password, salt FROM admin WHERE username = ? OR email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password");
                String salt = rs.getString("salt");
                
                logger.info("Found user in database");
                logger.info("Stored hash: " + storedHash);
                logger.info("Salt: " + salt);
                
                // Log the input password and generated hash for debugging
                String generatedHash = PasswordUtils.hashPassword(password, salt);
                logger.info("Generated hash from input: " + generatedHash);
                
                boolean isValid = PasswordUtils.verifyPassword(password, salt, storedHash);
                
                if (isValid) {
                    logger.info("Password verification successful");
                    // Update last login timestamp
                    updateLastLogin(rs.getInt("id"));
                } else {
                    logger.warning("Password verification failed for: " + identifier);
                    logger.warning("Expected hash: " + storedHash);
                    logger.warning("Generated hash: " + generatedHash);
                }
                
                return isValid;
            } else {
                logger.warning("No user found with identifier: " + identifier);
            }
            
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Login failed", e);
        }
    }
    
    private void updateLastLogin(int userId) {
        String sql = "UPDATE admin SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            logger.log(Level.INFO, "Updated last login for user ID: {0}", userId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Failed to update last login for user ID: %d", userId), e);
        }
    }
    
    public String getUsername(String identifier) {
        String sql = "SELECT username FROM admin WHERE username = ? OR email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("username");
            }
            
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch username", e);
        }
    }
    
    public void close() {
        logger.info("Closing AuthService and database connection");
        DatabaseConnection.closeConnection(connection);
        logger.info("AuthService closed successfully");
    }
}
