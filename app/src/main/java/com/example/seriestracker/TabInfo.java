package com.example.seriestracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public class TabInfo extends Fragment {

    private TextView tvImdbRating,tvRelease,tvRuntime,tvGenre,tvLanguage;
    private searchService.Detail detail;

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MovieActivity movieActivity = (MovieActivity) getActivity();
        Log.d("fgfg",movieActivity.sendData().toString());
        detail =  movieActivity.sendData();
    }*/

    public TabInfo() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info, container, false);
//        tvImdbRating = root.findViewById(R.id.info_imdb_rating);
//        tvRelease = root.findViewById(R.id.info_release);
//        tvRuntime = root.findViewById(R.id.info_runtime);
//        tvGenre = root.findViewById(R.id.info_genre);
//        tvLanguage = root.findViewById(R.id.info_language);

        Log.d("onCreateView","ngfhggj");
        //tvImdbRating.setText(detail.imdbRating);
        return root;
    }
}
