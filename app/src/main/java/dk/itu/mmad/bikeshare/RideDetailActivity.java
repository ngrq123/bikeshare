package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import io.realm.Realm;
import io.realm.Sort;

public class RideDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ID = "dk.itu.mmad.bikeshare.id";

    private TextView mBikeName;
    private TextView mStartRide;
    private TextView mEndRide;

    private Button mDeleteRide;

    // Database
    private Realm mRealm;

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

        // Database
        mRealm = Realm.getDefaultInstance();

        int rideId = getIntent().getIntExtra(EXTRA_ID, -1);

        if (rideId != -1) {
            mRealm.beginTransaction();
            final Ride ride = mRealm.where(Ride.class)
                    .equalTo("id", rideId)
                    .findFirst();
            mRealm.commitTransaction();

            // Set texts
            mBikeName.setText(ride.getBikeName());
            mStartRide.setText(ride.getStartRide());
            mEndRide.setText(ride.getEndRide());

            final File fileDir = this.getFilesDir();

            // Delete ride click event
            mDeleteRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = BikeShareActivity.newIntent(RideDetailActivity.this, ride.toString());

                    // Delete photo if file exists
                    File photoFile = new File(fileDir, "bike_photo_" + ride.getId()  + ".jpg");
                    if (photoFile.exists()) {
                        photoFile.delete();
                    }

                    mRealm.beginTransaction();
                    ride.deleteFromRealm();
                    mRealm.commitTransaction();

                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
