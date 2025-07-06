package app.tracky.controller;

import app.tracky.Main;
import app.tracky.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    
    private final AuthService authService;
    
    public LoginController() throws SQLException {
        this.authService = new AuthService();
    }
    
    @FXML
    public void initialize() {
        // Enable login button only when both fields have text
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> updateLoginButton());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> updateLoginButton());
    }
    
    private void updateLoginButton() {
        boolean isValid = !usernameField.getText().trim().isEmpty() && 
                         !passwordField.getText().trim().isEmpty();
        loginButton.setDisable(!isValid);
    }
    
    @FXML
    private void handleLogin() {
        String identifier = usernameField.getText().trim();
        String password = passwordField.getText();
        
        setFormDisabled(true);
        
        try {
            if (authService.login(identifier, password)) {
                navigateToHome(identifier);
            } else {
                showError("Invalid username/email or password");
                passwordField.clear();
                passwordField.requestFocus();
            }
        } catch (Exception e) {
            showError("Login failed: " + e.getMessage());
        } finally {
            setFormDisabled(false);
        }
    }
    
    private void setFormDisabled(boolean disabled) {
        usernameField.setDisable(disabled);
        passwordField.setDisable(disabled);
        loginButton.setDisable(disabled);
        registerButton.setDisable(disabled);
        
        if (disabled) {
            loginButton.setText("Signing in...");
        } else {
            loginButton.setText("Login");
            updateLoginButton();
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            // Load the register view
            Parent root = Main.loadFXML("/app/tracky/view/register.fxml");
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load registration form: " + e.getMessage());
        }
    }
    
    private void navigateToHome(String username) {
        try {
            // Load the home view
            Parent root = Main.loadFXML("/app/tracky/view/home.fxml");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            
            // Set the username in the home controller
            HomeController homeController = (HomeController) root.getUserData();
            if (homeController != null) {
                homeController.setUsername(username);
            }
            
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load home screen: " + e.getMessage());
        }
    }
    
    public void showSuccess(String message) {
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void showError(String message) {
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        
        // Add fade in effect
        FadeTransition ft = new FadeTransition(Duration.millis(300), errorLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        
        // Auto-hide error after 5 seconds
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> {
                        FadeTransition ft = new FadeTransition(Duration.millis(500), errorLabel);
                        ft.setFromValue(1.0);
                        ft.setToValue(0.0);
                        ft.setOnFinished(event -> errorLabel.setVisible(false));
                        ft.play();
                    });
                }
            },
            5000
        );
    }
    
    public void cleanup() {
        try {
            if (authService != null) {
                authService.close();
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
