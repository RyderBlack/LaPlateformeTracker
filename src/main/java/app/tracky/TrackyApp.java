package app.tracky;

import app.tracky.model.Admin;
import app.tracky.model.Student;
import app.tracky.model.StudyDomain;
import app.tracky.service.AdminService;
import app.tracky.service.ServiceFactory;
import app.tracky.service.StudentService;
import app.tracky.service.StudyDomainService;
import app.tracky.util.DatabaseInitializer;

import java.util.List;

public class TrackyApp {
    public static void main(String[] args) {
        try {
            // Initialize the database
            System.out.println("Initializing database...");
            DatabaseInitializer.initializeDatabase();
            
            // Get service instances
            AdminService adminService = ServiceFactory.getAdminService();
            StudentService studentService = ServiceFactory.getStudentService();
            StudyDomainService studyDomainService = ServiceFactory.getStudyDomainService();
            
            // Test Admin Service
            System.out.println("\n=== Testing Admin Service ===");
            Admin admin = new Admin("admin", "admin@example.com", "admin123");
            admin = adminService.create(admin);
            System.out.println("Created admin: " + admin.getUsername());
            
            Admin foundAdmin = adminService.authenticate("admin", "admin123");
            System.out.println("Authenticated admin: " + (foundAdmin != null ? foundAdmin.getUsername() : "null"));
            
            // Test Student Service
            System.out.println("\n=== Testing Student Service ===");
            Student student = new Student("John", "Doe", 20, 15.5);
            student = studentService.create(student);
            System.out.println("Created student: " + student.getFirstname() + " " + student.getLastname());
            
            // Test StudyDomain Service
            System.out.println("\n=== Testing StudyDomain Service ===");
            StudyDomain domain1 = new StudyDomain("Mathematics", 16.0);
            domain1.setStudent(student);
            domain1 = studyDomainService.create(domain1);
            System.out.println("Created study domain: " + domain1.getName() + " (Grade: " + domain1.getGrade() + ")");
            
            StudyDomain domain2 = new StudyDomain("Physics", 14.5);
            domain2.setStudent(student);
            domain2 = studyDomainService.create(domain2);
            System.out.println("Created study domain: " + domain2.getName() + " (Grade: " + domain2.getGrade() + ")");
            
            // Retrieve student with study domains
            student = studentService.findById(student.getId());
            System.out.println("\nStudent details:");
            System.out.println("Name: " + student.getFirstname() + " " + student.getLastname());
            System.out.println("Age: " + student.getAge());
            System.out.println("Average Grade: " + student.getGrade());
            
            System.out.println("\nStudy Domains:");
            for (StudyDomain domain : student.getStudyDomains()) {
                System.out.println("- " + domain.getName() + ": " + domain.getGrade());
            }
            
            // Test finding students by grade range
            System.out.println("\nFinding students with grade between 15 and 17:");
            List<Student> students = studentService.findByGradeRange(15.0, 17.0);
            for (Student s : students) {
                System.out.println("- " + s.getFirstname() + " " + s.getLastname() + ": " + s.getGrade());
            }
            
            System.out.println("\nApplication completed successfully!");
            
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
