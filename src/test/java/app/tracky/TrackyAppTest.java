package app.tracky;

import app.tracky.model.Admin;
import app.tracky.model.Student;
import app.tracky.model.StudyDomain;
import app.tracky.service.AdminService;
import app.tracky.service.ServiceFactory;
import app.tracky.service.StudentService;
import app.tracky.service.StudyDomainService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TrackyAppTest {
    private static AdminService adminService;
    private static StudentService studentService;
    private static StudyDomainService studyDomainService;

    @BeforeAll
    static void setUp() {
        // Initialize services
        adminService = ServiceFactory.getAdminService();
        studentService = ServiceFactory.getStudentService();
        studyDomainService = ServiceFactory.getStudyDomainService();
    }

    @Test
    void testAdminCrud() {
        // Create
        Admin admin = new Admin("testadmin", "test@example.com", "testpass");
        admin = adminService.create(admin);
        assertNotNull(admin.getId());
        
        // Read
        Admin foundAdmin = adminService.findById(admin.getId());
        assertNotNull(foundAdmin);
        assertEquals(admin.getUsername(), foundAdmin.getUsername());
        
        // Update
        admin.setEmail("updated@example.com");
        admin = adminService.update(admin);
        assertEquals("updated@example.com", admin.getEmail());
        
        // Delete
        boolean deleted = adminService.delete(admin.getId());
        assertTrue(deleted);
        assertNull(adminService.findById(admin.getId()));
    }

    @Test
    void testStudentCrud() {
        // Create
        Student student = new Student("Test", "Student", 25, 16.0);
        student = studentService.create(student);
        assertNotNull(student.getId());
        
        // Read
        Student foundStudent = studentService.findById(student.getId());
        assertNotNull(foundStudent);
        assertEquals("Test", foundStudent.getFirstname());
        
        // Update
        student.setLastname("Updated");
        student = studentService.update(student);
        assertEquals("Updated", student.getLastname());
        
        // Delete
        boolean deleted = studentService.delete(student.getId());
        assertTrue(deleted);
        assertNull(studentService.findById(student.getId()));
    }

    @Test
    void testStudyDomainCrud() {
        // Create a student first
        Student student = new Student("Domain", "Test", 22, 15.0);
        student = studentService.create(student);
        
        // Create domain
        StudyDomain domain = new StudyDomain("Test Domain", 17.5);
        domain.setStudent(student);
        domain = studyDomainService.create(domain);
        assertNotNull(domain.getId());
        
        // Read
        StudyDomain foundDomain = studyDomainService.findById(domain.getId());
        assertNotNull(foundDomain);
        assertEquals("Test Domain", foundDomain.getName());
        
        // Update
        domain.setName("Updated Domain");
        domain = studyDomainService.update(domain);
        assertEquals("Updated Domain", domain.getName());
        
        // Delete
        boolean deleted = studyDomainService.delete(domain.getId());
        assertTrue(deleted);
        assertNull(studyDomainService.findById(domain.getId()));
        
        // Clean up
        studentService.delete(student.getId());
    }

    @Test
    void testFindByGradeRange() {
        // Create test data
        Student student1 = new Student("Grade", "Range1", 20, 14.0);
        student1 = studentService.create(student1);
        
        Student student2 = new Student("Grade", "Range2", 21, 16.0);
        student2 = studentService.create(student2);
        
        // Test grade range
        var students = studentService.findByGradeRange(15.0, 17.0);
        assertEquals(1, students.size());
        assertEquals(16.0, students.get(0).getGrade());
        
        // Clean up
        studentService.delete(student1.getId());
        studentService.delete(student2.getId());
    }
}
