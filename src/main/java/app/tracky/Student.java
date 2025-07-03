package app.tracky;

import java.sql.*;

public class Student {
    private int id;
    private String firstname;
    private String lastname;
    private int age;
    private double grade;

    public Student() {}

    public Student(String firstname, String lastname, int age, double grade) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.grade = grade;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }

    // Database operations
    public boolean save() {
        if (id == 0) {
            return insert();
        } else {
            return update();
        }
    }

    private boolean insert() {
        String sql = "INSERT INTO student (firstname, lastname, age, grade) VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setInt(3, age);
            pstmt.setDouble(4, grade);
            
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
        String sql = "UPDATE student SET firstname = ?, lastname = ?, age = ?, grade = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setInt(3, age);
            pstmt.setDouble(4, grade);
            pstmt.setInt(5, id);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Student findById(int id) {
        String sql = "SELECT * FROM student WHERE id = ?";
        
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

    public static Student[] findAll() {
        String sql = "SELECT * FROM student ORDER BY lastname, firstname";
        
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // First, count the rows to create the array
            rs.last();
            int size = rs.getRow();
            rs.beforeFirst();
            
            Student[] students = new Student[size];
            int index = 0;
            
            while (rs.next()) {
                students[index++] = fromResultSet(rs);
            }
            
            return students;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return new Student[0];
        }
    }

    public boolean delete() {
        String sql = "DELETE FROM student WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Student fromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setFirstname(rs.getString("firstname"));
        student.setLastname(rs.getString("lastname"));
        student.setAge(rs.getInt("age"));
        student.setGrade(rs.getDouble("grade"));
        return student;
    }

    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s %s', age=%d, grade=%.2f}",
                id, firstname, lastname, age, grade);
    }
}
