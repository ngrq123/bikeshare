package dk.itu.mmad.bikeshare.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Ride;
import dk.itu.mmad.bikeshare.util.PictureUtils;

public class RideHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    private static final String TAG = "RideHolder";

    private TextView mBikeNameView;
    private TextView mStartRideView;
    private TextView mEndRideView;

    private ImageView mBikePhoto;

    private File mFileDir;
    private File mPhotoFile;

    private Ride mRide;

    private boolean isTouched = false;
    private long startTime = System.currentTimeMillis();

    public RideHolder(View itemView) {
        super(itemView);

        itemView.setOnTouchListener(this);

        mBikeNameView = itemView.findViewById(R.id.what_bike_ride);
        mStartRideView = itemView.findViewById(R.id.start_ride);
        mEndRideView = itemView.findViewById(R.id.end_ride);
        mBikePhoto = itemView.findViewById(R.id.bike_photo);

        mFileDir = itemView.getContext().getFilesDir();
    }

    public void bind(Ride ride) {
        mRide = ride;

        mBikeNameView.setText(ride.getBikeName());

        String startRideText = "Start: " + ride.getStartLocation();
        if (ride.isStartLocationKnown()) {
            startRideText += "\n(" + ride.getStartLongitude() + ", " + ride.getStartLatitude() + ")";
        }
        mStartRideView.setText(startRideText);

        if (ride.getEndLocation() != null && !ride.getEndLocation().isEmpty()) {
            String endRideText = "End: " + ride.getEndLocation();
            if (ride.isEndLocationKnown()) {
                endRideText += "\n(" + ride.getEndLongitude() + ", " + ride.getEndLatitude() + ")";
            }
            mEndRideView.setText(endRideText);
        }

        Bitmap bitmap = ride.getPicture();

        if (bitmap != null) {
            mBikePhoto.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        // Adapted from https://stackoverflow.com/a/17896654

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !isTouched) {
            isTouched = true;
            startTime = System.currentTimeMillis();
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP && isTouched && mRide != null) {
            isTouched = false;
            long duration = System.currentTimeMillis() - startTime;
            if (duration >= 2000) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Ride")
                        .setMessage("Do you want to delete this ride?")
                        .setCancelable(true)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = RideDetailActivity.newIntent(view.getContext(), mRide);
                                view.getContext().startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        }

        return true;
    }
}
