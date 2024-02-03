package com.example.beaconnext.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beaconnext.R;
import com.example.beaconnext.models.Student;
import com.example.beaconnext.singleton.LocalStorage;

public class StudentProfile extends AppCompatActivity {

    CardView cardView;
    ImageView imageview4;
    TextView sname,sdiv,syear,mmid,mmemail,msdepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardView =findViewById(R.id.cardView);
        cardView.setBackgroundResource(R.drawable.cardbg);
        sname=findViewById(R.id.sname);
        sdiv=findViewById(R.id.sdiv);
        syear=findViewById(R.id.syear);
        mmid=findViewById(R.id.mmid);
        mmemail=findViewById(R.id.mmemail);
        msdepartment=findViewById(R.id.msdepartment);
        imageview4=findViewById(R.id.imageView4);

        LocalStorage ls = new LocalStorage(this);
        Student currentuser= ls.getCurrentStudent();
        sname.setText(currentuser.getName());
        sdiv.setText(currentuser.getDivision());
        syear.setText(currentuser.getYear().toString());
        mmemail.setText(currentuser.getEmail());
        mmid.setText(currentuser.getMoodleId().toString());
        imageview4.setImageResource(currentuser.getGender().equals("Male")?R.drawable.userimage_male:R.drawable.userimage);


    }
}