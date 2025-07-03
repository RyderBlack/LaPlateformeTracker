package app.tracky;

import java.sql.*;

public class StudyDomain {
    private int id;
    private String name;
    private double grade;
    private int studentId;

    public StudyDomain() {}

    public StudyDomain(String name, double grade, int studentId) {
        this.name = name;
        this.grade = grade;
        this.studentId = studentId;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    // Database operations
    public boolean save() {
        if (id == 0) {
            return insert();
        } else {
            return update();
        }
    }

    private boolean insert() {
        String sql = "INSERT INTO study_domain (name, grade, student_id) VALUES (?, ?, ?) RETURNING id";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, name);
            pstmt.setDouble(2, grade);
            pstmt.setInt(3, studentId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean update() {
        String sql = "UPDATE study_domain SET name = ?, grade = ?, student_id = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setDouble(2, grade);
            pstmt.setInt(3, studentId);
            pstmt.setInt(4, id);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static StudyDomain findById(int id) {
        String sql = "SELECT * FROM study_domain WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return fromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StudyDomain[] findByStudentId(int studentId) {
        String sql = "SELECT * FROM study_domain WHERE student_id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, 
                 ResultSet.TYPE_SCROLL_INSENSITIVE, 
                 ResultSet.CONCUR_READ_ONLY)) {
            
            pstmt.setInt(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                // First, count the rows to create the array
                rs.last();
                int size = rs.getRow();
                rs.beforeFirst();
                
                StudyDomain[] domains = new StudyDomain[size];
                int index = 0;
                
                while (rs.next()) {
                    domains[index++] = fromResultSet(rs);
                }
                
                return domains;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return new StudyDomain[0];
        }
    }

    public boolean delete() {
        String sql = "DELETE FROM study_domain WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static StudyDomain fromResultSet(ResultSet rs) throws SQLException {
        StudyDomain domain = new StudyDomain();
        domain.setId(rs.getInt("id"));
        domain.setName(rs.getString("name"));
        domain.setGrade(rs.getDouble("grade"));
        domain.setStudentId(rs.getInt("student_id"));
        return domain;
    }

    @Override
    public String toString() {
        return String.format("StudyDomain{id=%d, name='%s', grade=%.2f, studentId=%d}",
                id, name, grade, studentId);
    }
}
