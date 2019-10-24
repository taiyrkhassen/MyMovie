package com.example.mymovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mymovies.R;
import com.example.mymovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    List<Movie> movies;
    public onPosterClickListener onPosterClickListener;
    public onRichEndListener onRichEndListener;
    public MovieAdapter(onPosterClickListener clickListener){
        movies = new ArrayList<>();
        this.onPosterClickListener = clickListener;
    }

    public interface onPosterClickListener{
        void onClickItem(int position);
    }

    public interface onRichEndListener{
        void onReachEnd();
    }

    public void setOnRichEndListener(MovieAdapter.onRichEndListener onRichEndListener) {
        this.onRichEndListener = onRichEndListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item, viewGroup, false);
        return new MovieViewHolder(view, onPosterClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        if(movies.size()>=20 && i>movies.size()-4 && onRichEndListener!=null){
            onRichEndListener.onReachEnd();
        }
        Movie movie = movies.get(i);
        ImageView posterImage = movieViewHolder.imagePoster;
        if(!movie.getPosterPath().equals("https://image.tmdb.org/t/p/w185null")) {
            Picasso.get().load(movie.getPosterPath()).into(posterImage);
        } else {
            Picasso.get().load(R.drawable.unavailable).fit().into(posterImage);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imagePoster;
        onPosterClickListener clickListener;
        public MovieViewHolder(@NonNull View itemView, onPosterClickListener clickListener){
            super(itemView);
            imagePoster = itemView.findViewById(R.id.film_poster_images);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClickItem(getAdapterPosition());
        }

    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

}
