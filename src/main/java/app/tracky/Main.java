package app.tracky;

public class Main {
    public static void main(String[] args) {
        // Test Admin
        System.out.println("=== Testing Admin ===");
        Admin admin = new Admin("testuser", "test@example.com", "testpass");
        boolean saved = admin.save();
        System.out.println("Admin saved: " + saved);
        
        // Test authentication
        Admin authenticated = Admin.authenticate("testuser", "testpass");
        System.out.println("Authenticated: " + (authenticated != null));
        
        // Test Student
        System.out.println("\n=== Testing Student ===");
        Student student = new Student("John", "Doe", 20, 15.5);
        saved = student.save();
        System.out.println("Student saved: " + saved);
        
        // Add study domain
        if (saved) {
            StudyDomain domain = new StudyDomain("Mathematics", 16.0, student.getId());
            saved = domain.save();
            System.out.println("Study domain saved: " + saved);
            
            // Find student with domain
            System.out.println("\n=== Student Details ===");
            Student found = Student.findById(student.getId());
            System.out.println("Found student: " + found);
            
            StudyDomain[] domains = StudyDomain.findByStudentId(student.getId());
            System.out.println("Study domains:");
            for (StudyDomain d : domains) {
                System.out.println("  - " + d);
            }
        }
    }
}
