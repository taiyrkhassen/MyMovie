package com.example.mymovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymovies.R;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Video;

import java.util.ArrayList;
import java.util.List;

public class TraillerAdapter extends RecyclerView.Adapter<TraillerAdapter.TrailerHolder> {
    List<Video> trailerArray;
    List<Video> sdsdd;
    List<Review> sds;
    OnTraillerClickListener clickListener;
    public TraillerAdapter(){
        trailerArray = new ArrayList<>();
    }

    public interface OnTraillerClickListener{
        void onClickTrailler(String url);
    }


    public void setClickListener(OnTraillerClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, viewGroup, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder trailerHolder, int i) {
        Video video = trailerArray.get(i);
        trailerHolder.textViewName.setText(video.getName());
    }

    @Override
    public int getItemCount() {
        return trailerArray.size();
    }

    class TrailerHolder extends RecyclerView.ViewHolder {
        ImageView imagePlay;
        TextView textViewName;
        public TrailerHolder(@NonNull final View itemView) {
            super(itemView);
            imagePlay = itemView.findViewById(R.id.imageViewPlay);
            textViewName = itemView.findViewById(R.id.textViewNameVideo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClickTrailler(trailerArray.get(getAdapterPosition()).getKey());
                }
            });
        }
    }

    public void setTrailers(List<Video> trailerArray) {
        this.trailerArray = trailerArray;
        notifyDataSetChanged();
    }
}
