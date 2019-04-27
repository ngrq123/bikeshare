package dk.itu.mmad.bikeshare.controller;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;

public class BikeHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "BikeHolder";

    private TextView mBikeIdView;
    private TextView mBikeNameView;
    private TextView mBikeTypeView;
    private TextView mBikePriceView;

    private ImageView mBikePhoto;

    public BikeHolder(View itemView) {
        super(itemView);

        mBikeIdView = itemView.findViewById(R.id.bike_id);
        mBikeNameView = itemView.findViewById(R.id.bike_name);
        mBikeTypeView = itemView.findViewById(R.id.bike_type);
        mBikePriceView = itemView.findViewById(R.id.bike_price);
        mBikePhoto = itemView.findViewById(R.id.bike_photo);
    }

    public void bind(Bike bike) {
        mBikeIdView.setText(bike.getId());
        String bikeName = bike.getName();
        if (bike.isInUse()) {
            bikeName += " (In Use)";
        } else {
            bikeName += " (Free at " + bike.getLocation() + ")";
            if (bike.isLocationKnown()) {
                bikeName += "\n(" + bike.getLongitude() + ", " + bike.getLatitude() + ")";
            }
        }
        mBikeNameView.setText(bikeName);
        mBikeTypeView.setText(bike.getType());
        String bikePrice = bike.getPricePerHr() + " kr/hr";
        mBikePriceView.setText(bikePrice);

        Bitmap bitmap = bike.getPicture();

        if (bitmap != null) {
            mBikePhoto.setImageBitmap(bitmap);
        }
    }
}
