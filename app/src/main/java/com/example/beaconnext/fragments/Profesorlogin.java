package com.example.beaconnext.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beaconnext.R;
import com.example.beaconnext.Wrapper.SharedPrefWrapper;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.models.AuthModels.Requests.LoginRequest;
import com.example.beaconnext.models.AuthModels.Response.LoginResponse;
import com.example.beaconnext.models.AuthModels.Response.ResponseError;
import com.example.beaconnext.models.Teacher;
import com.example.beaconnext.screens.ProfDashboard;
import com.example.beaconnext.screens.Register;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profesorlogin extends Fragment {

    TextView loginbtn, toregisterbtn;
    EditText moodleid, loginpass;
    private String androidId;
    ProgressDialog progressDialog;

    private boolean passwordshowing = false;
    ImageView repasswordvisi;
    AuthApiInterface apiInterface;
    SharedPreferences sharedPreferences;

    public Profesorlogin() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Context context = getContext();
        apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);
        androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        sharedPreferences = context.getSharedPreferences("BeaconNext", 0);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_professorlogin, container, false);
        loginbtn = view.findViewById(R.id.loginbtn);
        moodleid = view.findViewById(R.id.loginmoodleid);
        toregisterbtn = view.findViewById(R.id.toregisterbtn);
        loginpass = view.findViewById(R.id.loginpass);
        repasswordvisi = view.findViewById(R.id.regrepassvisi);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching your details");
        progressDialog.setTitle("Loading...");

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String email = moodleid.getText().toString().trim();
                    String password = loginpass.getText().toString().trim();
                    progressDialog.show();
                    LoginApiCall(email, password);

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

    public void LoginApiCall(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> loginResponseCall = apiInterface.teacherLogin(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    getUser(loginResponse.getToken());
                    sharedPreferences.edit().putString("token", loginResponse.getToken()).apply();
                    Toast.makeText(getContext(), "" + loginResponse.getSuccess(), Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        ResponseBody errorBody = response.errorBody();
                        String errorbody = errorBody.string();
                        Gson gson = new Gson();
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
        Call<Teacher> teacherCall = apiInterface.getCurrentTeacher(token);
        teacherCall.enqueue(new Callback<Teacher>() {
            @Override
            public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                Teacher teacher = response.body();
                SharedPrefWrapper sharedPrefobject = new SharedPrefWrapper(teacher.getRole(), teacher);
                Gson gson = new Gson();
                String teacherJson = gson.toJson(sharedPrefobject);
                sharedPreferences.edit().putString("currentuser", teacherJson).apply();
                progressDialog.dismiss();
                startActivity(new Intent(getContext(), ProfDashboard.class));
            }
            @Override
            public void onFailure(Call<Teacher> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Please check your connection :)", Toast.LENGTH_SHORT).show();

            }
        });
    }

}