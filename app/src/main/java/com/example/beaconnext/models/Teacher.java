package com.example.beaconnext.models;

public class Teacher {
    String role,name,email,gender,department;

    public Teacher() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Teacher(String role, String name, String email, String gender, String department) {
        this.role = role;
        this.name = name;
        this.email = email;
        this.department=department;
        this.gender=gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
