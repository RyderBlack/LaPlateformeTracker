package app.tracky.dao.impl;

import app.tracky.dao.StudyDomainDao;
import app.tracky.database.DatabaseConnection;
import app.tracky.model.StudyDomain;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudyDomainDaoImpl implements StudyDomainDao {
    private static final String FIND_BY_ID = "SELECT * FROM study_domain WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM study_domain";
    private static final String FIND_BY_STUDENT_ID = "SELECT * FROM study_domain WHERE student_id = ?";
    private static final String FIND_BY_NAME = "SELECT * FROM study_domain WHERE study_domain_name LIKE ?";
    private static final String FIND_BY_GRADE_RANGE = "SELECT * FROM study_domain WHERE study_domain_grade BETWEEN ? AND ?";
    private static final String FIND_BY_STUDENT_AND_GRADE_RANGE = 
            "SELECT * FROM study_domain WHERE student_id = ? AND study_domain_grade BETWEEN ? AND ?";
    private static final String INSERT = "INSERT INTO study_domain (study_domain_name, study_domain_grade, student_id) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE study_domain SET study_domain_name = ?, study_domain_grade = ?, student_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM study_domain WHERE id = ?";

    @Override
    public StudyDomain findById(int id) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_ID, new DatabaseConnection.ResultSetHandler<StudyDomain>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setInt(1, id);
                }

                @Override
                public StudyDomain handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        return mapRowToStudyDomain(rs);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding study domain by id", e);
        }
    }

    @Override
    public List<StudyDomain> findAll() {
        try {
            return DatabaseConnection.executeQuery(FIND_ALL, new DatabaseConnection.ResultSetHandler<List<StudyDomain>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    // No parameters for this query
                }

                @Override
                public List<StudyDomain> handle(ResultSet rs) throws SQLException {
                    List<StudyDomain> studyDomains = new ArrayList<>();
                    while (rs.next()) {
                        studyDomains.add(mapRowToStudyDomain(rs));
                    }
                    return studyDomains;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all study domains", e);
        }
    }

    @Override
    public StudyDomain create(StudyDomain studyDomain) {
        try {
            return DatabaseConnection.executeQuery(INSERT, new DatabaseConnection.ResultSetHandler<StudyDomain>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, studyDomain.getName());
                    statement.setDouble(2, studyDomain.getGrade());
                    statement.setInt(3, studyDomain.getStudent().getId());
                }

                @Override
                public StudyDomain handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        studyDomain.setId(rs.getInt(1));
                        return studyDomain;
                    }
                    throw new SQLException("Creating study domain failed, no ID obtained.");
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error creating study domain", e);
        }
    }

    @Override
    public StudyDomain update(StudyDomain studyDomain) {
        try {
            DatabaseConnection.executeQuery(UPDATE, new DatabaseConnection.QueryExecutor() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, studyDomain.getName());
                    statement.setDouble(2, studyDomain.getGrade());
                    statement.setInt(3, studyDomain.getStudent().getId());
                    statement.setInt(4, studyDomain.getId());
                }

                @Override
                public void processUpdateResult(int affectedRows) throws SQLException {
                    if (affectedRows == 0) {
                        throw new SQLException("Updating study domain failed, no rows affected.");
                    }
                }
            });
            return studyDomain;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating study domain", e);
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
            throw new RuntimeException("Error deleting study domain", e);
        }
    }

    @Override
    public List<StudyDomain> findByStudentId(int studentId) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_STUDENT_ID, new DatabaseConnection.ResultSetHandler<List<StudyDomain>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setInt(1, studentId);
                }

                @Override
                public List<StudyDomain> handle(ResultSet rs) throws SQLException {
                    List<StudyDomain> studyDomains = new ArrayList<>();
                    while (rs.next()) {
                        studyDomains.add(mapRowToStudyDomain(rs));
                    }
                    return studyDomains;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding study domains by student id", e);
        }
    }

    @Override
    public List<StudyDomain> findByName(String name) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_NAME, new DatabaseConnection.ResultSetHandler<List<StudyDomain>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, "%" + name + "%");
                }

                @Override
                public List<StudyDomain> handle(ResultSet rs) throws SQLException {
                    List<StudyDomain> studyDomains = new ArrayList<>();
                    while (rs.next()) {
                        studyDomains.add(mapRowToStudyDomain(rs));
                    }
                    return studyDomains;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding study domains by name", e);
        }
    }

    @Override
    public List<StudyDomain> findByGradeRange(double minGrade, double maxGrade) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_GRADE_RANGE, new DatabaseConnection.ResultSetHandler<List<StudyDomain>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setDouble(1, minGrade);
                    statement.setDouble(2, maxGrade);
                }

                @Override
                public List<StudyDomain> handle(ResultSet rs) throws SQLException {
                    List<StudyDomain> studyDomains = new ArrayList<>();
                    while (rs.next()) {
                        studyDomains.add(mapRowToStudyDomain(rs));
                    }
                    return studyDomains;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding study domains by grade range", e);
        }
    }

    @Override
    public List<StudyDomain> findByStudentAndGradeRange(int studentId, double minGrade, double maxGrade) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_STUDENT_AND_GRADE_RANGE, new DatabaseConnection.ResultSetHandler<List<StudyDomain>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setInt(1, studentId);
                    statement.setDouble(2, minGrade);
                    statement.setDouble(3, maxGrade);
                }

                @Override
                public List<StudyDomain> handle(ResultSet rs) throws SQLException {
                    List<StudyDomain> studyDomains = new ArrayList<>();
                    while (rs.next()) {
                        studyDomains.add(mapRowToStudyDomain(rs));
                    }
                    return studyDomains;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding study domains by student and grade range", e);
        }
    }

    private StudyDomain mapRowToStudyDomain(ResultSet rs) throws SQLException {
        StudyDomain studyDomain = new StudyDomain();
        studyDomain.setId(rs.getInt("id"));
        studyDomain.setName(rs.getString("study_domain_name"));
        studyDomain.setGrade(rs.getDouble("study_domain_grade"));
        
        // Note: The student object will be set by the service layer
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (createdAt != null) {
            studyDomain.setCreatedAt(createdAt.toLocalDateTime());
        }
        if (updatedAt != null) {
            studyDomain.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return studyDomain;
    }
}
