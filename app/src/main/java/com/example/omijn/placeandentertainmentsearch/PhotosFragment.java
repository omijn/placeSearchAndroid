package com.example.omijn.placeandentertainmentsearch;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {

    private String placeId;
    protected GeoDataClient mGeoDataClient;
    private ArrayList<Bitmap> photosData;
    private PhotosAdapter adapter;

    private RecyclerView recyclerView;
    private TextView emptyView;

    public PhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photos, container, false);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(v.getContext());

        photosData = new ArrayList<>();
        
        placeId = getArguments().getString("place_id");
        getPhotos();

        // get reference to recyclerview
        recyclerView = v.findViewById(R.id.da_pf_rv_photos);
        emptyView = v.findViewById(R.id.da_pf_tv_no_photos);

        // create new adapter
        adapter = new PhotosAdapter(photosData);

        // set adapter to recyclerview
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (photosData.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        return v;
    }

    private void getPhotos() {

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                Log.d("PhotosFragment getPhotos", "sizeof buffer = " + Integer.toString(photoMetadataBuffer.getCount()));

                for (int i = 0; i < photoMetadataBuffer.getCount(); i++) {
                    // Get ith photo in the list.
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);

                    // Get a full-size bitmap for the photo.
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            photosData.add(bitmap);
                            adapter.notifyDataSetChanged();

                            if (photosData.isEmpty()) {
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

}
