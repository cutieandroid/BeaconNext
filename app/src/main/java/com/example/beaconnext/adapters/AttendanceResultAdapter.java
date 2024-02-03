package com.example.beaconnext.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beaconnext.R;
import com.example.beaconnext.models.AttendanceResultResponse;

import java.util.ArrayList;

public class AttendanceResultAdapter extends RecyclerView.Adapter<AttendanceResultAdapter.ViewHolder> {
    Context context;
    ArrayList<AttendanceResultResponse> attendance;

    public AttendanceResultAdapter(Context context, ArrayList<AttendanceResultResponse> attendance) {
        this.context = context;
        this.attendance = attendance;
    }

    @NonNull
    @Override
    public AttendanceResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendance_result_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceResultAdapter.ViewHolder holder, int position) {

        AttendanceResultResponse attendanceResultResponse = attendance.get(position);

        holder.studentName.setText(attendanceResultResponse.getId().getName());
        holder.attcount.setText("Count: " + String.valueOf(attendanceResultResponse.getCount()));
        boolean status = attendanceResultResponse.isPresent();
        if (status == true) {
            holder.status.setTextColor(Color.GREEN);
        } else {
            holder.status.setTextColor(Color.RED);
        }
        holder.status.setText(String.valueOf(status));
    }

    @Override
    public int getItemCount() {
        return attendance.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, attcount, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.studentName);
            attcount = itemView.findViewById(R.id.attcount);
            status = itemView.findViewById(R.id.status);
        }
    }
}
