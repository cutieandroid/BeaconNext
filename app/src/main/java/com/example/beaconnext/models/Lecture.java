package com.example.beaconnext.models;

import com.google.gson.annotations.SerializedName;

public class Lecture {

    String subjectName;
    String department;
    String StartTime;
    String EndTime;
    Integer year;
    String division;
    String lecturer;
    @SerializedName("_id")
    String lectureid;

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    @SerializedName("class")
    Integer classRoom;
    Integer minimumTime;

    public String getLectureid() {
        return lectureid;
    }

    public void setLectureid(String lectureid) {
        this.lectureid = lectureid;
    }

    public Lecture() {
    }

    public Lecture(String subjectName, String department, String startTime, String endTime, Integer year, String division, String lecturer, String lectureid, Integer classRoom, Integer minimumTime) {
        this.subjectName = subjectName;
        this.department = department;
        StartTime = startTime;
        EndTime = endTime;
        this.year = year;
        this.division = division;
        this.lecturer = lecturer;
        this.lectureid = lectureid;
        this.classRoom = classRoom;
        this.minimumTime = minimumTime;
    }

    public Lecture(String subjectName, String department, String startTime, String endTime, Integer year, String division, Integer classRoom, Integer minimumTime) {
        this.subjectName = subjectName;
        this.department = department;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.year = year;
        this.division = division;
        this.classRoom = classRoom;
        this.minimumTime = minimumTime;
    }

    public Lecture(String subjectName, String department, String startTime, String endTime, Integer year, String division, Integer classRoom, Integer minimumTime, String lecturer) {
        this.subjectName = subjectName;
        this.department = department;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.year = year;
        this.division = division;
        this.classRoom = classRoom;
        this.minimumTime = minimumTime;
        this.lecturer=lecturer;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
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

    public Integer getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(Integer classRoom) {
        this.classRoom = classRoom;
    }

    public Integer getMinimumTime() {
        return minimumTime;
    }

    public void setMinimumTime(Integer minimumTime) {
        this.minimumTime = minimumTime;
    }


}
