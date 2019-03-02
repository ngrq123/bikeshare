package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RideArrayAdaptor extends ArrayAdapter<Ride> {

    private final Context mContext;
    private final List<Ride> mValues;

    public RideArrayAdaptor(Context context, List<Ride> values) {
        super(context, R.layout.list_item_ride, values);
        mContext = context;
        mValues = values;
    }

    @Override
    public View getView(final int position, View concertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_ride, parent, false);

        Ride ride = mValues.get(position);

        TextView bikeView = (TextView) rowView.findViewById(R.id.what_bike_ride);
        bikeView.append(ride.getBikeName());

        TextView startView = (TextView) rowView.findViewById(R.id.start_ride);
        startView.append(ride.getStartRide());

        TextView endView = (TextView) rowView.findViewById(R.id.end_ride);
        endView.append(ride.getEndRide());

        return rowView;
    }

}
