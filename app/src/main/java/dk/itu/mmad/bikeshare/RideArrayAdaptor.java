package dk.itu.mmad.bikeshare;

import android.content.Context;
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
    public View getView(int position, View concertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_ride, parent, false);
        TextView bikeView = (TextView) rowView.findViewById(R.id.what_bike_ride);
        bikeView.append(mValues.get(position).getBikeName());
        TextView startView = (TextView) rowView.findViewById(R.id.start_ride);
        bikeView.append(mValues.get(position).getStartRide());
        TextView endView = (TextView) rowView.findViewById(R.id.end_ride);
        bikeView.append(mValues.get(position).getEndRide());
        return rowView;
    }

}
