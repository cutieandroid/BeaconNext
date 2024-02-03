package com.example.beaconnext.screens;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beaconnext.R;
import com.example.beaconnext.adapters.VpAdapter;
import com.example.beaconnext.customwidgets.CustomViewPager;

public class Register extends AppCompatActivity {
    CustomViewPager viewpager;
    TextView studentbtn, profbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewpager = findViewById(R.id.viewpager);
        studentbtn = findViewById(R.id.studentbtn);
        studentbtn.setBackgroundResource(R.drawable.touchbg);
        profbtn = findViewById(R.id.professorbtn);
        VpAdapter vpAdapter = new VpAdapter(getSupportFragmentManager());
        viewpager.setAdapter(vpAdapter);
        viewpager.setCanScroll(false);
        viewpager.setCurrentItem(2);

        studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(2);

                studentbtn.setBackgroundResource(R.drawable.touchbg);
                profbtn.setBackgroundResource(R.drawable.edittextbg);

            }
        });

        profbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(3);

                profbtn.setBackgroundResource(R.drawable.touchbg);
                studentbtn.setBackgroundResource(R.drawable.edittextbg);

            }
        });


    }
}