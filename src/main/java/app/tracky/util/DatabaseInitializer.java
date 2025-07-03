package app.tracky.util;

import app.tracky.database.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    private static final String SQL_SCRIPT = "/db/migration/tracky_db_V1.sql";
    
    public static void initializeDatabase() {
        try (InputStream inputStream = DatabaseInitializer.class.getResourceAsStream(SQL_SCRIPT);
             Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            
            if (inputStream == null) {
                throw new RuntimeException("Database initialization script not found: " + INIT_SCRIPT);
            }
            
            // Read the SQL script
            String sql = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            
            // Split the script into individual statements, handling PostgreSQL functions and triggers
            String[] statements = sql.split(";\\s*\\n");
            StringBuilder currentStatement = new StringBuilder();
            
            for (String line : statements) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                currentStatement.append(line).append("\n");
                
                // Check if this is a complete statement (not inside a function or other block)
                if (line.endsWith("$$") || line.endsWith("END;") || line.endsWith("END") || 
                    line.endsWith("END IF;") || line.endsWith("END IF") ||
                    line.endsWith("END LOOP;") || line.endsWith("END LOOP")) {
                    continue;
                }
                
                if (line.endsWith(";")) {
                    String stmt = currentStatement.toString().trim();
                    if (!stmt.isEmpty()) {
                        statement.execute(stmt);
                    }
                    currentStatement = new StringBuilder();
                }
            }
            
            connection.commit();
            System.out.println("Database initialized successfully");
            
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Initializing database...");
        initializeDatabase();
        System.out.println("Database initialization completed.");
    }
}
