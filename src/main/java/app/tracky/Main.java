package app.tracky;

import app.tracky.controller.DashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class Main extends Application {
    private static Stage primaryStage;
    private static Admin currentAdmin;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showLoginView();
        stage.show();
    }

    public static void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/LoginView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add("/styles/dark-theme.css");
            primaryStage.setScene(scene);
            primaryStage.setTitle("Admin Login - Tracky");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showRegisterView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/RegisterView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add("/styles/dark-theme.css");
            primaryStage.setScene(scene);
            primaryStage.setTitle("Admin Registration - Tracky");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDashboard(Admin admin) {
        try {
            currentAdmin = admin;
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/DashboardView.fxml"));
            Parent root = loader.load();
            
            // Set the admin username in the dashboard
            DashboardController controller = loader.getController();
            controller.setUsername(admin.getUsername());
            
            // Initialize sample data
            try {
                app.tracky.util.SampleDataInitializer.initializeSampleData();
            } catch (Exception e) {
                System.err.println("Error initializing sample data: " + e.getMessage());
                e.printStackTrace();
                showError("Error initializing sample data: " + e.getMessage());
            }
            
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add("/styles/dark-theme.css");
            primaryStage.setScene(scene);
            primaryStage.setTitle("Dashboard - Tracky");
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }
    
    public static void logout() {
        currentAdmin = null;
        showLoginView();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
