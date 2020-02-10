package com.example.seriestracker;

import android.app.FragmentManager;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MovieActivity extends AppCompatActivity {

    private searchService.Detail movieDetail;
    private TextView title;

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        tabLayout = findViewById(R.id.tabs);
        appBarLayout = findViewById(R.id.appBarLayout);
        viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TabInfo(),"INFO");
        viewPagerAdapter.addFragment(new TabSeasons(),"SEASONS");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        movieDetail = (searchService.Detail) getIntent().getParcelableExtra("detail");
        title = findViewById(R.id.title);
        title.setText(movieDetail.Title);

        sendData();
    }

    public searchService.Detail sendData() {
        return movieDetail;
    }
}