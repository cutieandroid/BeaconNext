package com.example.beaconnext.singleton;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.beaconnext.Wrapper.SharedPrefWrapper;
import com.example.beaconnext.models.Student;
import com.example.beaconnext.models.Teacher;
import com.google.gson.Gson;


public class LocalStorage {
    Gson gson = new Gson();
    String tempuser ;
    String token;

    public LocalStorage(Context context) {
        SharedPreferences  sharedPreferences= context.getSharedPreferences("BeaconNext", 0);
        String tempspobject = sharedPreferences.getString("currentuser", null);
        token=sharedPreferences.getString("token",null);
        SharedPrefWrapper sharedPrefWrapper = gson.fromJson(tempspobject, SharedPrefWrapper.class);
        Object tempobject = sharedPrefWrapper.getObject();
        tempuser=gson.toJson(tempobject);
    }

    public Student getCurrentStudent() {

        return gson.fromJson(tempuser, Student.class);

    }

    public Teacher getCurrentTeacher() {
        return gson.fromJson(tempuser, Teacher.class);
    }

    public String getToken(){
        return token;
    }


}
