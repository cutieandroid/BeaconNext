package com.example.beaconnext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beaconnext.R;
import com.example.beaconnext.models.Lecture;
import com.example.beaconnext.singleton.DateHandler;

import java.util.ArrayList;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {
    Context context;
    ArrayList<Lecture> upcomingLectures;

    public BottomSheetAdapter(Context context, ArrayList<Lecture> upcomingLectures) {
        this.context = context;
        this.upcomingLectures = upcomingLectures;
    }

    @NonNull
    @Override
    public BottomSheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lecture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetAdapter.ViewHolder holder, int position) {


        Lecture lecture = upcomingLectures.get(position);
        String startTime = DateHandler.IstConverter(lecture.getStartTime());
        String endTime = DateHandler.IstConverter(lecture.getEndTime());
        holder.lecTime.setText(String.format("%s - %s", startTime, endTime));
        holder.classroom.setText(String.valueOf(lecture.getClassRoom()));
        holder.subname.setText(lecture.getSubjectName());
        holder.lecturer.setText(lecture.getLecturer());

    }

    @Override
    public int getItemCount() {
        return upcomingLectures.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView lecTime, classroom, subname, lecturer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lecTime = itemView.findViewById(R.id.lectime);
            classroom = itemView.findViewById(R.id.classroom);
            subname = itemView.findViewById(R.id.subname);
            lecturer = itemView.findViewById(R.id.lecturer);


        }
    }


}
