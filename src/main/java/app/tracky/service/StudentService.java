package app.tracky.service;

import app.tracky.model.Student;

import java.util.List;

public interface StudentService extends Service<Student> {
    List<Student> findByFirstname(String firstname);
    List<Student> findByLastname(String lastname);
    List<Student> findByAgeRange(int minAge, int maxAge);
    List<Student> findByGradeRange(double minGrade, double maxGrade);
}
