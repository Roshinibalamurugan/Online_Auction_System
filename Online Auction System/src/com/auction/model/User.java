package com.auction.model;

public class User {
    private int userId;
    private String name;
    private String email;
    private String role;
    private String password;  // Add password field

    // Constructor to initialize all fields, including password
    public User(int userId, String name, String email, String role, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;  // Initialize password
    }

    // Getter and Setter methods for each field

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;  // Getter for email
    }

    public void setEmail(String email) {
        this.email = email;  // Setter for email
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;  // Getter for password
    }

    public void setPassword(String password) {
        this.password = password;  // Setter for password
    }
}
