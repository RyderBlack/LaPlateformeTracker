package app.tracky;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

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

    public static void showDashboard() {
        // This would be your main dashboard view
        // For now, we'll just show a simple success message
        System.out.println("Login successful! Dashboard would be shown here.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
