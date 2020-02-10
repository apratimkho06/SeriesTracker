package com.example.seriestracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchLaterActivity extends AppCompatActivity {

    private CharSequence[] radioButtonValues = {"Remove","Move to Completed Shows"};
    private List<searchService.Detail> movieDetails;
    private DBHelper dbHelper;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);

        dbHelper = new DBHelper(this);
        initializeData();

        final RecyclerView recyclerView = findViewById(R.id.watched_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        if (movieDetails.isEmpty()) {
            Snackbar.make(recyclerView,"Start Adding",Snackbar.LENGTH_SHORT).show();
        }

        adapter = new MovieAdapter(movieDetails);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MovieAdapter.onClickListener() {
            @Override
            public void onItemLongClick(final int position, View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choose");
                builder.setSingleChoiceItems(radioButtonValues, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            try {
                                dbHelper.deleteById(searchService.Detail.class,movieDetails.get(position).id);
                                Toast.makeText(getApplicationContext(),"Deleted!",Toast.LENGTH_SHORT);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            searchService.Detail detail = movieDetails.get(position);
                            detail.Status = true;
                            try {
                                dbHelper.createOrUpdate(detail);
                                Toast.makeText(getApplicationContext(),"Added to Completed Shows!",Toast.LENGTH_SHORT);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                        initializeData();
                        adapter = new MovieAdapter(movieDetails);
                        recyclerView.setAdapter(adapter);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initializeData() {
        movieDetails = new ArrayList<>();
        try {
            movieDetails.addAll(dbHelper.getFilteredMovies(searchService.Detail.class,false));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
