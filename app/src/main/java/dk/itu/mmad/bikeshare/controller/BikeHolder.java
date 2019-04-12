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

    private File mFileDir;
    private File mPhotoFile;

    public BikeHolder(View itemView) {
        super(itemView);

        mBikeIdView = itemView.findViewById(R.id.bike_id);
        mBikeNameView = itemView.findViewById(R.id.bike_name);
        mBikeTypeView = itemView.findViewById(R.id.bike_type);
        mBikePriceView = itemView.findViewById(R.id.bike_price);
        mBikePhoto = itemView.findViewById(R.id.bike_photo);

        mFileDir = itemView.getContext().getFilesDir();
    }

    public void bind(Bike bike) {
        mBikeIdView.setText(bike.getId());
        mBikeNameView.setText(bike.getName() + " (" + (bike.isInUse() ? "In Use" : "Free") + ")");
        mBikeTypeView.setText(bike.getType());
        mBikePriceView.setText(bike.getPricePerMin() + " kr/min");

        // Show photo if file exists
        mPhotoFile = new File(mFileDir, "bike_photo_" + bike.getId()  + ".jpg");
        if (mPhotoFile.exists()) {
            showPhoto();
        }
    }

    // Adapted from textbook listing 16.11 (page 315)
    private void showPhoto() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mBikePhoto.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), (Activity) itemView.getContext());
            mBikePhoto.setImageBitmap(bitmap);
        }
    }
}
