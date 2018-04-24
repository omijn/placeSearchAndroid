package com.example.omijn.placeandentertainmentsearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultHolder> {

    private ArrayList<PlaceResult> data;
    private ListItemClickListener clickListener;

    public ResultsAdapter(ArrayList<PlaceResult> data, ListItemClickListener listener) {
        this.data = data;
        clickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemDetailsSurfaceClicked(int clickedItemIndex);
        void onListItemFavoriteToggleClicked(int clickedItemIndex);
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_result_list_item, parent, false);
        ResultHolder holder = new ResultHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {

        PlaceResult currentPlaceResult = data.get(position);

        holder.nameTextView.setText(currentPlaceResult.getName());
        holder.addressTextView.setText(currentPlaceResult.getAddress());
        Picasso.get().load(currentPlaceResult.getIcon()).into(holder.iconImageView);

        if (currentPlaceResult.isFavorite()) {
            holder.favoriteImageView.setImageResource(R.drawable.heart_fill_red);
        } else {
            holder.favoriteImageView.setImageResource(R.drawable.heart_outline_black);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ResultHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView addressTextView;
        private ImageView iconImageView;
        private ImageView favoriteImageView;
        private LinearLayout detailsClickableLL;

        public ResultHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_place_result_name);
            addressTextView = itemView.findViewById(R.id.tv_place_result_address);
            iconImageView = itemView.findViewById(R.id.iv_place_result_icon);
            favoriteImageView = itemView.findViewById(R.id.ra_iv_favorite_toggle);
            detailsClickableLL = itemView.findViewById(R.id.ra_ll_details_clickable_surface);



            detailsClickableLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    clickListener.onListItemDetailsSurfaceClicked(clickedPosition);
                }
            });

            favoriteImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    clickListener.onListItemFavoriteToggleClicked(clickedPosition);
                }
            });
        }

    }
}
