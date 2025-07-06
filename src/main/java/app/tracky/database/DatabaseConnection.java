package app.tracky.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "config.properties";
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = new java.io.FileInputStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }

            Properties prop = new Properties();
            prop.load(input);

            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");

            if (url == null || username == null || password == null) {
                throw new RuntimeException("Database configuration is missing required properties");
            }
            
            // Initialize database schema if needed
            initializeSchema();
            
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void executeUpdate(String query, Object... params) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
        }
    }

    public static <T> T executeQuery(String query, ResultSetHandler<T> handler, Object... params) 
            throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            
            try (ResultSet resultSet = statement.executeQuery()) {
                return handler.handle(resultSet);
            }
        }
    }

    private static void initializeSchema() throws SQLException {
        System.out.println("Initializing database schema...");
        
        // First, check if the table exists
        boolean tableExists = false;
        boolean needsRecreate = false;
        
        try (Connection conn = getConnection()) {
            // Check if table exists
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "admin", null)) {
                tableExists = rs.next();
                
                if (tableExists) {
                    System.out.println("Admin table exists, checking schema...");
                    // Check if the salt column exists
                    try (ResultSet columns = conn.getMetaData().getColumns(null, null, "admin", "salt")) {
                        if (!columns.next()) {
                            System.out.println("Admin table is missing 'salt' column, will recreate...");
                            needsRecreate = true;
                        }
                    }
                }
            }
            
            // Drop and recreate table if needed
            if (needsRecreate) {
                System.out.println("Dropping existing admin table...");
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("DROP TABLE IF EXISTS admin CASCADE");
                    System.out.println("Admin table dropped successfully");
                }
            }
            
            // Create the table with the correct schema
            String createTableSQL = "CREATE TABLE IF NOT EXISTS admin (" +
                    "id SERIAL PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "salt VARCHAR(255) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "last_login TIMESTAMP, " +
                    "is_active BOOLEAN DEFAULT TRUE" +
                    ")";
            
            System.out.println("Creating admin table...");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                System.out.println("Admin table created successfully with correct schema");
            }
            
            // Verify the table exists and has the correct columns
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "admin", null)) {
                if (rs.next()) {
                    System.out.println("Verified admin table exists");
                    try (ResultSet columns = conn.getMetaData().getColumns(null, null, "admin", "salt")) {
                        if (columns.next()) {
                            System.out.println("Verified 'salt' column exists in admin table");
                        } else {
                            System.err.println("Warning: 'salt' column is missing after table creation");
                        }
                    }
                } else {
                    System.err.println("Warning: Could not verify admin table existence after creation");
                }
            }
        }
    }

    public interface ResultSetHandler<T> {
        T handle(ResultSet resultSet) throws SQLException;
    }
}
