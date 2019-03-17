package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RideAdaptor extends RecyclerView.Adapter<RideHolder> {
    // Logging variable
    private static final String TAG = "RideAdaptor";

    private final LayoutInflater mInflater;
    private List<Ride> mAllRides;

    public RideAdaptor(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_ride, parent, false);
        return new RideHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RideHolder holder, int position) {
        if (mAllRides != null) {
            Ride ride = mAllRides.get(position);
            holder.bind(ride);
        }
    }

    public void setRides(List<Ride> rides){
        mAllRides = rides;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mAllRides != null) {
            return mAllRides.size();
        }
        return 0;
    }
}
