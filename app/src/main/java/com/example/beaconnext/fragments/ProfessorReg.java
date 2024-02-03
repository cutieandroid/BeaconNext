package com.example.beaconnext.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.beaconnext.models.AuthModels.Requests.FirstLoginRequest;
import com.example.beaconnext.models.AuthModels.Response.FirstLoginResponse;
import com.example.beaconnext.models.AuthModels.Response.ResponseError;
;
import com.example.beaconnext.models.Teacher;

import com.example.beaconnext.screens.Login;
import com.example.beaconnext.screens.ProfDashboard;
import com.example.beaconnext.screens.Register;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfessorReg extends Fragment {


    TextView tologintbn, registerbtn;
    ImageView passwordvisi, repasswordvisi;
    private boolean passwordshowing = false;
    EditText password, repassword, mid;
    ProgressDialog progressDialog;
    AuthApiInterface apiInterface;
    SharedPreferences sharedPreferences;

    public ProfessorReg() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Context context = getContext();
        // androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);
        sharedPreferences = context.getSharedPreferences("BeaconNext", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_professor_r_eg, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("BeaconNext");
        progressDialog.setMessage("Validating...");
        progressDialog.setCanceledOnTouchOutside(false);
        tologintbn = view.findViewById(R.id.tologinbtn);
        password = view.findViewById(R.id.regpassword);
        repassword = view.findViewById(R.id.regrenterpassword);
        passwordvisi = view.findViewById(R.id.regpassvisi);
        repasswordvisi = view.findViewById(R.id.regrepassvisi);
        mid = view.findViewById(R.id.regmoodleid);
        registerbtn = view.findViewById(R.id.registerbtn);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkempty()) {
                    return;
                } else if (checkpassword()) {
                    return;
                } else {
                    String email = mid.getText().toString().trim();
                    String oldPassword = password.getText().toString().trim();
                    String newPassword = repassword.getText().toString().trim();
                    progressDialog.show();
                    FirstLoginTeacherApiCall(email, oldPassword, newPassword);

                }
            }
        });

        tologintbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Login.class));
            }
        });

        passwordvisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking if password is showing or not
                if (passwordshowing) {
                    passwordshowing = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordvisi.setImageResource(R.drawable.baseline_visibility_24);
                } else {
                    passwordshowing = true;

                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordvisi.setImageResource(R.drawable.baseline_visibility_off_24);
                }
                //set the cursor at last of the text
                password.setSelection(password.length());

            }
        });

        repasswordvisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking if password is showing or not
                if (passwordshowing) {
                    passwordshowing = false;
                    repassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    repasswordvisi.setImageResource(R.drawable.baseline_visibility_24);
                } else {
                    passwordshowing = true;

                    repassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    repasswordvisi.setImageResource(R.drawable.baseline_visibility_off_24);

                }
                //set the cursor at last of the text
            }
        });
        return view;
    }

    public boolean checkpassword() {
        if (password.getText().toString().trim().length() < 5) {
            Toast.makeText(getContext(), "Password too small", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public boolean checkempty() {
        if (mid.getText().toString().trim().isEmpty() || password.getText().toString().isEmpty()
                || repassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    protected void FirstLoginTeacherApiCall(String email, String oldpassword, String newpassword) {
        FirstLoginRequest firstLoginRequest = new FirstLoginRequest(email, oldpassword, newpassword);
        Call<FirstLoginResponse> firstLoginResponseCall = apiInterface.firstLoginTeacher(firstLoginRequest);
        firstLoginResponseCall.enqueue(new Callback<FirstLoginResponse>() {
            @Override
            public void onResponse(Call<FirstLoginResponse> call, Response<FirstLoginResponse> response) {
                if (response.isSuccessful()) {
                    FirstLoginResponse firstLoginResponse = response.body();
                    getUser(firstLoginResponse.getToken());
                    sharedPreferences.edit().putString("token", firstLoginResponse.getToken()).apply();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), firstLoginResponse.getSuccess(), Toast.LENGTH_SHORT).show();

                } else {

                    try {
                        ResponseBody errorBody = response.errorBody();
                        String errorbody = errorBody.string();
                        System.out.println(errorbody);
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
            public void onFailure(Call<FirstLoginResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your connection :)", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

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