package com.example.beaconnext.screens;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.beaconnext.R;
import com.example.beaconnext.adapters.AttendanceResultAdapter;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.models.AttendanceRequest;
import com.example.beaconnext.models.AttendanceResultRequest;
import com.example.beaconnext.models.AttendanceResultResponse;
import com.example.beaconnext.models.MarkPresentRequest;
import com.example.beaconnext.singleton.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceResult extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<AttendanceResultResponse> attendance = new ArrayList<>();
    AttendanceResultAdapter adapter;
    AuthApiInterface apiInterface;
    String lectureId;
    ProgressBar progressBar;
    TextView placeHolderText, markPresentBtn;
    EditText moodleId;
    LocalStorage ls;
    SwipeRefreshLayout attrefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_result);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //String flag = getIntent().getStringExtra("flag");
        apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);
        lectureId = getIntent().getStringExtra("lecture");
        attrefreshLayout=findViewById(R.id.attrefreshlayout);

        ls = new LocalStorage(this);
        /*if (flag.equals("markPresent")) {
            setContentView(R.layout.mark_present);
            markPresentBtn = findViewById(R.id.markPresentbtn);
            moodleId = findViewById(R.id.moodleid);
            markPresentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (moodleId.getText().toString().isEmpty()) {
                        Toast.makeText(AttendanceResult.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String mid = moodleId.getText().toString();
                    markPresent(mid, lectureId);
                }
            });

        }*/


            rv = findViewById(R.id.rv);
            placeHolderText = findViewById(R.id.placeHolderText);
            progressBar = findViewById(R.id.progressBar);
            rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            adapter = new AttendanceResultAdapter(this, attendance);
            rv.setAdapter(adapter);
            showAttendance();

        attrefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                attendance.clear();
                showAttendance();
                attrefreshLayout.setRefreshing(false);
            }
        });
        }




    private void markPresent(String mid, String lectureId) {
        MarkPresentRequest request = new MarkPresentRequest(mid, lectureId);
        Call<AttendanceRequest> responseCall = apiInterface.markPresent(ls.getToken(), request);
        responseCall.enqueue(new Callback<AttendanceRequest>() {
            @Override
            public void onResponse(Call<AttendanceRequest> call, Response<AttendanceRequest> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AttendanceResult.this, "Student has been marked present", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AttendanceResult.this, "Cannot mark this student present please check moodle id", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AttendanceRequest> call, Throwable t) {
                Toast.makeText(AttendanceResult.this, "Cannot mark this student present please check your connection", Toast.LENGTH_SHORT).show();


            }
        });

    }

    void showAttendance() {
        System.out.println(lectureId);
        AttendanceResultRequest requestbody = new AttendanceResultRequest(lectureId);
        Call<ArrayList<AttendanceResultResponse>> getresultCall = apiInterface.getLectureAttendance(ls.getToken(), requestbody);
        getresultCall.enqueue(new Callback<ArrayList<AttendanceResultResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AttendanceResultResponse>> call, Response<ArrayList<AttendanceResultResponse>> response) {
                if (response.isSuccessful()) {
                    List<AttendanceResultResponse> temp = response.body();
                    attendance.addAll(temp);
                    if (attendance.size() == 0) {
                        progressBar.setVisibility(View.GONE);
                        placeHolderText.setText("No attendance available :) , wait for students to mark attendancce");
                        placeHolderText.setVisibility(View.VISIBLE);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    placeHolderText.setText("No Attendance yet :(");
                    placeHolderText.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<AttendanceResultResponse>> call, Throwable t) {
                System.out.println(t.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
                placeHolderText.setText("Connection problem :( please check your connection and try again");
                placeHolderText.setVisibility(View.VISIBLE);

            }
        });

    }

}