package app.tracky.controller;

import app.tracky.Admin;
import app.tracky.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private void handleLogin() {
        String usernameOrEmail = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Basic validation
        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            showError("Please enter both username/email and password");
            return;
        }
        
        // Try to authenticate
        Admin admin = Admin.authenticate(usernameOrEmail, password);
        
        if (admin != null) {
            // Login successful
            clearForm();
            Main.showDashboard(admin);
        } else {
            // Login failed
            showError("Invalid username/email or password");
        }
    }
    
    @FXML
    private void switchToRegister() {
        clearForm();
        Main.showRegisterView();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        errorLabel.setVisible(false);
    }
}
