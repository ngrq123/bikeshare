package dk.itu.mmad.bikeshare.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Ride;
import io.realm.Realm;

public class RideDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ID = "dk.itu.mmad.bikeshare.id";

    private TextView mBikeName;
    private TextView mStartRide;
    private TextView mEndRide;

    private Button mDeleteRide;

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

        final int rideId = getIntent().getIntExtra(EXTRA_ID, -1);

        if (rideId == -1) {
            Toast.makeText(this, "No ride found", Toast.LENGTH_SHORT);
        }

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    Ride ride = bgRealm.where(Ride.class)
                            .equalTo("mId", rideId)
                            .findFirst();

                    // Set texts
                    mBikeName.setText(ride.getBikeName());
                    mStartRide.setText(ride.getStartLocation());
                    mEndRide.setText(ride.getEndLocation());
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(getApplicationContext(), "No ride found", Toast.LENGTH_SHORT);
                }
            });
        }

        // Delete ride click event
        mDeleteRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            Ride ride = bgRealm.where(Ride.class)
                                    .equalTo("mId", rideId)
                                    .findFirst();

                            if (ride == null) {
                                throw new RuntimeException("No ride found");
                            }

                            Intent intent = BikeShareActivity.newIntent(RideDetailActivity.this, ride.toString());

                            // Delete photo if file exists
                            File fileDir = getApplicationContext().getFilesDir();
                            File photoFile = new File(fileDir, "ride_photo_" + ride.getId() + ".jpg");
                            if (photoFile.exists()) {
                                photoFile.delete();
                            }

                            Bike bike = bgRealm.where(Bike.class)
                                    .equalTo("mId", ride.getBikeId())
                                    .findFirst();

                            bike.setInUse(false);
                            bgRealm.insertOrUpdate(bike);
                            ride.deleteFromRealm();

                            startActivity(intent);
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
