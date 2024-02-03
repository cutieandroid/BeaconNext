package com.example.beaconnext.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beaconnext.R;
import com.example.beaconnext.models.Lecture;
import com.example.beaconnext.screens.AttendanceResult;
import com.example.beaconnext.singleton.DateHandler;

import java.util.ArrayList;

public class LectureHistoryAdapter extends RecyclerView.Adapter<LectureHistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<Lecture> history;
    String flag;

    public LectureHistoryAdapter(Context context, ArrayList<Lecture> history, String flag) {
        this.context = context;
        this.history = history;
        this.flag = flag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lecture_history_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lecture lecture = history.get(position);
        holder.lectime.setText(String.format("%s-%s", DateHandler.IstConverter(lecture.getStartTime()), DateHandler.IstConverter(lecture.getEndTime())));
        holder.classroom.setText(String.valueOf(lecture.getClassRoom()));
        holder.subname.setText(lecture.getSubjectName());
        holder.lecturer.setText(lecture.getLecturer());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AttendanceResult.class);
                i.putExtra("lecture", lecture.getLectureid());
                i.putExtra("flag", flag);
                view.getContext().startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView lectime, classroom, subname, lecturer;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            lectime = itemView.findViewById(R.id.lectime);
            classroom = itemView.findViewById(R.id.classroom);
            subname = itemView.findViewById(R.id.subname);
            lecturer = itemView.findViewById(R.id.lecturer);

        }
    }
}
