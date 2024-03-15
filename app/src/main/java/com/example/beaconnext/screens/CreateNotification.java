package com.example.beaconnext.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.beaconnext.R;
import com.example.beaconnext.adapters.SpinnerAdapter;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.models.Notifications;
import com.example.beaconnext.singleton.DateHandler;
import com.example.beaconnext.singleton.LocalStorage;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNotification extends AppCompatActivity {

    LocalStorage localStorage;
    Spinner spinner, spinneryear;
    String selectedDepartment = "Computer";
    Integer selectedYear = 1;
    TextView toTime;
    Button createNotificationButton;
    EditText notititle,notidescription,division;
    AuthApiInterface apiInterface;
    ProgressDialog progressDialog;
    int TO_CODE = 1;
    String from, to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        localStorage = new LocalStorage(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("BeaconNext");
        progressDialog.setMessage("Creating Notification...");
        spinner = findViewById(R.id.spinner);
        spinneryear = findViewById(R.id.spinneryear);
        toTime = findViewById(R.id.totime);
        createNotificationButton = findViewById(R.id.createnotificationbutton);
        notititle=findViewById(R.id.notititle);
        notidescription=findViewById(R.id.notidescription);
        division=findViewById(R.id.division);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this);

        spinner.setAdapter(SpinnerAdapter.Deptadapter);
        spinneryear.setAdapter(SpinnerAdapter.yearadapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDepartment = SpinnerAdapter.departments[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinneryear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedYear = Integer.parseInt(SpinnerAdapter.years[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker(1);
            }
        });

        createNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    Toast.makeText(CreateNotification.this, "All fields are required :(", Toast.LENGTH_SHORT).show();
                } else {
                    String title = notititle.getText().toString();
                    String department = selectedDepartment;;
                    String EndTime = to;
                    String year = selectedYear.toString();
                    String divisiondata = division.getText().toString();
                    String description= notidescription.getText().toString();
                    createNoti(title,department,EndTime,year,divisiondata,description);
                }
            }
        });

    }

    private void createNoti(String title, String department, String endTime, String year, String divisiondata, String description) {

        progressDialog.show();
        Notifications creatednotifications= new Notifications(title,description,department,year,divisiondata,endTime);
        apiInterface= AuthApiClient.getClient().create(AuthApiInterface.class);
        Call<Notifications> notificationsCall= apiInterface.createNotification(localStorage.getToken(), creatednotifications);
        notificationsCall.enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(CreateNotification.this, "Lecture created successfully :)", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    progressDialog.dismiss();
                    int message= response.code();
                    Toast.makeText(CreateNotification.this, ""+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreateNotification.this, "Error creating lecture, please check you connection :(", Toast.LENGTH_SHORT).show();

            }
        });

    }




    private void showDateTimePicker(int code) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Handle the date selection
                        // Now, show the TimePicker
                        showTimePicker(selectedYear, selectedMonth, selectedDay, code);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    private void showTimePicker(int selectedYear, int selectedMonth, int selectedDay, int code) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        String month = "";
                        String min = "";
                        String hur = "";
                        String day = "";
                        // Handle the time selection
                        // Now, you have both the selected date and time
                        if (selectedMonth + 1 < 10) {
                            month = "0" + (selectedMonth + 1);
                        } else {
                            month = String.valueOf(selectedMonth + 1);
                        }
                        if (selectedMinute < 10) {
                            min = "0" + selectedMinute;
                        } else {
                            min = String.valueOf(selectedMinute);
                        }
                        if (selectedHour < 10) {
                            hur = "0" + selectedHour;
                        } else {
                            hur = String.valueOf(selectedHour);
                        }
                        if (selectedDay < 10) {
                            day = "0" + selectedDay;
                        } else {
                            day = String.valueOf(selectedDay);
                        }

                        String dateTime = selectedYear + "-" + month + "-" + day + "T" + hur + ":" + min + ":00";
                        String dateTimeToShow=day+"/"+month+"-"+hur+":"+min;
                        showDateTime(dateTime, code,dateTimeToShow);
                    }
                },
                hour, minute, true
        );
        timePickerDialog.show();

    }

    private void showDateTime(String dateTime, int code,String dateTimeToShow) {
        DateHandler handler= new DateHandler();
        if (code == 1) {
            to = DateHandler.utcConverter(dateTime);
            toTime.setText(dateTimeToShow);

        }
    }

    private boolean checkInput() {
        if (notititle.getText().toString().isEmpty() || division.getText().toString().isEmpty() || notidescription.getText().toString().isEmpty()|| to== null || selectedDepartment == null || selectedYear == null) {

            return true;
        }
        return false;
    }
}