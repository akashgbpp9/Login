package com.example.login;

public class User {
    public String id;
    public String username;
    public String email;
    public String dob;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public User(String id, String username, String email, String dob) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.dob = dob;
    }

    public User() {
    }
}
