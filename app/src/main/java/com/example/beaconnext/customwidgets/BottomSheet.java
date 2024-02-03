package com.example.beaconnext.customwidgets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beaconnext.R;
import com.example.beaconnext.adapters.BottomSheetAdapter;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.models.Lecture;
import com.example.beaconnext.singleton.LocalStorage;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheet extends BottomSheetDialogFragment {
    RecyclerView bottomSheetRv;
    ArrayList<Lecture> upcomingLectures = new ArrayList<>();
    TextView placeHolderText;
    ProgressBar progressbar;
    LocalStorage ls;
    String token;
    AuthApiInterface apiInterface;
    BottomSheetAdapter bottomSheetAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);
        placeHolderText = view.findViewById(R.id.placeHolderText);
        bottomSheetRv = view.findViewById(R.id.bottomSheetRv);
        progressbar = view.findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        ls = new LocalStorage(getContext());
        bottomSheetAdapter = new BottomSheetAdapter(getContext(), upcomingLectures);
        bottomSheetRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        bottomSheetRv.setAdapter(bottomSheetAdapter);
        Bundle args = getArguments();
        assert args != null;
        if (args.get("sourceActivity").equals("student")) {
            getUpcomingLecturesStudent();
        } else {
            getUpcomingLecturesTeachers();
        }


        return view;

    }

    private void getUpcomingLecturesTeachers() {
        token = ls.getToken();
        Call<List<Lecture>> lectureCall = apiInterface.upcomingLectureTeacher(token);
        lectureCall.enqueue(new Callback<List<Lecture>>() {
            @Override
            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {
                if (response.isSuccessful()) {
                    List<Lecture> tempList = response.body();
                    // System.out.println(tempList);
                    if (tempList.size() != 0) {
                        progressbar.setVisibility(View.GONE);
                        upcomingLectures.addAll(tempList);
                        bottomSheetAdapter.notifyDataSetChanged();
                    } else {
                        progressbar.setVisibility(View.GONE);
                        bottomSheetRv.setVisibility(View.GONE);
                        placeHolderText.setVisibility(View.VISIBLE);

                    }


                } else {
                    progressbar.setVisibility(View.GONE);
                    bottomSheetRv.setVisibility(View.GONE);
                    placeHolderText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Lecture>> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                bottomSheetRv.setVisibility(View.GONE);
                placeHolderText.setVisibility(View.VISIBLE);
                placeHolderText.setText("Cannot get Upcoming lectures.Please check your Connection :(");
            }
        });

    }

    private void getUpcomingLecturesStudent() {
        token = ls.getToken();
        Call<List<Lecture>> lectureCall = apiInterface.upcomingLectureStudent(token);
        lectureCall.enqueue(new Callback<List<Lecture>>() {
            @Override
            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {
                if (response.isSuccessful()) {
                    List<Lecture> tempList = response.body();
                    // System.out.println(tempList);
                    if (tempList.size() != 0) {
                        progressbar.setVisibility(View.GONE);
                        upcomingLectures.addAll(tempList);
                        bottomSheetAdapter.notifyDataSetChanged();
                    } else {
                        progressbar.setVisibility(View.GONE);
                        bottomSheetRv.setVisibility(View.GONE);
                        placeHolderText.setVisibility(View.VISIBLE);

                    }


                } else {
                    progressbar.setVisibility(View.GONE);
                    bottomSheetRv.setVisibility(View.GONE);
                    placeHolderText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Lecture>> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                bottomSheetRv.setVisibility(View.GONE);
                placeHolderText.setVisibility(View.VISIBLE);
                placeHolderText.setText("Cannot get Upcoming lectures.Please check your Connection :(");
            }
        });


    }
}




