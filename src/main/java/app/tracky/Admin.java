package app.tracky;

import java.sql.*;

import app.tracky.model.Hashing;

public class Admin {
    private int id;
    private String username;
    private String email;
    private String password;

    public Admin() {}

    public Admin(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password; // In a real app, this should be hashed
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Database operations
    public static Admin authenticate(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (Hashing.checkPassword(password, hashedPassword)) {
                        Admin admin = new Admin();
                        admin.setId(rs.getInt("id"));
                        admin.setUsername(rs.getString("username"));
                        admin.setEmail(rs.getString("email"));

                        admin.setPassword(hashedPassword);
                        return admin;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean save() {
        if (id == 0) {
            return insert();
        } else {
            return update();
        }
    }

    private boolean insert() {
        String sql = "INSERT INTO admin (username, email, password, salt) VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            String hashedPassword = Hashing.hashPassword(password);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, ""); // Empty string as dummy salt since BCrypt includes salt in hash
            
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
        String sql = "UPDATE admin SET username = ?, email = ?, password = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setInt(4, id);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Admin findById(int id) {
        String sql = "SELECT * FROM admin WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setEmail(rs.getString("email"));
                    admin.setPassword(rs.getString("password"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete() {
        String sql = "DELETE FROM admin WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        Admin admin = new Admin("UserTest", "test.user@laplateforme.io", "hashmdp");
        boolean success = admin.save();

        if (success) {
        System.out.println("✅ Admin enregistré avec succès. ID : " + admin.getId());
        } else {
            System.out.println("❌ Échec de l'enregistrement de l'admin.");
        }

        Admin loggedIn = Admin.authenticate("Arnaud", "Laplateforme1212");
        if (loggedIn != null) {
            System.out.println("✅ Connecté en tant que : " + loggedIn.getUsername());
        } else {
            System.out.println("❌ Échec de l'authentification.");
        }
    }
}
