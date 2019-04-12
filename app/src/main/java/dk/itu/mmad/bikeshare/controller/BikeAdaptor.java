package dk.itu.mmad.bikeshare.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Ride;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class BikeAdaptor extends RealmRecyclerViewAdapter<Bike, BikeHolder> {
    // Adapted from
    // https://github.com/realm/realm-android-adapters/blob/master/example/src/main/java/io/realm/examples/adapters/ui/recyclerview/MyRecyclerViewAdapter.java

    // Logging variable
    private static final String TAG = "BikeAdaptor";

    BikeAdaptor(OrderedRealmCollection<Bike> data) {
        super(data,true);
        setHasStableIds(true);
    }

    @Override
    public BikeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_bike, parent, false);
        return new BikeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BikeHolder holder, int position) {
        Bike bike = getItem(position);
        holder.bind(bike);
    }
}
