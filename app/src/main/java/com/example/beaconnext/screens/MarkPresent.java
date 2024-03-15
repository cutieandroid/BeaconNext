package com.example.beaconnext.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beaconnext.R;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.models.AttendanceRequest;
import com.example.beaconnext.models.MarkPresentRequest;
import com.example.beaconnext.singleton.LocalStorage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarkPresent extends AppCompatActivity {
    EditText moodleId;
   TextView markPresentButton;
   AuthApiInterface authApiInterface;
   LocalStorage ls;
   ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_present);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("BeaconNext");
        progressDialog.setMessage("Marking attendance");
        authApiInterface= AuthApiClient.getClient().create(AuthApiInterface.class);
        ls=new LocalStorage(this);
        String lectureid= getIntent().getStringExtra("lecture");
        moodleId=findViewById(R.id.moodleid);
        markPresentButton=findViewById(R.id.markPresentbtn);
        markPresentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                markPresent(moodleId.getText().toString().trim(),lectureid);
            }
        });


    }
    private void markPresent(String moodleId,String lectureId){
        progressDialog.show();
        MarkPresentRequest markPresentRequest= new MarkPresentRequest(moodleId,lectureId);

        Call<AttendanceRequest> markPresentcall = authApiInterface.markPresent(ls.getToken(), markPresentRequest);
        markPresentcall.enqueue(new Callback<AttendanceRequest>() {
            @Override
            public void onResponse(Call<AttendanceRequest> call, Response<AttendanceRequest> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(MarkPresent.this, "Student has been marked present", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(MarkPresent.this,ProfDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();


                }
                else{
                    Toast.makeText(MarkPresent.this, "Student is not eligible for attendance", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AttendanceRequest> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MarkPresent.this, "Connection problem , please try again :)", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
