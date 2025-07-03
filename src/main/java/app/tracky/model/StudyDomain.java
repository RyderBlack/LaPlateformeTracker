package app.tracky.model;

public class StudyDomain extends BaseEntity {
    private String name;
    private double grade;
    private Student student;

    public StudyDomain() {
    }

    public StudyDomain(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "StudyDomain{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", studentId=" + (student != null ? student.getId() : "null") +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
