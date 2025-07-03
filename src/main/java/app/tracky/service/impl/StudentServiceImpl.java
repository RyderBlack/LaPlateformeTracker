package app.tracky.service.impl;

import app.tracky.dao.StudentDao;
import app.tracky.dao.StudyDomainDao;
import app.tracky.dao.impl.StudentDaoImpl;
import app.tracky.dao.impl.StudyDomainDaoImpl;
import app.tracky.model.Student;
import app.tracky.model.StudyDomain;
import app.tracky.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao;
    private final StudyDomainDao studyDomainDao;

    public StudentServiceImpl() {
        this.studentDao = new StudentDaoImpl();
        this.studyDomainDao = new StudyDomainDaoImpl();
    }

    @Override
    public Student findById(int id) {
        Student student = studentDao.findById(id);
        if (student != null) {
            // Load study domains for the student
            List<StudyDomain> studyDomains = studyDomainDao.findByStudentId(id);
            student.setStudyDomains(studyDomains);
        }
        return student;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = studentDao.findAll();
        // Load study domains for each student
        for (Student student : students) {
            List<StudyDomain> studyDomains = studyDomainDao.findByStudentId(student.getId());
            student.setStudyDomains(studyDomains);
        }
        return students;
    }

    @Override
    public Student create(Student student) {
        // Validate student data
        if (student.getFirstname() == null || student.getFirstname().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (student.getLastname() == null || student.getLastname().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (student.getAge() <= 0) {
            throw new IllegalArgumentException("Age must be greater than 0");
        }
        if (student.getGrade() < 0 || student.getGrade() > 20) {
            throw new IllegalArgumentException("Grade must be between 0 and 20");
        }

        // Create the student
        Student createdStudent = studentDao.create(student);

        // Save study domains if any
        if (student.getStudyDomains() != null && !student.getStudyDomains().isEmpty()) {
            for (StudyDomain domain : student.getStudyDomains()) {
                domain.setStudent(createdStudent);
                studyDomainDao.create(domain);
            }
        }

        return createdStudent;
    }

    @Override
    public Student update(Student student) {
        // Check if student exists
        Student existingStudent = studentDao.findById(student.getId());
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student not found with id: " + student.getId());
        }

        // Validate student data
        if (student.getFirstname() == null || student.getFirstname().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (student.getLastname() == null || student.getLastname().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (student.getAge() <= 0) {
            throw new IllegalArgumentException("Age must be greater than 0");
        }
        if (student.getGrade() < 0 || student.getGrade() > 20) {
            throw new IllegalArgumentException("Grade must be between 0 and 20");
        }

        // Update the student
        Student updatedStudent = studentDao.update(student);

        // Update study domains if any
        if (student.getStudyDomains() != null) {
            // Delete existing domains not in the updated list
            List<StudyDomain> existingDomains = studyDomainDao.findByStudentId(student.getId());
            for (StudyDomain existing : existingDomains) {
                boolean found = false;
                for (StudyDomain updated : student.getStudyDomains()) {
                    if (updated.getId() != null && updated.getId().equals(existing.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    studyDomainDao.delete(existing.getId());
                }
            }

            // Add or update domains
            for (StudyDomain domain : student.getStudyDomains()) {
                domain.setStudent(updatedStudent);
                if (domain.getId() == null) {
                    studyDomainDao.create(domain);
                } else {
                    studyDomainDao.update(domain);
                }
            }
        }

        return updatedStudent;
    }

    @Override
    public boolean delete(int id) {
        // Check if student exists
        if (studentDao.findById(id) == null) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
        
        // Delete study domains first (due to foreign key constraint)
        List<StudyDomain> domains = studyDomainDao.findByStudentId(id);
        for (StudyDomain domain : domains) {
            studyDomainDao.delete(domain.getId());
        }
        
        // Delete the student
        return studentDao.delete(id);
    }

    @Override
    public List<Student> findByFirstname(String firstname) {
        return studentDao.findByFirstname(firstname);
    }

    @Override
    public List<Student> findByLastname(String lastname) {
        return studentDao.findByLastname(lastname);
    }

    @Override
    public List<Student> findByAgeRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
            throw new IllegalArgumentException("Invalid age range");
        }
        return studentDao.findByAgeRange(minAge, maxAge);
    }

    @Override
    public List<Student> findByGradeRange(double minGrade, double maxGrade) {
        if (minGrade < 0 || maxGrade > 20 || minGrade > maxGrade) {
            throw new IllegalArgumentException("Grade must be between 0 and 20");
        }
        return studentDao.findByGradeRange(minGrade, maxGrade);
    }
}
