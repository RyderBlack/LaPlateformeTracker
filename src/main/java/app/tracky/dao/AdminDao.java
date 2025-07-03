package app.tracky.dao;

import app.tracky.model.Admin;

public interface AdminDao extends Dao<Admin> {
    Admin findByUsername(String username);
    Admin findByEmail(String email);
}
