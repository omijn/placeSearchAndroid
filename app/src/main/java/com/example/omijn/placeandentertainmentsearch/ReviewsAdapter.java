package com.example.omijn.placeandentertainmentsearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {
    private ArrayList<Review> data;
    private ListItemClickListener clickListener;

    public ReviewsAdapter(ArrayList<Review> data, ListItemClickListener listener) {
        this.data = data;
        this.clickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        ReviewHolder holder = new ReviewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {

        Picasso.get().load(data.get(position).getAuthorPhoto()).into(holder.authorPhotoImageView);
        holder.authorNameTextView.setText(data.get(position).getAuthor());
        holder.timestampTextView.setText(data.get(position).getTimestamp());
        holder.ratingRatingBar.setRating(data.get(position).getRating());
        holder.reviewTextTextView.setText(data.get(position).getReview());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView authorPhotoImageView;
        private TextView authorNameTextView;
        private RatingBar ratingRatingBar;
        private TextView timestampTextView;
        private TextView reviewTextTextView;

        public ReviewHolder(View itemView) {
            super(itemView);

            authorPhotoImageView = itemView.findViewById(R.id.iv_review_author_photo);
            authorNameTextView = itemView.findViewById(R.id.tv_review_author_name);
            ratingRatingBar = itemView.findViewById(R.id.rb_review);
            timestampTextView = itemView.findViewById(R.id.tv_review_timestamp);
            reviewTextTextView = itemView.findViewById(R.id.tv_review_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickListener.onListItemClick(clickedPosition);
        }
    }
}
