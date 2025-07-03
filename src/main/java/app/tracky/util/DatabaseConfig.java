package app.tracky.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "/application.properties";

    static {
        try (InputStream input = DatabaseConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }
}
