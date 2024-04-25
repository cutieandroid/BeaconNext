package com.example.beaconnext.models;

public class AttendanceReport {
    String department;
    Integer year;
    String division;

    public AttendanceReport(String department, Integer year, String division) {
        this.department = department;
        this.year = year;
        this.division = division;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
