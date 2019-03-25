package dk.itu.mmad.bikeshare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class RideHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    private TextView mBikeNameView;
    private TextView mStartRideView;
    private TextView mEndRideView;

    private Ride mRide;

    private boolean isTouched = false;
    private long startTime = System.currentTimeMillis();

    public RideHolder(View itemView) {
        super(itemView);

        itemView.setOnTouchListener(this);

        mBikeNameView = itemView.findViewById(R.id.what_bike_ride);
        mStartRideView = itemView.findViewById(R.id.start_ride);
        mEndRideView = itemView.findViewById(R.id.end_ride);
    }

    public void bind(Ride ride) {
        mRide = ride;

        mBikeNameView.setText(ride.getBikeName());
        mStartRideView.setText(ride.getStartRide());
        mEndRideView.setText(ride.getEndRide());
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
