package com.example.beaconnext.screens;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.beaconnext.R;
import com.example.beaconnext.Wrapper.SharedPrefWrapper;
import com.example.beaconnext.adapters.VpAdapter;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.customwidgets.CustomViewPager;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {
    CustomViewPager viewpager;
    public static final int REQUEST_CODE_PERMISSIONS = 100;
    TextView studentbtn, profbtn;
    SharedPreferences sharedPreferences;
    AuthApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPermissions();
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkPermissions();
        apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);
        sharedPreferences = getSharedPreferences("BeaconNext", MODE_PRIVATE);
        viewpager = findViewById(R.id.viewpager);
        viewpager.setCanScroll(false);
        studentbtn = findViewById(R.id.studentbtn);
        studentbtn.setBackgroundResource(R.drawable.touchbg);
        profbtn = findViewById(R.id.professorbtn);
        VpAdapter vpAdapter = new VpAdapter(getSupportFragmentManager());
        viewpager.setAdapter(vpAdapter);

        studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(0);
                studentbtn.setBackgroundResource(R.drawable.touchbg);
                profbtn.setBackgroundResource(R.drawable.edittextbg);

            }
        });

        profbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(1);
                profbtn.setBackgroundResource(R.drawable.touchbg);
                studentbtn.setBackgroundResource(R.drawable.edittextbg);

            }
        });

    }


    private void checkPermissions() {
        String[] requiredPermissions = Build.VERSION.SDK_INT < Build.VERSION_CODES.S
                ? new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}
                : new String[]{android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION};
        if (isAnyOfPermissionsNotGranted(requiredPermissions)) {
            ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_CODE_PERMISSIONS);
        }

    }

    private boolean isAnyOfPermissionsNotGranted(String[] requiredPermissions) {
        for (String permission : requiredPermissions) {
            int checkSelfPermissionResult = ContextCompat.checkSelfPermission(this, permission);
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermissionResult) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_CODE_PERMISSIONS == requestCode) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Location permissions are mandatory to use BLE features on Android 6.0 or higher", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String token = sharedPreferences.getString("token", null);
        if (token != null) {
            String currentuser = sharedPreferences.getString("currentuser", null);
            if (currentuser != null) {
                Gson gson = new Gson();
                SharedPrefWrapper sharedprefobj = gson.fromJson(currentuser, SharedPrefWrapper.class);
                if (sharedprefobj.getType().equals("student")) {
                    Intent intent= new Intent(Login.this, Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (sharedprefobj.getType().equals("teacher")) {
                    Intent intent= new Intent(Login.this, ProfDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
            } else {
                Toast.makeText(this, "cannot get user please login again", Toast.LENGTH_SHORT).show();
            }

        }
    }

}


