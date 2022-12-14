package io.sevcik.csdemo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private boolean isAdmin;

    public User() {}

    public User(String username, String password, boolean isAdmin) {
        this.setUsername(username);
        this.setPassword(password);
        this.setAdmin(isAdmin);
    }


    public User(Long id, String username, String password, boolean isAdmin) {
        this.setId(id);
        this.setUsername(username);
        this.setPassword(password);
        this.setAdmin(isAdmin);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + "'}";
    }
}
