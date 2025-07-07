package app.tracky.util;

import app.tracky.Student;
import app.tracky.StudyDomain;

import java.util.Arrays;
import java.util.List;

public class SampleDataInitializer {
    // Common study domains
    private static final List<String> DOMAINS = Arrays.asList(
        "Computer Science", "Mathematics", "Physics", "Biology", "Chemistry",
        "Engineering", "Business", "Economics", "Psychology", "Sociology"
    );

    public static void initializeSampleData() {
        // Check if we already have data
        Student[] existingStudents = Student.findAll();
        if (existingStudents != null && existingStudents.length > 0) {
            return; // Don't add sample data if we already have students
        }

        // Create sample students with more variety
        Student[] sampleStudents = {
            new Student("Alexandre", "Dupont", 19, 15.5),
            new Student("Sophie", "Martin", 20, 16.2),
            new Student("Thomas", "Bernard", 21, 14.8),
            new Student("Camille", "Petit", 22, 17.5),
            new Student("Hugo", "Durand", 20, 15.0),
            new Student("Emma", "Leroy", 21, 16.8),
            new Student("Lucas", "Moreau", 22, 14.2),
            new Student("Chloé", "Laurent", 20, 16.0),
            new Student("Nathan", "Simon", 23, 15.7),
            new Student("Léa", "Michel", 21, 17.0),
            new Student("Enzo", "Lefebvre", 20, 15.9),
            new Student("Manon", "Roux", 22, 16.5),
            new Student("Maxime", "Fournier", 21, 14.7),
            new Student("Clara", "Girard", 20, 16.3),
            new Student("Théo", "Bonnet", 22, 15.8)
        };

        // Save sample students to the database and assign study domains
        for (int i = 0; i < sampleStudents.length; i++) {
            Student student = sampleStudents[i];
            student.save();
            
            // Assign 1-3 random study domains to each student
            int numDomains = 1 + (i % 3); // Vary between 1-3 domains per student
            for (int j = 0; j < numDomains; j++) {
                String domainName = DOMAINS.get((i + j) % DOMAINS.size());
                double grade = 10.0 + (Math.random() * 10); // Random grade between 10.0 and 20.0
                StudyDomain domain = new StudyDomain(domainName, grade, student.getId());
                domain.save();
            }
        }
    }
}
