package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RideAdaptor extends RecyclerView.Adapter<RideHolder> {

    private final List<Ride> mRides;

    public RideAdaptor(List<Ride> rides) {
        mRides = rides;
    }

    @NonNull
    @Override
    public RideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new RideHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RideHolder holder, final int position) {
        Ride ride = mRides.get(position);
        holder.bind(ride);
    }

    @Override
    public int getItemCount() {
        return mRides.size();
    }
}
