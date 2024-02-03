package com.example.beaconnext.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SpinnerAdapter {
    public static String[] departments = {"Computer", "IT", "AI&ML", "DataScience", "Civil", "Mechanical", "First Year"};

    public static String[] years = {"1", "2", "3", "4"};

    public static ArrayAdapter<String> Deptadapter;
    public static ArrayAdapter<String> yearadapter;
    Context context;

    public SpinnerAdapter(Context context) {
        this.context = context;
        Deptadapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, departments);
        Deptadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearadapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, years);
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


}
