package com.example.beaconnext.models;

public class MarkPresentRequest {

    String moodleId, lectureId;

    public MarkPresentRequest(String moodleId, String lectureId) {
        this.moodleId = moodleId;
        this.lectureId = lectureId;
    }

    public MarkPresentRequest() {
    }

    public String getMoodleId() {
        return moodleId;
    }

    public void setMoodleId(String moodleId) {
        this.moodleId = moodleId;
    }

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }
}
