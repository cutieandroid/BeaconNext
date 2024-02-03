package com.example.beaconnext.screens;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beaconnext.R;
import com.example.beaconnext.adapters.LectureHistoryAdapter;
import com.example.beaconnext.api.AuthClient.AuthApiClient;
import com.example.beaconnext.api.AuthClient.AuthApiInterface;
import com.example.beaconnext.models.Lecture;
import com.example.beaconnext.singleton.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LectureHistoryActivity extends AppCompatActivity {

    RecyclerView rv;
    LectureHistoryAdapter adapter;
    ArrayList<Lecture> history = new ArrayList<>();
    AuthApiInterface apiInterface;
    TextView placeHolderText;
    LocalStorage ls;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_history);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ls = new LocalStorage(this);
        flag = getIntent().getStringExtra("flag");
        apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);
        rv = findViewById(R.id.rv);
        placeHolderText = findViewById(R.id.placeHolderText);
        adapter = new LectureHistoryAdapter(this, history, flag);
        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rv.setAdapter(adapter);
        getHistory();

    }

    private void getHistory() {
        rv.setVisibility(View.VISIBLE);

        Call<ArrayList<Lecture>> lectureCall = apiInterface.historyLectures(ls.getToken());
        lectureCall.enqueue(new Callback<ArrayList<Lecture>>() {
            @Override
            public void onResponse(Call<ArrayList<Lecture>> call, Response<ArrayList<Lecture>> response) {
                if (response.isSuccessful()) {
                    List<Lecture> temp = response.body();
                    history.addAll(temp);
                    if (history.size() == 0) {
                        rv.setVisibility(View.GONE);
                        placeHolderText.setText("No history available, please create some lectures :)");
                        placeHolderText.setVisibility(View.VISIBLE);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    rv.setVisibility(View.GONE);
                    placeHolderText.setText("Oops :(, Unable to fetch lectures");
                    placeHolderText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Lecture>> call, Throwable t) {

                rv.setVisibility(View.GONE);
                placeHolderText.setText("Oops :(, please check your connection and try again");
                placeHolderText.setVisibility(View.VISIBLE);

            }
        });

    }
}