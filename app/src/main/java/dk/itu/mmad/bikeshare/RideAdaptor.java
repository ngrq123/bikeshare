package dk.itu.mmad.bikeshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RideAdaptor extends RealmRecyclerViewAdapter<Ride, RideHolder> {
    // Adapted from
    // https://github.com/realm/realm-android-adapters/blob/master/example/src/main/java/io/realm/examples/adapters/ui/recyclerview/MyRecyclerViewAdapter.java

    // Logging variable
    private static final String TAG = "RideAdaptor";

    RideAdaptor(OrderedRealmCollection<Ride> data) {
        super(data,true);
        setHasStableIds(true);
    }

    @Override
    public RideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ride, parent, false);
        return new RideHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RideHolder holder, int position) {
        Ride ride = getItem(position);
        holder.bind(ride);
    }
}
