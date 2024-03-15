package com.example.beaconnext.models;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.QueryMap;

public class Notifications {
   String title,description,department,year,division,EndTime;

    public Notifications(String title, String description, String department, String year, String division, String endTime) {
        this.title = title;
        this.description = description;
        this.department = department;
        this.year = year;
        this.division = division;
        this.EndTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        this.EndTime = endTime;
    }
}
