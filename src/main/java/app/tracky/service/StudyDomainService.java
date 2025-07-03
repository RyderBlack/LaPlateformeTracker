package app.tracky.service;

import app.tracky.model.StudyDomain;

import java.util.List;

public interface StudyDomainService extends Service<StudyDomain> {
    List<StudyDomain> findByStudentId(int studentId);
    List<StudyDomain> findByName(String name);
    List<StudyDomain> findByGradeRange(double minGrade, double maxGrade);
    List<StudyDomain> findByStudentAndGradeRange(int studentId, double minGrade, double maxGrade);
}
