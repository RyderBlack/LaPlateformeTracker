package app.tracky.dao;

import app.tracky.model.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {
    List<Student> findByFirstname(String firstname);
    List<Student> findByLastname(String lastname);
    List<Student> findByAgeRange(int minAge, int maxAge);
    List<Student> findByGradeRange(double minGrade, double maxGrade);
}
