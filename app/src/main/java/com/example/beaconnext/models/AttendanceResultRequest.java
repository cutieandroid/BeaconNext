package com.example.beaconnext.models;

public class AttendanceResultRequest {

    public AttendanceResultRequest() {
    }

    public AttendanceResultRequest(String lectureId) {
        this.lectureId = lectureId;
    }

    String lectureId;

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    @Override
    public String toString() {
        return "AttendanceResultRequest{" +
                "lectureId='" + lectureId + '\'' +
                '}';
    }
}
