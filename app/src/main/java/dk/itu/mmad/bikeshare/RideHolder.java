package dk.itu.mmad.bikeshare;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class RideHolder extends RecyclerView.ViewHolder {

    private TextView mBikeNameView;
    private TextView mStartRideView;
    private TextView mEndRideView;

    public RideHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        super(layoutInflater.inflate(R.layout.list_item_ride, parent, false));
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
