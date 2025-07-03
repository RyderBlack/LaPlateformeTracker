package app.tracky.dao;

import app.tracky.model.StudyDomain;

import java.util.List;

public interface StudyDomainDao extends Dao<StudyDomain> {
    List<StudyDomain> findByStudentId(int studentId);
    List<StudyDomain> findByName(String name);
    List<StudyDomain> findByGradeRange(double minGrade, double maxGrade);
    List<StudyDomain> findByStudentAndGradeRange(int studentId, double minGrade, double maxGrade);
}
