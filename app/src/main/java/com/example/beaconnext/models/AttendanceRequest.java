package com.example.beaconnext.models;

public class AttendanceRequest {
    String uuid;

    public AttendanceRequest() {
    }

    public AttendanceRequest(String uuid) {
        this.uuid = uuid;
    }

    public String getLectureId() {
        return uuid;
    }

    public void setLectureId(String uuid) {
        this.uuid = uuid;
    }
}
