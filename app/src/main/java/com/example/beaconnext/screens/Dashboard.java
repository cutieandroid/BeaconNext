package com.example.beaconnext.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.beaconnext.App;
import com.example.beaconnext.R;
import com.example.beaconnext.adapters.NotificationAdapter;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.customwidgets.BottomSheet;
import com.example.beaconnext.models.Lecture;
import com.example.beaconnext.models.Notifications;
import com.example.beaconnext.models.Student;
import com.example.beaconnext.service.ForegroundScan;
import com.example.beaconnext.singleton.DateHandler;
import com.example.beaconnext.singleton.LocalStorage;
import com.google.gson.Gson;
import com.kontakt.sdk.android.ble.manager.ProximityManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {
    CardView noticard, scheduledLectures,ongoinglec;
    Gson gson;
    NotificationAdapter notificationAdapter;

    ArrayList<Notifications> notifications = new ArrayList<>();

    RecyclerView notificationrv;
    TextView sname,placeHolderText,lectime,classroom,subname,lecturer;
    ImageView imageview5;
    LocalStorage ls;
    AuthApiInterface apiInterface;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    public static final String TAG = "ProximityManager";
    String lectureId;
    SwipeRefreshLayout refreshlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apiInterface= AuthApiClient.getClient().create(AuthApiInterface.class);
        progressDialog = new ProgressDialog(Dashboard.this);
        progressDialog.setTitle("BeaconNext");
        progressDialog.setMessage("Loading");
        notificationrv = findViewById(R.id.dashboardrv);
        notificationrv.setLayoutManager(new LinearLayoutManager(Dashboard.this, RecyclerView.VERTICAL, false));
        notificationAdapter = new NotificationAdapter(Dashboard.this, notifications);
        notificationrv.setAdapter(notificationAdapter);
        noticard = findViewById(R.id.noticard);
        noticard.setBackgroundResource(R.drawable.edittextbg);
        imageview5=findViewById(R.id.imageView5);
        sname = findViewById(R.id.sname);
        ongoinglec=findViewById(R.id.ongoinglec);
        placeHolderText=findViewById(R.id.placeHolderText);
        lectime=findViewById(R.id.lectime);
        classroom=findViewById(R.id.classroom);
        subname=findViewById(R.id.subname);
        lecturer=findViewById(R.id.lecturer);
        progressBar=findViewById(R.id.progressBar);
        refreshlayout=findViewById(R.id.refreshlayout);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkOngoingLec();
                notifications.clear();
                getNoti();
                refreshlayout.setRefreshing(false);
            }
        });

        ls= new LocalStorage(this);
        System.out.println(ls.getToken());
        Student currentuser=ls.getCurrentStudent();
        sname.setText(currentuser.getName());
        imageview5.setImageResource(currentuser.getGender().equals("male")?R.drawable.userimage_male:R.drawable.userimage);
        checkOngoingLec();
        getNoti();

        scheduledLectures = findViewById(R.id.scheduledlectures);
        scheduledLectures.setBackgroundResource(R.drawable.cardbg);
        scheduledLectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet bottomSheet = new BottomSheet();
                Bundle args = new Bundle();
                args.putString("sourceActivity", "student");
                bottomSheet.setArguments(args);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });
        imageview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(Dashboard.this,StudentProfile.class));
            }
        });

        ongoinglec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void checkOngoingLec() {
        String token= ls.getToken();
        Call<List<Lecture>> ongoinglectureCall= apiInterface.ongoingLectureStudent(token);
        ongoinglectureCall.enqueue(new Callback<List<Lecture>>() {
            @Override
            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {
                if(response.isSuccessful()){
                    List<Lecture> temp= response.body();
                    if(temp.size()==0){
                        progressBar.setVisibility(View.GONE);
                        ongoinglec.setVisibility(View.INVISIBLE);
                        placeHolderText.setVisibility(View.VISIBLE);
                    }
                    else{
                        Lecture lecture= temp.get(0);
                        Intent i= new Intent(Dashboard.this, ForegroundScan.class);
                        startService(i);
                        lectime.setText(String.format("%s-%s",DateHandler.IstConverter(lecture.getStartTime()), DateHandler.IstConverter(lecture.getEndTime())));
                        classroom.setText(String.valueOf(lecture.getClassRoom()));
                        subname.setText(lecture.getSubjectName());
                        lecturer.setText(lecture.getLecturer());
                        lectureId=lecture.getLectureid();
                        ongoinglec.setVisibility(View.VISIBLE);


                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    ongoinglec.setVisibility(View.INVISIBLE);
                    placeHolderText.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<Lecture>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                ongoinglec.setVisibility(View.INVISIBLE);
                placeHolderText.setText("Oops :(, unable to get ongoing lecture");
                placeHolderText.setVisibility(View.VISIBLE);

            }
        });
    }
    private void getNoti(){
        String token=ls.getToken();
        Call<List<Notifications>> notificationcall= apiInterface.getNotifications(token);
        notificationcall.enqueue(new Callback<List<Notifications>>() {
            @Override
            public void onResponse(Call<List<Notifications>> call, Response<List<Notifications>> response) {
                List<Notifications> tempnotifications=response.body();
                notifications.addAll(tempnotifications);
                notificationAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Notifications>> call, Throwable t) {

            }
        });

    }
}