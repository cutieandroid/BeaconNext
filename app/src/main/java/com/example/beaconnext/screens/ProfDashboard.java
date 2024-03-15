package com.example.beaconnext.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.beaconnext.R;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.customwidgets.BottomSheet;
import com.example.beaconnext.models.Lecture;
import com.example.beaconnext.models.Teacher;
import com.example.beaconnext.singleton.DateHandler;
import com.example.beaconnext.singleton.LocalStorage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfDashboard extends AppCompatActivity {
    Button createAttendacne;
    TextView sname, placeHolderText, lectime, classroom, subname, lecturer;
    CardView scheduledLectures, cardViewNew, lectureHistory,notibutton,markstudent, requestButton;
    ImageView imageview5;
    LocalStorage ls;
    ProgressBar progressBar;
    AuthApiInterface apiInterface;

    String lectureId;
    SwipeRefreshLayout profdashrefreshlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_dashboard);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);
        Bundle args = new Bundle();
        createAttendacne = findViewById(R.id.createAttendance);
        sname = findViewById(R.id.sname);
        scheduledLectures = findViewById(R.id.scheduledlectures);
        ls = new LocalStorage(this);
        System.out.println(ls.getToken());
        Teacher currentuser = ls.getCurrentTeacher();
        sname.setText(currentuser.getName());
        imageview5 = findViewById(R.id.imageView5);
        cardViewNew = findViewById(R.id.cardViewnew);
        placeHolderText = findViewById(R.id.placeHolderText);
        lectime = findViewById(R.id.lectime);
        classroom = findViewById(R.id.classroom);
        subname = findViewById(R.id.subname);
        lecturer = findViewById(R.id.lecturer);
        progressBar = findViewById(R.id.progressBar);
        lectureHistory = findViewById(R.id.lectureHistory);
        notibutton=findViewById(R.id.notiButton);
        markstudent=findViewById(R.id.markattendance);
       // requestButton = findViewById(R.id.requestButton);
        imageview5.setImageResource(currentuser.getGender().equals("Male") ? R.drawable.userimage_male : R.drawable.userimage);
        profdashrefreshlayout=findViewById(R.id.profdashrefreshlayout);
        checkOngoingLec();

        createAttendacne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfDashboard.this, CreateLecture.class));

            }
        });

        scheduledLectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet bottomSheet = new BottomSheet();
                args.putString("sourceActivity", "teacher");
                bottomSheet.setArguments(args);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });

        lectureHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProfDashboard.this, LectureHistoryActivity.class);
                i.putExtra("flag", "lectureHistory");
                startActivity(i);
            }
        });

        cardViewNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfDashboard.this, AttendanceResult.class);
                i.putExtra("lecture", lectureId);
                i.putExtra("flag","result");
                view.getContext().startActivity(i);
            }
        });

        notibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfDashboard.this,CreateNotification.class);
                startActivity(i);

            }
        });

        profdashrefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkOngoingLec();
                profdashrefreshlayout.setRefreshing(false);
            }
        });

        markstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProfDashboard.this, LectureHistoryActivity.class);
                i.putExtra("flag", "markattendance");
                startActivity(i);

            }
        });
      /*  requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfDashboard.this, LectureHistoryActivity.class);
                i.putExtra("flag", "markPresent");
                startActivity(i);

            }
        });*/
    }


    private void checkOngoingLec() {

        String token = ls.getToken();
        Call<List<Lecture>> ongoinglectureCall = apiInterface.ongoingLectureTeacher(token);
        ongoinglectureCall.enqueue(new Callback<List<Lecture>>() {
            @Override
            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {
                if (response.isSuccessful()) {
                    List<Lecture> temp = response.body();
                    if (temp.size() == 0) {
                        progressBar.setVisibility(View.GONE);
                        cardViewNew.setVisibility(View.INVISIBLE);
                        placeHolderText.setVisibility(View.VISIBLE);
                    } else {
                        Lecture lecture = temp.get(0);
                        lectime.setText(String.format("%s-%s", DateHandler.IstConverter(lecture.getStartTime()), DateHandler.IstConverter(lecture.getEndTime())));
                        classroom.setText(String.valueOf(lecture.getClassRoom()));
                        subname.setText(lecture.getSubjectName());
                        lecturer.setText(lecture.getLecturer());
                        lectureId = lecture.getLectureid();
                        cardViewNew.setVisibility(View.VISIBLE);

                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    cardViewNew.setVisibility(View.INVISIBLE);
                    placeHolderText.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<Lecture>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                cardViewNew.setVisibility(View.INVISIBLE);
                placeHolderText.setText("Oops :(, unable to get ongoing lecture");
                placeHolderText.setVisibility(View.VISIBLE);

            }
        });
    }
}
