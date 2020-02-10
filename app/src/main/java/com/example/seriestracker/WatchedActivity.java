package com.example.seriestracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchedActivity extends AppCompatActivity {

    private List<searchService.Detail> movieDetails;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched);

        dbHelper = new DBHelper(this);
        initializeData();

        RecyclerView recyclerView = findViewById(R.id.watched_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        MovieAdapter adapter = new MovieAdapter(movieDetails);
        recyclerView.setAdapter(adapter);
    }

    private void initializeData() {
        movieDetails = new ArrayList<>();
        try {
            movieDetails.addAll(dbHelper.getFilteredMovies(searchService.Detail.class,true));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
