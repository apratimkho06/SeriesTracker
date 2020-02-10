package com.example.seriestracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.j256.ormlite.stmt.query.In;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<searchService.ResultWithDetail> {

    private CharSequence[] radioButtonValues = {"Add to Watched","Add to Watch Later"};
    private Button mSearchButton;
    private EditText mSearchEditText;
    private RecyclerView mMovieListRecyclerView;
    private MovieRecyclerViewAdapter mMovieAdapter;
    private String mMovieTitle,typeSelection;
    private ProgressBar mProgressBar;
    private Spinner mTypeSpinner;
    private DBHelper dbHelper;

    private static final int LOADER_ID = 1;

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(MainActivity.this);
        mSearchEditText = findViewById(R.id.search_edittext);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    startSearch();
                    handled = true;
                }
                return handled;
            }
        });
        mTypeSpinner = findViewById(R.id.spinner);
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelection = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                typeSelection = parent.getItemAtPosition(0).toString();
            }
        });

        mSearchButton = findViewById(R.id.search_button);
        mMovieListRecyclerView = findViewById(R.id.recycler_view);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List movieDetailList = new ArrayList<>();
                try {
                    movieDetailList.addAll(dbHelper.getAll(searchService.Detail.class));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for(Object detail : movieDetailList) {
                    Log.d(LOG_TAG,detail.toString());
                }
                startSearch();
            }
        });
        mMovieAdapter = new MovieRecyclerViewAdapter(null);
        mMovieListRecyclerView.setAdapter(mMovieAdapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count),StaggeredGridLayoutManager.VERTICAL);
        mMovieListRecyclerView.setItemAnimator(null);
        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
        getSupportLoaderManager().enableDebugLogging(true);
        mProgressBar = findViewById(R.id.progress_spinner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_watched:
                startActivity(new Intent(MainActivity.this,WatchedActivity.class));
                return true;
            case R.id.action_plan_to_watch:
                startActivity(new Intent(MainActivity.this,WatchLaterActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mMovieTitle",mMovieTitle);
        outState.putString("mTypeSelection",typeSelection);
        outState.putInt("progress_visibility",mProgressBar.getVisibility());
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int progress_visibility = savedInstanceState.getInt("progress_visibility");
        if(progress_visibility == View.VISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mMovieTitle = savedInstanceState.getString("mMovieTitle");
        if (mMovieTitle != null) {
            Bundle args = new Bundle();
            args.putString("typeSelection",typeSelection);
            args.putString("movieTitle",mMovieTitle);
            getSupportLoaderManager().initLoader(LOADER_ID,args,this);
        }
    }

    @NonNull
    @Override
    public Loader<searchService.ResultWithDetail> onCreateLoader(int id, @Nullable Bundle args) {
        return new RetrofitLoader(MainActivity.this,args.getString("movieTitle"),args.getString("typeSelection"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<searchService.ResultWithDetail> loader, searchService.ResultWithDetail data) {
        mProgressBar.setVisibility(View.GONE);
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
        if(data.getResponse().equals("True")) {
            mMovieAdapter.swapData(data.getMovieDetailList());
        } else {
            Snackbar.make(mMovieListRecyclerView,
                    "The movie title you entered was not found", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<searchService.ResultWithDetail> loader) {
        mMovieAdapter.swapData(null);
    }

    public class MovieRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

        private List<searchService.Detail> mValues;

        public MovieRecyclerViewAdapter(List<searchService.Detail> items) {
            mValues = items;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_movie, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final searchService.Detail detail = mValues.get(position);
            final String title = detail.Title;
            final String imdbId = detail.imdbID;
            final String imdbRating = detail.imdbRating;
            final String runtime = detail.Runtime;
            final String year = detail.Year;
            holder.mImdbRating.setText(imdbRating);
            holder.mTitleView.setText(title);
            holder.mYearView.setText(year);

            final String imageUrl;
            if (! detail.Poster.equals("N/A")) {
                imageUrl = detail.Poster;
            } else {
                // default image if there is no poster available
                imageUrl = "http://www.imdb.com/images/nopicture/medium/film.png";
            }
            holder.mThumbImageView.layout(0, 0, 0, 0); // invalidate the width so that glide wont use that dimension
            Glide.with(MainActivity.this).load(imageUrl).into(holder.mThumbImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG,detail.toString());

                    Intent intent = new Intent(MainActivity.this,MovieActivity.class);
                    intent.putExtra("detail",detail);
                    startActivity(intent);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.mView.getContext());
//                    builder.setTitle("Choose");
//                    builder.setSingleChoiceItems(radioButtonValues, -1, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Log.d(LOG_TAG,detail.toString());
//                            if (which == 0) {
//                                detail.setStatus(true);
//                            } else {
//                                detail.setStatus(false);
//                            }
//                            Log.d(LOG_TAG,detail.toString());
//                            try {
//                                dbHelper.createOrUpdate(detail);
//                                Toast.makeText(getApplicationContext(),"Successfully Added! " + detail.Title,Toast.LENGTH_SHORT);
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
//                            dialog.dismiss();
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mValues == null) {
                return 0;
            }
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mYearView;
            public final TextView mImdbRating;
            public final ImageView mThumbImageView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.movie_title);
                mYearView = (TextView) view.findViewById(R.id.movie_year);
                mThumbImageView = (ImageView) view.findViewById(R.id.thumbnail);
                mImdbRating = (TextView) view.findViewById(R.id.movie_imdb_rating);
            }

        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
            Glide.clear(holder.mThumbImageView);
        }

        public void swapData(List<searchService.Detail> items) {
            if(items != null) {
                mValues = items;
                notifyDataSetChanged();

            } else {
                mValues = null;
            }
        }
    }

    private void startSearch() {
        if(CommonUtils.isNetworkAvailable(getApplicationContext())) {
            CommonUtils.hideSoftKeyboard(MainActivity.this);
            String movieTitle = mSearchEditText.getText().toString().trim();
            if (!movieTitle.isEmpty()) {
                Bundle args = new Bundle();
                args.putString("movieTitle", movieTitle);
                args.putString("typeSelection",typeSelection);
                getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                mMovieTitle = movieTitle;
                mProgressBar.setVisibility(View.VISIBLE);
                mMovieListRecyclerView.setVisibility(View.GONE);
            }
            else
                Snackbar.make(mMovieListRecyclerView,
                        "Please enter a movie title",
                        Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(mMovieListRecyclerView,
                    "Network not available! Please connect to the Internet.",
                    Snackbar.LENGTH_LONG).show();
        }
    }
}
