package app.tracky.controller;

import app.tracky.Admin;
import app.tracky.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }
        
        if (password.length() < 8) {
            showError("Password must be at least 8 characters long");
            return;
        }
        
        // Check if username or email already exists
        if (Admin.usernameExists(username)) {
            showError("Username already taken");
            return;
        }
        
        if (Admin.emailExists(email)) {
            showError("Email already registered");
            return;
        }
        
        // Create new admin
        Admin newAdmin = new Admin(username, email, password);
        boolean success = newAdmin.save();
        
        if (success) {
            // Registration successful, switch to login
            clearForm();
            Main.showLoginView();
        } else {
            showError("Failed to create account. Please try again.");
        }
    }
    
    @FXML
    private void switchToLogin() {
        clearForm();
        Main.showLoginView();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void clearForm() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        errorLabel.setVisible(false);
    }
}
