package dk.itu.mmad.bikeshare.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmRecyclerViewAdapter;

public class BikeSelectionAdaptor extends RealmBaseAdapter<Bike> {

    // Logging variable
    private static final String TAG = "BikeSelectionAdaptor";

    BikeSelectionAdaptor(OrderedRealmCollection<Bike> data) {
        super(data);
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        // https://stackoverflow.com/a/30663821
        View viewToReturn = View.inflate(parent.getContext(), R.layout.support_simple_spinner_dropdown_item, null);
        TextView bikenameView = viewToReturn.findViewById(android.R.id.text1);
        bikenameView.setText(getItem(i).getName() + " (" + getItem(i).getId() + ")");
        return viewToReturn;
    }
}
