package com.example.seriestracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.seriestracker.searchService.Detail;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static onClickListener onClickListener;
    private Context context;
    private List<Detail> movieDetails;

    public MovieAdapter(List<Detail> movieDetails) {
        this.movieDetails = movieDetails;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        CardView cardView;
        TextView title,directorName,year;
        ImageView moviePoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            cardView = itemView.findViewById(R.id.cardview);
            title = itemView.findViewById(R.id.cv_title);
            directorName = itemView.findViewById(R.id.cv_director);
            moviePoster = itemView.findViewById(R.id.iv_poster);
            year = itemView.findViewById(R.id.cv_release);
        }

        @Override
        public boolean onLongClick(View v) {
            onClickListener.onItemLongClick(getAdapterPosition(),v);
            return true;
        }
    }

    public void setOnItemClickListener(onClickListener onclicklistener) {
        MovieAdapter.onClickListener = onclicklistener;
    }

    public interface onClickListener {
        void onItemLongClick(int position, View v);
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_db_movie,parent,false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(v);
        context = parent.getContext();
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        holder.title.setText(movieDetails.get(position).Title);
        holder.directorName.setText(movieDetails.get(position).Director);
        holder.year.setText(movieDetails.get(position).Year);
        final String imageUrl;
        if (! movieDetails.get(position).Poster.equals("N/A")) {
            imageUrl = movieDetails.get(position).Poster;
        } else {
            // default image if there is no poster available
            imageUrl = "http://www.imdb.com/images/nopicture/medium/film.png";
        }
        holder.moviePoster.layout(0, 0, 0, 0); // invalidate the width so that glide wont use that dimension
        Glide.with(context).load(imageUrl).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return movieDetails.size();
    }
}
