package app.tracky.service.impl;

import app.tracky.dao.StudentDao;
import app.tracky.dao.StudyDomainDao;
import app.tracky.dao.impl.StudentDaoImpl;
import app.tracky.dao.impl.StudyDomainDaoImpl;
import app.tracky.model.StudyDomain;
import app.tracky.service.StudyDomainService;

import java.util.List;

public class StudyDomainServiceImpl implements StudyDomainService {
    private final StudyDomainDao studyDomainDao;
    private final StudentDao studentDao;

    public StudyDomainServiceImpl() {
        this.studyDomainDao = new StudyDomainDaoImpl();
        this.studentDao = new StudentDaoImpl();
    }

    @Override
    public StudyDomain findById(int id) {
        StudyDomain domain = studyDomainDao.findById(id);
        if (domain != null) {
            // Load the associated student
            domain.setStudent(studentDao.findById(domain.getStudent().getId()));
        }
        return domain;
    }

    @Override
    public List<StudyDomain> findAll() {
        List<StudyDomain> domains = studyDomainDao.findAll();
        // Load associated students
        for (StudyDomain domain : domains) {
            domain.setStudent(studentDao.findById(domain.getStudent().getId()));
        }
        return domains;
    }

    @Override
    public StudyDomain create(StudyDomain domain) {
        validateStudyDomain(domain);
        return studyDomainDao.create(domain);
    }

    @Override
    public StudyDomain update(StudyDomain domain) {
        // Check if domain exists
        StudyDomain existingDomain = studyDomainDao.findById(domain.getId());
        if (existingDomain == null) {
            throw new IllegalArgumentException("Study domain not found with id: " + domain.getId());
        }
        
        validateStudyDomain(domain);
        return studyDomainDao.update(domain);
    }

    @Override
    public boolean delete(int id) {
        // Check if domain exists
        if (studyDomainDao.findById(id) == null) {
            throw new IllegalArgumentException("Study domain not found with id: " + id);
        }
        return studyDomainDao.delete(id);
    }

    @Override
    public List<StudyDomain> findByStudentId(int studentId) {
        // Check if student exists
        if (studentDao.findById(studentId) == null) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }
        
        List<StudyDomain> domains = studyDomainDao.findByStudentId(studentId);
        // Set the student for each domain
        for (StudyDomain domain : domains) {
            domain.setStudent(studentDao.findById(studentId));
        }
        return domains;
    }

    @Override
    public List<StudyDomain> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        
        List<StudyDomain> domains = studyDomainDao.findByName(name);
        // Load associated students
        for (StudyDomain domain : domains) {
            domain.setStudent(studentDao.findById(domain.getStudent().getId()));
        }
        return domains;
    }

    @Override
    public List<StudyDomain> findByGradeRange(double minGrade, double maxGrade) {
        validateGradeRange(minGrade, maxGrade);
        
        List<StudyDomain> domains = studyDomainDao.findByGradeRange(minGrade, maxGrade);
        // Load associated students
        for (StudyDomain domain : domains) {
            domain.setStudent(studentDao.findById(domain.getStudent().getId()));
        }
        return domains;
    }

    @Override
    public List<StudyDomain> findByStudentAndGradeRange(int studentId, double minGrade, double maxGrade) {
        // Check if student exists
        if (studentDao.findById(studentId) == null) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }
        
        validateGradeRange(minGrade, maxGrade);
        
        List<StudyDomain> domains = studyDomainDao.findByStudentAndGradeRange(studentId, minGrade, maxGrade);
        // Set the student for each domain
        for (StudyDomain domain : domains) {
            domain.setStudent(studentDao.findById(studentId));
        }
        return domains;
    }
    
    private void validateStudyDomain(StudyDomain domain) {
        if (domain == null) {
            throw new IllegalArgumentException("Study domain cannot be null");
        }
        if (domain.getName() == null || domain.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Study domain name is required");
        }
        if (domain.getGrade() < 0 || domain.getGrade() > 20) {
            throw new IllegalArgumentException("Grade must be between 0 and 20");
        }
        if (domain.getStudent() == null || domain.getStudent().getId() == null) {
            throw new IllegalArgumentException("Student is required");
        }
        // Verify student exists
        if (studentDao.findById(domain.getStudent().getId()) == null) {
            throw new IllegalArgumentException("Student not found with id: " + domain.getStudent().getId());
        }
    }
    
    private void validateGradeRange(double minGrade, double maxGrade) {
        if (minGrade < 0 || maxGrade > 20 || minGrade > maxGrade) {
            throw new IllegalArgumentException("Invalid grade range. Grades must be between 0 and 20");
        }
    }
}
