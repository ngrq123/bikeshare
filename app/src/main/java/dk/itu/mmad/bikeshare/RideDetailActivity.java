package dk.itu.mmad.bikeshare;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RideDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ID = "dk.itu.mmad.bikeshare.id";

    private TextView mBikeName;
    private TextView mStartRide;
    private TextView mEndRide;

    private Button mDeleteRide;

    // Singleton
    private RideViewModel mRideViewModel;

    public static Intent newIntent(Context packageContext, Ride ride) {
        Intent intent = new Intent(packageContext, RideDetailActivity.class);
        intent.putExtra(EXTRA_ID, ride.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);

        // Texts
        mBikeName = (TextView) findViewById(R.id.bike_name);
        mStartRide = (TextView) findViewById(R.id.start_ride);
        mEndRide = (TextView) findViewById(R.id.end_ride);

        // Button
        mDeleteRide = (Button) findViewById(R.id.delete_button);

        // Singleton to share an object between the app activities
        mRideViewModel = ViewModelProviders.of(this).get(RideViewModel.class);

        int rideId = getIntent().getIntExtra(EXTRA_ID, -1);

        if (rideId != -1) {
            final Ride ride = mRideViewModel.getRide(rideId);

            // Set texts
            mBikeName.setText(ride.getBikeName());
            mStartRide.setText(ride.getStartRide());
            mEndRide.setText(ride.getEndRide());

            // Delete ride click event
            mDeleteRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = BikeShareActivity.newIntent(RideDetailActivity.this, ride.toString());
                    mRideViewModel.delete(ride);
                    startActivity(intent);
                }
            });
        }

    }
}
