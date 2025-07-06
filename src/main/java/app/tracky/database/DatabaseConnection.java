package app.tracky.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "/application.properties";
    private static BlockingQueue<Connection> connectionPool;
    private static int poolSize;
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }

            Properties prop = new Properties();
            prop.load(input);

            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
            poolSize = Integer.parseInt(prop.getProperty("db.pool.size", "10"));
            int maxPoolSize = Integer.parseInt(prop.getProperty("db.pool.max", "20"));
            int minIdle = Integer.parseInt(prop.getProperty("db.pool.minIdle", "5"));
            int maxIdle = Integer.parseInt(prop.getProperty("db.pool.maxIdle", "10"));
            long maxLifetime = Long.parseLong(prop.getProperty("db.pool.maxLifetime", "1800000"));
            int connectionTimeout = Integer.parseInt(prop.getProperty("db.pool.connectionTimeout", "30000"));
            int idleTimeout = Integer.parseInt(prop.getProperty("db.pool.idleTimeout", "600000"));

            // Initialize the connection pool
            connectionPool = new ArrayBlockingQueue<>(maxPoolSize);
            
            // Create initial connections
            for (int i = 0; i < Math.min(poolSize, maxPoolSize); i++) {
                connectionPool.add(createNewConnection());
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to initialize database connection pool", e);
        }
    }

    private static Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = connectionPool.poll();
        if (connection == null || connection.isClosed() || !connection.isValid(5)) {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            return createNewConnection();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    if (!connectionPool.offer(connection)) {
                        connection.close();
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void closeAllConnections() {
        for (Connection connection : connectionPool) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        connectionPool.clear();
    }

    public static void executeQuery(String query, QueryExecutor executor) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            if (executor != null) {
                executor.setParameters(statement);
                if (query.trim().toUpperCase().startsWith("SELECT")) {
                    try (ResultSet resultSet = statement.executeQuery()) {
                        executor.processResultSet(resultSet);
                    }
                } else {
                    int affectedRows = statement.executeUpdate();
                    executor.processUpdateResult(affectedRows);
                }
            } else {
                statement.execute();
            }
        }
    }

    public static <T> T executeQuery(String query, ResultSetHandler<T> handler) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            if (handler != null) {
                handler.setParameters(statement);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return handler.handle(resultSet);
                }
            } else {
                statement.execute();
                return null;
            }
        }
    }

    public static interface QueryExecutor {
        void setParameters(PreparedStatement statement) throws SQLException;
        default void processResultSet(ResultSet resultSet) throws SQLException {}
        default void processUpdateResult(int affectedRows) throws SQLException {}
    }

    public static interface ResultSetHandler<T> {
        void setParameters(PreparedStatement statement) throws SQLException;
        T handle(ResultSet resultSet) throws SQLException;
    }
}
