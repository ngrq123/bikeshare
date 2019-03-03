package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RideDetailActivity extends AppCompatActivity {

    private static final String EXTRA_BIKE_NAME = "dk.itu.mmad.bikeshare.bike_name";
    private static final String EXTRA_START_RIDE = "dk.itu.mmad.bikeshare.start_ride";
    private static final String EXTRA_END_RIDE = "dk.itu.mmad.bikeshare.end_ride";
    private static final String EXTRA_POSITION = "dk.itu.mmad.bikeshare.position";

    private TextView mBikeName;
    private TextView mStartRide;
    private TextView mEndRide;

    private Button mDeleteRide;

    public static Intent newIntent(Context packageContext, Ride ride, int position) {
        Intent intent = new Intent(packageContext, RideDetailActivity.class);
        intent.putExtra(EXTRA_BIKE_NAME, ride.getBikeName());
        intent.putExtra(EXTRA_START_RIDE, ride.getStartRide());
        intent.putExtra(EXTRA_END_RIDE, ride.getEndRide());
        intent.putExtra(EXTRA_POSITION, position);
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

        // Set texts
        mBikeName.setText(getIntent().getStringExtra(EXTRA_BIKE_NAME));
        mStartRide.setText(getIntent().getStringExtra(EXTRA_START_RIDE));
        mEndRide.setText(getIntent().getStringExtra(EXTRA_END_RIDE));

        // Delete ride click event
        mDeleteRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getIntent().getIntExtra(EXTRA_POSITION, -1);
                Intent intent = BikeShareActivity.newIntent(RideDetailActivity.this, position);
                startActivity(intent);
            }
        });

    }
}