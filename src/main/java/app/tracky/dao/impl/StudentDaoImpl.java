package app.tracky.dao.impl;

import app.tracky.dao.StudentDao;
import app.tracky.database.DatabaseConnection;
import app.tracky.model.Student;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    private static final String FIND_BY_ID = "SELECT * FROM student WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM student";
    private static final String FIND_BY_FIRSTNAME = "SELECT * FROM student WHERE firstname LIKE ?";
    private static final String FIND_BY_LASTNAME = "SELECT * FROM student WHERE lastname LIKE ?";
    private static final String FIND_BY_AGE_RANGE = "SELECT * FROM student WHERE age BETWEEN ? AND ?";
    private static final String FIND_BY_GRADE_RANGE = "SELECT * FROM student WHERE grade BETWEEN ? AND ?";
    private static final String INSERT = "INSERT INTO student (firstname, lastname, age, grade) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE student SET firstname = ?, lastname = ?, age = ?, grade = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM student WHERE id = ?";

    @Override
    public Student findById(int id) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_ID, new DatabaseConnection.ResultSetHandler<Student>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setInt(1, id);
                }

                @Override
                public Student handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        return mapRowToStudent(rs);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding student by id", e);
        }
    }

    @Override
    public List<Student> findAll() {
        try {
            return DatabaseConnection.executeQuery(FIND_ALL, new DatabaseConnection.ResultSetHandler<List<Student>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    // No parameters for this query
                }

                @Override
                public List<Student> handle(ResultSet rs) throws SQLException {
                    List<Student> students = new ArrayList<>();
                    while (rs.next()) {
                        students.add(mapRowToStudent(rs));
                    }
                    return students;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all students", e);
        }
    }

    @Override
    public Student create(Student student) {
        try {
            return DatabaseConnection.executeQuery(INSERT, new DatabaseConnection.ResultSetHandler<Student>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, student.getFirstname());
                    statement.setString(2, student.getLastname());
                    statement.setInt(3, student.getAge());
                    statement.setDouble(4, student.getGrade());
                }

                @Override
                public Student handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        student.setId(rs.getInt(1));
                        return student;
                    }
                    throw new SQLException("Creating student failed, no ID obtained.");
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error creating student", e);
        }
    }

    @Override
    public Student update(Student student) {
        try {
            DatabaseConnection.executeQuery(UPDATE, new DatabaseConnection.QueryExecutor() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, student.getFirstname());
                    statement.setString(2, student.getLastname());
                    statement.setInt(3, student.getAge());
                    statement.setDouble(4, student.getGrade());
                    statement.setInt(5, student.getId());
                }

                @Override
                public void processUpdateResult(int affectedRows) throws SQLException {
                    if (affectedRows == 0) {
                        throw new SQLException("Updating student failed, no rows affected.");
                    }
                }
            });
            return student;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating student", e);
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            return DatabaseConnection.executeQuery(DELETE, new DatabaseConnection.ResultSetHandler<Boolean>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setInt(1, id);
                }

                @Override
                public Boolean handle(ResultSet rs) throws SQLException {
                    return true;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student", e);
        }
    }

    @Override
    public List<Student> findByFirstname(String firstname) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_FIRSTNAME, new DatabaseConnection.ResultSetHandler<List<Student>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, "%" + firstname + "%");
                }

                @Override
                public List<Student> handle(ResultSet rs) throws SQLException {
                    List<Student> students = new ArrayList<>();
                    while (rs.next()) {
                        students.add(mapRowToStudent(rs));
                    }
                    return students;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding students by firstname", e);
        }
    }

    @Override
    public List<Student> findByLastname(String lastname) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_LASTNAME, new DatabaseConnection.ResultSetHandler<List<Student>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, "%" + lastname + "%");
                }

                @Override
                public List<Student> handle(ResultSet rs) throws SQLException {
                    List<Student> students = new ArrayList<>();
                    while (rs.next()) {
                        students.add(mapRowToStudent(rs));
                    }
                    return students;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding students by lastname", e);
        }
    }

    @Override
    public List<Student> findByAgeRange(int minAge, int maxAge) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_AGE_RANGE, new DatabaseConnection.ResultSetHandler<List<Student>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setInt(1, minAge);
                    statement.setInt(2, maxAge);
                }

                @Override
                public List<Student> handle(ResultSet rs) throws SQLException {
                    List<Student> students = new ArrayList<>();
                    while (rs.next()) {
                        students.add(mapRowToStudent(rs));
                    }
                    return students;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding students by age range", e);
        }
    }

    @Override
    public List<Student> findByGradeRange(double minGrade, double maxGrade) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_GRADE_RANGE, new DatabaseConnection.ResultSetHandler<List<Student>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setDouble(1, minGrade);
                    statement.setDouble(2, maxGrade);
                }

                @Override
                public List<Student> handle(ResultSet rs) throws SQLException {
                    List<Student> students = new ArrayList<>();
                    while (rs.next()) {
                        students.add(mapRowToStudent(rs));
                    }
                    return students;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding students by grade range", e);
        }
    }

    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setFirstname(rs.getString("firstname"));
        student.setLastname(rs.getString("lastname"));
        student.setAge(rs.getInt("age"));
        student.setGrade(rs.getDouble("grade"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (createdAt != null) {
            student.setCreatedAt(createdAt.toLocalDateTime());
        }
        if (updatedAt != null) {
            student.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return student;
    }
}
