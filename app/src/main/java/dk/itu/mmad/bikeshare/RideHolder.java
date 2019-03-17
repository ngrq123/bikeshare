package dk.itu.mmad.bikeshare;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RideHolder extends RecyclerView.ViewHolder {

    private TextView mBikeNameView;
    private TextView mStartRideView;
    private TextView mEndRideView;

    public RideHolder(View itemView) {
        super(itemView);
        mBikeNameView = itemView.findViewById(R.id.what_bike_ride);
        mStartRideView = itemView.findViewById(R.id.start_ride);
        mEndRideView = itemView.findViewById(R.id.end_ride);
    }

    public void bind(Ride ride) {
        mBikeNameView.setText(ride.getBikeName());
        mStartRideView.setText(ride.getStartRide());
        mEndRideView.setText(ride.getEndRide());
    }
}
