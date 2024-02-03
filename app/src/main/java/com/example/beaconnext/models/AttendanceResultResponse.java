package com.example.beaconnext.models;

import android.content.Intent;

public class AttendanceResultResponse {

    StudentID Id;
    Integer Count;
    boolean Present;

    public AttendanceResultResponse() {
    }

    public AttendanceResultResponse(StudentID id, Integer count, boolean present) {
        Id = id;
        Count = count;
        Present = present;
    }

    public StudentID getId() {
        return Id;
    }

    public void setId(StudentID id) {
        Id = id;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }

    public boolean isPresent() {
        return Present;
    }

    public void setPresent(boolean present) {
        Present = present;
    }
}


