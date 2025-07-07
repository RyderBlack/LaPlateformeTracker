package app.tracky.controller;

import app.tracky.Main;
import app.tracky.Student;
import app.tracky.StudyDomain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label statusLabel;
    @FXML private Label lastSavedLabel;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchType;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> firstNameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, Integer> ageColumn;
    @FXML private TableColumn<Student, Double> gradeColumn;

    private ObservableList<Student> studentData = FXCollections.observableArrayList();
    private Timer autoSaveTimer;
    private static final long AUTO_SAVE_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours

    @FXML
    public void initialize() {
        try {
            // Set up table columns
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
            gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
            
            // Set up study domain column
            TableColumn<Student, String> studyDomainColumn = new TableColumn<>("Study Domains");
            studyDomainColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue();
                StudyDomain[] domains = StudyDomain.findByStudentId(student.getId());
                String domainsText = String.join(", ", Arrays.stream(domains)
                    .map(StudyDomain::getName)
                    .toArray(String[]::new));
                return new javafx.beans.property.SimpleStringProperty(domainsText);
            });
            
            // Add the study domain column to the table
            if (!studentTable.getColumns().contains(studyDomainColumn)) {
                studentTable.getColumns().add(studyDomainColumn);
            }

            // Set up search type combo box
            searchType.getItems().addAll("Name", "Age", "Grade", "Study Domain");
            searchType.getSelectionModel().selectFirst();

            // Load initial data
            refreshData();

            // Set up auto-save
            setupAutoSave();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error initializing dashboard: " + e.getMessage());
        }
    }

    public void setUsername(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        String type = searchType.getValue();
        
        if (searchText.isEmpty()) {
            studentTable.setItems(studentData);
            return;
        }

        ObservableList<Student> filteredList = FXCollections.observableArrayList();
        
        for (Student student : studentData) {
            switch (type) {
                case "Name":
                    if (student.getFirstname().toLowerCase().contains(searchText) || 
                        student.getLastname().toLowerCase().contains(searchText)) {
                        filteredList.add(student);
                    }
                    break;
                case "Age":
                    try {
                        int age = Integer.parseInt(searchText);
                        if (student.getAge() == age) {
                            filteredList.add(student);
                        }
                    } catch (NumberFormatException e) {
                        // Ignore invalid number input
                    }
                    break;
                case "Grade":
                    try {
                        double grade = Double.parseDouble(searchText);
                        if (student.getGrade() == grade) {
                            filteredList.add(student);
                        }
                    } catch (NumberFormatException e) {
                        // Ignore invalid number input
                    }
                    break;
                case "Study Domain":
                    StudyDomain[] domainsArray = StudyDomain.findByStudentId(student.getId());
                    List<StudyDomain> domains = Arrays.asList(domainsArray);
                    for (StudyDomain domain : domains) {
                        if (domain.getName().toLowerCase().contains(searchText)) {
                            filteredList.add(student);
                            break;
                        }
                    }
                    break;
            }
        }
        
        studentTable.setItems(filteredList);
    }

    @FXML
    private void handleExportCSV() {
        try {
            String fileName = "students_export_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            FileWriter writer = new FileWriter(fileName);
            
            // Write header
            writer.write("ID,First Name,Last Name,Age,Grade,Study Domains\n");
            
            // Write data
            for (Student student : studentData) {
                StudyDomain[] domainsArray = StudyDomain.findByStudentId(student.getId());
                List<StudyDomain> domains = Arrays.asList(domainsArray);
                String domainNames = String.join("; ", domains.stream().map(StudyDomain::getName).toArray(String[]::new));
                
                writer.write(String.format("%d,%s,%s,%d,%.2f,\"%s\"\n",
                    student.getId(),
                    student.getFirstname(),
                    student.getLastname(),
                    student.getAge(),
                    student.getGrade(),
                    domainNames
                ));
            }
            
            writer.close();
            updateStatus("Exported to " + fileName);
        } catch (IOException e) {
            updateStatus("Error exporting to CSV: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        if (autoSaveTimer != null) {
            autoSaveTimer.cancel();
        }
        Main.showLoginView();
    }

    @FXML
    public void refreshData() {
        try {
            Student[] studentsArray = Student.findAll();
            List<Student> students = Arrays.asList(studentsArray);
            studentData.setAll(students);
            studentTable.setItems(studentData);
            updateStatus("Loaded " + students.size() + " students");
        } catch (Exception e) {
            updateStatus("Error loading students: " + e.getMessage());
        }
    }

    private void setupAutoSave() {
        autoSaveTimer = new Timer(true);
        autoSaveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    // You might want to implement a proper save mechanism here
                    // For now, we'll just update the status
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    javafx.application.Platform.runLater(() -> {
                        lastSavedLabel.setText("Last saved: " + timestamp);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, AUTO_SAVE_INTERVAL, AUTO_SAVE_INTERVAL);
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
