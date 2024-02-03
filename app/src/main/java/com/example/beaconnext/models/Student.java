package com.example.beaconnext.models;

public class Student {
    String name,email,division,role,gender,department;
    Integer moodleId,year;

    public Student() {
    }

    public Student(String name, String email, String division, String role, String gender,String department,Integer moodleId, Integer year) {
        this.name = name;
        this.email = email;
        this.division = division;
        this.role = role;
        this.gender=gender;
        this.department=department;
        this.moodleId = moodleId;
        this.year = year;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getMoodleId() {
        return moodleId;
    }

    public void setMoodleId(Integer moodleId) {
        this.moodleId = moodleId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
