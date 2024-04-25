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
import com.example.beaconnext.models.Lecture;
import com.example.beaconnext.singleton.DateHandler;
import com.example.beaconnext.singleton.LocalStorage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateLecture extends AppCompatActivity {
    Spinner spinner, spinneryear;
    String selectedDepartment = "Computer";
    Integer selectedYear = 1;
    TextView fromTime, totime;
    Button createAttedanceButton;
    EditText classroom, Subject, division, minTime;
    AuthApiInterface apiInterface;
    LocalStorage localStorage;
    ProgressDialog progressDialog;
    String from, to;
    int FROM_CODE = 0;
    int TO_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lecture);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        localStorage = new LocalStorage(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("BeaconNext");
        progressDialog.setMessage("Creating Lecture...");
        spinner = findViewById(R.id.spinner);
        spinneryear = findViewById(R.id.spinneryear);
        fromTime = findViewById(R.id.fromTime);
        totime = findViewById(R.id.totime);
        createAttedanceButton = findViewById(R.id.createattendancebutton);
        Subject = findViewById(R.id.Subject);
        classroom = findViewById(R.id.classroom);
        division = findViewById(R.id.division);
        minTime = findViewById(R.id.minTime);

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

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker(0);

            }
        });
        totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker(1);
            }
        });

        createAttedanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    Toast.makeText(CreateLecture.this, "All fields are required :(", Toast.LENGTH_SHORT).show();
                } else {
                    String subjectName = Subject.getText().toString();
                    String department = selectedDepartment;
                    String StartTime = from;
                    String EndTime = to;
                    Integer year = selectedYear;
                    String divisiondata = division.getText().toString();
                    Integer classRoomdata = Integer.parseInt(classroom.getText().toString());
                    Integer minimumTime = Integer.parseInt(minTime.getText().toString());
                    createLecture(subjectName, department, StartTime, EndTime, year, divisiondata, classRoomdata, minimumTime);
                }


            }
        });


    }

    private void createLecture(String subjectName, String department, String startTime, String endTime, Integer year, String divisiondata, Integer classRoomdata, Integer minimumTime) {
        progressDialog.show();
        Lecture lecture = new Lecture(subjectName, department, startTime, endTime, year, divisiondata, classRoomdata, minimumTime);
        System.out.println(lecture.toString());
        apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);
        Call<Lecture> lectureCall = apiInterface.createLecture(localStorage.getToken(), lecture);
        lectureCall.enqueue(new Callback<Lecture>() {
            @Override
            public void onResponse(Call<Lecture> call, Response<Lecture> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(CreateLecture.this, "Lecture created successfully :)", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.code()==500) {

                    Toast.makeText(CreateLecture.this, "Ongoing lecture found, cannot create lecture :(", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
                else {
                    progressDialog.dismiss();
                    int message= response.code();
                    Toast.makeText(CreateLecture.this, ""+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Lecture> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreateLecture.this, "Error creating lecture, please check you connection :(", Toast.LENGTH_SHORT).show();
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
        if (code == 0) {
            from = DateHandler.utcConverter(dateTime);
            fromTime.setText(dateTimeToShow);

        } else {
            to = DateHandler.utcConverter(dateTime);
            totime.setText(dateTimeToShow);
        }

    }

    private boolean checkInput() {
        if (Subject.getText().toString().isEmpty() || division.getText().toString().isEmpty() || classroom.getText().toString().isEmpty() || minTime.getText().toString().isEmpty() || from == null || to == null || selectedDepartment == null || selectedYear == null) {

            return true;
        }
        return false;
    }





}
