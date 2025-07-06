package app.tracky.controller;

import app.tracky.Main;
import app.tracky.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    
    private final AuthService authService;
    
    public RegisterController() throws SQLException {
        this.authService = new AuthService();
    }
    
    @FXML
    public void initialize() {
        // Enable register button only when all fields are valid
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
    }
    
    private void validateForm() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        boolean isValid = !username.isEmpty() && 
                         !email.isEmpty() && 
                         !password.isEmpty() && 
                         password.length() >= 6 &&
                         password.equals(confirmPassword);
        
        registerButton.setDisable(!isValid);
    }
    
    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        // Disable form during registration attempt
        setFormDisabled(true);
        
        try {
            System.out.println("Attempting to register user: " + username);
            if (authService.register(username, email, password)) {
                // Registration successful, go to login
                System.out.println("Registration successful for user: " + username);
                navigateToLogin("Registration successful! Please login.");
            } else {
                String errorMsg = "Registration failed. Please try again.";
                System.err.println(errorMsg);
                showError(errorMsg);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error during registration: " + e.getMessage());
            showError(e.getMessage());
        } catch (Exception e) {
            String errorMsg = "An error occurred during registration: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            showError(errorMsg);
        } finally {
            setFormDisabled(false);
        }
    }
    
    @FXML
    private void handleBackToLogin() {
        navigateToLogin(null);
    }
    
    private void navigateToLogin(String message) {
        try {
            // Load the login view
            Parent root = Main.loadFXML("/app/tracky/view/login.fxml");
            Stage stage = (Stage) backButton.getScene().getWindow();
            
            // If there's a success message, show it in the login screen
            if (message != null) {
                LoginController loginController = (LoginController) root.getUserData();
                if (loginController != null) {
                    loginController.showSuccess(message);
                }
            }
            
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load login screen: " + e.getMessage());
        }
    }
    
    private void setFormDisabled(boolean disabled) {
        usernameField.setDisable(disabled);
        emailField.setDisable(disabled);
        passwordField.setDisable(disabled);
        confirmPasswordField.setDisable(disabled);
        registerButton.setDisable(disabled);
        backButton.setDisable(disabled);
        
        if (disabled) {
            registerButton.setText("Registering...");
        } else {
            registerButton.setText("Register");
            validateForm();
        }
    }
    
    private void showError(String message) {
        if (message == null || message.trim().isEmpty()) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            return;
        }
        
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
