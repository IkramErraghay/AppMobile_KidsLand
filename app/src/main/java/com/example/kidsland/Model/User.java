package com.example.kidsland.Model;

import java.util.List;
import java.util.Map;

public class User {
    private String email;
    private String fullName;
    private String password;


    private Map<String, ChildAccount> childAccounts;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String fullName, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }
    // Getters et setters...
    public Map<String, ChildAccount> getChildAccounts() {
        return childAccounts;
    }

    public void setChildAccounts(Map<String, ChildAccount> childAccounts) {
        this.childAccounts = childAccounts;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}