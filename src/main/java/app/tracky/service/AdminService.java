package app.tracky.service;

import app.tracky.model.Admin;

public interface AdminService extends Service<Admin> {
    Admin authenticate(String username, String password);
    Admin findByUsername(String username);
    Admin findByEmail(String email);
}
