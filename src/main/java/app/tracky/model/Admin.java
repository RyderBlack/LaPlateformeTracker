package app.tracky.model;

public class Admin extends BaseEntity {
    private String username;
    private String email;
    private String password;

    public Admin() {
    }

    public Admin(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password; // In a real app, this should be hashed
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // In a real app, this should be hashed
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
