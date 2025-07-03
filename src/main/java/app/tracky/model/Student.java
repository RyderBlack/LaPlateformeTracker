package app.tracky.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends BaseEntity {
    private String firstname;
    private String lastname;
    private int age;
    private double grade;
    private List<StudyDomain> studyDomains;

    public Student() {
        this.studyDomains = new ArrayList<>();
    }

    public Student(String firstname, String lastname, int age, double grade) {
        this();
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.grade = grade;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public List<StudyDomain> getStudyDomains() {
        return studyDomains;
    }

    public void setStudyDomains(List<StudyDomain> studyDomains) {
        this.studyDomains = studyDomains;
    }

    public void addStudyDomain(StudyDomain domain) {
        if (domain != null) {
            studyDomains.add(domain);
            domain.setStudent(this);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", grade=" + grade +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
