package com.example.mymovies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymovies.R;
import com.example.mymovies.data.Review;

import java.util.ArrayList;
import java.util.List;

//extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{
    List<Review> arrayReview;

    public ReviewAdapter(){
        arrayReview = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewHolder reviewViewHolder, int i) {
        Review review = arrayReview.get(i);
        reviewViewHolder.textViewName.setText(review.getAuthor());
        reviewViewHolder.textViewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return arrayReview.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewContent;
        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewName = itemView.findViewById(R.id.textViewNameMen);
        }
    }

    public void setReviews(List<Review> arrayReview) {
        this.arrayReview = arrayReview;
        notifyDataSetChanged();
    }
}
