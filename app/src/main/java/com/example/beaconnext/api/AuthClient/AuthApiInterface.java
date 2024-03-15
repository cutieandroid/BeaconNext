package com.example.beaconnext.api.AuthClient;

import com.example.beaconnext.models.AttendanceRequest;
import com.example.beaconnext.models.AttendanceResponse;
import com.example.beaconnext.models.AttendanceResultRequest;
import com.example.beaconnext.models.AttendanceResultResponse;
import com.example.beaconnext.models.AuthModels.Requests.FirstLoginRequest;
import com.example.beaconnext.models.AuthModels.Requests.LoginRequest;
import com.example.beaconnext.models.AuthModels.Response.FirstLoginResponse;
import com.example.beaconnext.models.AuthModels.Response.LoginResponse;
import com.example.beaconnext.models.Lecture;
import com.example.beaconnext.models.MarkPresentRequest;
import com.example.beaconnext.models.Notifications;
import com.example.beaconnext.models.Student;
import com.example.beaconnext.models.Teacher;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApiInterface {

    @POST("first-student-login")
    Call<FirstLoginResponse> firstLogin(@Body FirstLoginRequest firstLoginRequest);

    @POST("student-login")
    Call<LoginResponse> studentLogin(@Body LoginRequest loginRequest);

    @POST("first-teacher-login")
    Call<FirstLoginResponse> firstLoginTeacher(@Body FirstLoginRequest firstLoginRequest);

    @POST("teacher-login")
    Call<LoginResponse> teacherLogin(@Body LoginRequest loginRequest);

    @GET("current-student")
    Call<Student> getCurrentStudent(@Header("Authorization") String token);

    @GET("current-teacher")
    Call<Teacher> getCurrentTeacher(@Header("Authorization") String token);

    @POST("create-lecture")
    Call<Lecture> createLecture(@Header("Authorization") String token, @Body Lecture lecture);

    @GET("ongoing-student")
    Call<List<Lecture>> ongoingLectureStudent(@Header("Authorization") String token);

    @GET("upcoming-student")
    Call<List<Lecture>> upcomingLectureStudent(@Header("Authorization") String token);

    @GET("ongoing-teacher")
    Call<List<Lecture>> ongoingLectureTeacher(@Header("Authorization") String token);

    @GET("upcoming-teacher")
    Call<List<Lecture>> upcomingLectureTeacher(@Header("Authorization") String token);

    @GET("history-teacher")
    Call<ArrayList<Lecture>> historyLectures(@Header("Authorization") String token);

    @POST("count-attendance")
    Call<AttendanceResponse> markattendance(@Header("Authorization") String token, @Body AttendanceRequest attendance);

    @POST("get-attendance-lecture")
    Call<ArrayList<AttendanceResultResponse>> getLectureAttendance(@Header("Authorization") String token, @Body AttendanceResultRequest attendanceResultRequest);


    @POST("mark-present")
    Call<AttendanceRequest> markPresent(@Header("Authorization") String token, @Body MarkPresentRequest markPresentRequest);

    @POST("create-notification")
    Call<Notifications> createNotification(@Header("Authorization") String token, @Body Notifications notifications);

    @GET("get-notification-student")
    Call<List<Notifications>> getNotifications(@Header("Authorization") String token);
}
