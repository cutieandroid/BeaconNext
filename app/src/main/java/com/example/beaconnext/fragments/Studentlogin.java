package com.example.beaconnext.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beaconnext.R;
import com.example.beaconnext.Wrapper.SharedPrefWrapper;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;

import com.example.beaconnext.models.AuthModels.Requests.LoginRequest;
import com.example.beaconnext.models.AuthModels.Response.LoginResponse;
import com.example.beaconnext.models.AuthModels.Response.ResponseError;
import com.example.beaconnext.models.Student;
import com.example.beaconnext.screens.Dashboard;
import com.example.beaconnext.screens.Register;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Studentlogin extends Fragment {

    ProgressDialog progressDialog;
    private String androidId;

    TextView loginbtn, toregisterbtn;
    EditText moodleid, loginpass;

    String email;
    private boolean passwordshowing = false;
    ImageView logstudpasswordvisi, repasswordvisi;
    AuthApiInterface apiInterface;
    SharedPreferences sharedPreferences;
    Gson gson;
    Context context;

    public Studentlogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getContext();
        apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);
        sharedPreferences = context.getSharedPreferences("BeaconNext", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_studentlogin, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching your details");
        progressDialog.setTitle("Loading...");
        loginbtn = view.findViewById(R.id.loginbtn);
        loginpass = view.findViewById(R.id.loginpass);
        toregisterbtn = view.findViewById(R.id.toregisterbtn);
        moodleid = view.findViewById(R.id.loginmoodleid);
        repasswordvisi = view.findViewById(R.id.regrepassvisi);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                if (checkFields()) {
                    Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String moodleId = moodleid.getText().toString().trim();
                    String password = loginpass.getText().toString().trim();
                    progressDialog.show();
                    LoginApiCall(moodleId, password, androidId);

                }
            }
        });
        repasswordvisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking if password is showing or not
                if (passwordshowing) {
                    passwordshowing = false;
                    loginpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    repasswordvisi.setImageResource(R.drawable.baseline_visibility_24);
                } else {
                    passwordshowing = true;

                    loginpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    repasswordvisi.setImageResource(R.drawable.baseline_visibility_off_24);

                }
                //set the cursor at last of the text
            }
        });

        toregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Register.class));
            }
        });

        return view;
    }

    public boolean checkFields() {

        if (moodleid.getText().toString().trim().isEmpty() || loginpass.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void LoginApiCall(String moodleId, String password, String deviceId) {
        LoginRequest loginRequest = new LoginRequest(password, deviceId, Integer.parseInt(moodleId));
        Call<LoginResponse> loginResponseCall = apiInterface.studentLogin(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    sharedPreferences.edit().putString("token", loginResponse.getToken()).apply();
                    Toast.makeText(getContext(), "" + loginResponse.getSuccess(), Toast.LENGTH_SHORT).show();
                    getUser(loginResponse.getToken());

                } else {

                    try {
                        ResponseBody errorBody = response.errorBody();
                        String errorbody = errorBody.string();
                        gson = new Gson();
                        ResponseError error = gson.fromJson(errorbody, ResponseError.class);
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "" + error.getError(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Please check your connection :)", Toast.LENGTH_SHORT).show();

            }
        });
    }

    protected void getUser(String token) {
        Call<Student> studentCall = apiInterface.getCurrentStudent(token);
        studentCall.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                Student student = response.body();
                SharedPrefWrapper sharedPrefobject = new SharedPrefWrapper(student.getRole(), student);
                Gson gson = new Gson();
                String studentJson = gson.toJson(sharedPrefobject);
                sharedPreferences.edit().putString("currentuser", studentJson).apply();
                progressDialog.dismiss();
                startActivity(new Intent(getContext(), Dashboard.class));
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Please check your connection :)", Toast.LENGTH_SHORT).show();

            }
        });
    }
}