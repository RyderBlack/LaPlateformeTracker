package app.tracky.service.impl;

import app.tracky.dao.AdminDao;
import app.tracky.dao.impl.AdminDaoImpl;
import app.tracky.model.Admin;
import app.tracky.service.AdminService;

public class AdminServiceImpl implements AdminService {
    private final AdminDao adminDao;

    public AdminServiceImpl() {
        this.adminDao = new AdminDaoImpl();
    }

    @Override
    public Admin findById(int id) {
        return adminDao.findById(id);
    }

    @Override
    public java.util.List<Admin> findAll() {
        return adminDao.findAll();
    }

    @Override
    public Admin create(Admin admin) {
        // Check if username or email already exists
        if (adminDao.findByUsername(admin.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (adminDao.findByEmail(admin.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        // In a real app, you would hash the password here
        return adminDao.create(admin);
    }

    @Override
    public Admin update(Admin admin) {
        // Check if admin exists
        Admin existingAdmin = adminDao.findById(admin.getId());
        if (existingAdmin == null) {
            throw new IllegalArgumentException("Admin not found with id: " + admin.getId());
        }

        // Check if new username is taken by another admin
        Admin adminWithSameUsername = adminDao.findByUsername(admin.getUsername());
        if (adminWithSameUsername != null && !adminWithSameUsername.getId().equals(admin.getId())) {
            throw new IllegalArgumentException("Username already taken");
        }

        // Check if new email is taken by another admin
        Admin adminWithSameEmail = adminDao.findByEmail(admin.getEmail());
        if (adminWithSameEmail != null && !adminWithSameEmail.getId().equals(admin.getId())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // If password is not being updated, keep the existing one
        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            admin.setPassword(existingAdmin.getPassword());
        }

        return adminDao.update(admin);
    }

    @Override
    public boolean delete(int id) {
        // Check if admin exists
        if (adminDao.findById(id) == null) {
            throw new IllegalArgumentException("Admin not found with id: " + id);
        }
        return adminDao.delete(id);
    }

    @Override
    public Admin authenticate(String username, String password) {
        Admin admin = adminDao.findByUsername(username);
        if (admin != null) {
            // In a real app, you would verify the hashed password here
            if (password.equals(admin.getPassword())) {
                return admin;
            }
        }
        return null;
    }

    @Override
    public Admin findByUsername(String username) {
        return adminDao.findByUsername(username);
    }

    @Override
    public Admin findByEmail(String email) {
        return adminDao.findByEmail(email);
    }
}
