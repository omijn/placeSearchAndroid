package com.example.omijn.placeandentertainmentsearch;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoHolder> {

    private ArrayList<Bitmap> data;

    public PhotosAdapter(ArrayList<Bitmap> list) {
        this.data = list;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);
        PhotoHolder holder = new PhotoHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        holder.photo.setImageBitmap(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        public PhotoHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.da_pf_rv_iv_photo);
        }

    }
}
