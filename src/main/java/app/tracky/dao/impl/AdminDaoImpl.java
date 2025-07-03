package app.tracky.dao.impl;

import app.tracky.dao.AdminDao;
import app.tracky.database.DatabaseConnection;
import app.tracky.model.Admin;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminDaoImpl implements AdminDao {
    private static final String FIND_BY_ID = "SELECT * FROM admin WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM admin";
    private static final String FIND_BY_USERNAME = "SELECT * FROM admin WHERE username = ?";
    private static final String FIND_BY_EMAIL = "SELECT * FROM admin WHERE email = ?";
    private static final String INSERT = "INSERT INTO admin (username, email, password) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE admin SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM admin WHERE id = ?";

    @Override
    public Admin findById(int id) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_ID, new DatabaseConnection.ResultSetHandler<Admin>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setInt(1, id);
                }

                @Override
                public Admin handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        return mapRowToAdmin(rs);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding admin by id", e);
        }
    }

    @Override
    public List<Admin> findAll() {
        try {
            return DatabaseConnection.executeQuery(FIND_ALL, new DatabaseConnection.ResultSetHandler<List<Admin>>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    // No parameters for this query
                }

                @Override
                public List<Admin> handle(ResultSet rs) throws SQLException {
                    List<Admin> admins = new ArrayList<>();
                    while (rs.next()) {
                        admins.add(mapRowToAdmin(rs));
                    }
                    return admins;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all admins", e);
        }
    }

    @Override
    public Admin create(Admin admin) {
        try {
            return DatabaseConnection.executeQuery(INSERT, new DatabaseConnection.ResultSetHandler<Admin>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, admin.getUsername());
                    statement.setString(2, admin.getEmail());
                    statement.setString(3, admin.getPassword());
                }

                @Override
                public Admin handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        admin.setId(rs.getInt(1));
                        return admin;
                    }
                    throw new SQLException("Creating admin failed, no ID obtained.");
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error creating admin", e);
        }
    }

    @Override
    public Admin update(Admin admin) {
        try {
            DatabaseConnection.executeQuery(UPDATE, new DatabaseConnection.QueryExecutor() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, admin.getUsername());
                    statement.setString(2, admin.getEmail());
                    statement.setString(3, admin.getPassword());
                    statement.setInt(4, admin.getId());
                }

                @Override
                public void processUpdateResult(int affectedRows) throws SQLException {
                    if (affectedRows == 0) {
                        throw new SQLException("Updating admin failed, no rows affected.");
                    }
                }
            });
            return admin;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating admin", e);
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
            throw new RuntimeException("Error deleting admin", e);
        }
    }

    @Override
    public Admin findByUsername(String username) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_USERNAME, new DatabaseConnection.ResultSetHandler<Admin>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, username);
                }

                @Override
                public Admin handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        return mapRowToAdmin(rs);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding admin by username", e);
        }
    }

    @Override
    public Admin findByEmail(String email) {
        try {
            return DatabaseConnection.executeQuery(FIND_BY_EMAIL, new DatabaseConnection.ResultSetHandler<Admin>() {
                @Override
                public void setParameters(PreparedStatement statement) throws SQLException {
                    statement.setString(1, email);
                }

                @Override
                public Admin handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        return mapRowToAdmin(rs);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException("Error finding admin by email", e);
        }
    }

    private Admin mapRowToAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getInt("id"));
        admin.setUsername(rs.getString("username"));
        admin.setEmail(rs.getString("email"));
        admin.setPassword(rs.getString("password"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (createdAt != null) {
            admin.setCreatedAt(createdAt.toLocalDateTime());
        }
        if (updatedAt != null) {
            admin.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return admin;
    }
}
