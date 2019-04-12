package dk.itu.mmad.bikeshare.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Ride;
import dk.itu.mmad.bikeshare.util.PictureUtils;

public class BikeHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "BikeHolder";

    private TextView mBikeIdView;
    private TextView mBikeNameView;
    private TextView mBikeTypeView;
    private TextView mBikePriceView;

    private ImageView mBikePhoto;

    public BikeHolder(View itemView) {
        super(itemView);

        mBikeIdView = itemView.findViewById(R.id.bike_id);
        mBikeNameView = itemView.findViewById(R.id.bike_name);
        mBikeTypeView = itemView.findViewById(R.id.bike_type);
        mBikePriceView = itemView.findViewById(R.id.bike_price);
        mBikePhoto = itemView.findViewById(R.id.bike_photo);
    }

    public void bind(Bike bike) {
        mBikeIdView.setText(bike.getId());
        mBikeNameView.setText(bike.getName() + " (" + (bike.isInUse() ? "In Use" : "Free") + ")");
        mBikeTypeView.setText(bike.getType());
        mBikePriceView.setText(bike.getPricePerMin() + " kr/min");

        Bitmap bitmap = bike.getPicture();

        if (bitmap != null) {
            mBikePhoto.setImageBitmap(bitmap);
        }
    }
}
