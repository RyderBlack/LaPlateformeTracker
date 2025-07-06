package app.tracky;

import app.tracky.database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {
    private static Main instance;
    
    public Main() {
        instance = this;
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Test database connection
            try (Connection conn = DatabaseConnection.getConnection()) {
                System.out.println("Successfully connected to the database");
            } catch (SQLException e) {
                showError("Database Error", "Cannot connect to the database. Please check your database settings.", e);
                return;
            }

            // Load the login view
            Parent root = loadFXML("/app/tracky/view/login.fxml");
            
            // Set up the stage
            primaryStage.setTitle("LaPlateforme Tracker");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(500);
            
            // Show the stage
            primaryStage.show();
            
        } catch (Exception e) {
            showError("Startup Error", "Failed to start application", e);
        }
    }
    
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(fxml));
        return loader.load();
    }
    
    public static void showError(String title, String message) {
        showError(title, message, null);
    }
    
    public static void showError(String title, String message, Throwable throwable) {
        System.err.println(title + ": " + message);
        if (throwable != null) {
            throwable.printStackTrace();
        }
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(throwable != null ? throwable.getMessage() : "No additional information");
        alert.showAndWait();
    }
    
    public static Main getInstance() {
        return instance;
    }
    
    public static void main(String[] args) {
        // Set up database driver
        try {
            Class.forName("org.postgresql.Driver");
            launch(args);
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
