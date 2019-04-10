package dk.itu.mmad.bikeshare.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Ride;
import io.realm.Realm;
import io.realm.Sort;

public class EndRideActivity extends AppCompatActivity {
    private static final String TAG = "EndRideActivity";

    // GUI variables
    private Button mEndRide;
    private TextView mLastEnded;
    private TextView mNewWhat;
    private TextView mNewWhere;

    // Database
    private Realm mRealm;

    // Last ride information
    private Ride mLast = new Ride(-1, "", null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);

        mLastEnded = (TextView) findViewById(R.id.last_ride);
        updateUI();

        // Button
        mEndRide = (Button) findViewById(R.id.end_button);

        // Texts
        mNewWhat = (TextView) findViewById(R.id.what_text);
        mNewWhere = (TextView) findViewById(R.id.where_text);

        // Database
        mRealm = Realm.getDefaultInstance();

        // End ride click event
        mEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNewWhat.getText().length() > 0 && mNewWhere.getText().length() > 0) {
                    final String bikeName = mNewWhat.getText().toString().trim();
                    final String endRide = mNewWhere.getText().toString().trim();
                    final Date endDate = Calendar.getInstance().getTime();

                    mRealm.beginTransaction();
                    Bike bike = mRealm.where(Bike.class)
                            .equalTo("mName", bikeName)
                            .findFirst();

                    if (bike == null) {
                        mLastEnded.setText("Bike not found");
                    } else {
                        Ride ride = mRealm.where(Ride.class)
                                .equalTo("mBikeId", bike.getId())
                                .sort("mId", Sort.DESCENDING)
                                .findFirst();

                        if (ride != null && ride.getEndLocation() == null) {
                            mLast = ride;
                            mLast.setEndLocation(endRide);
                            mLast.setEndDate(endDate);
                            mRealm.insertOrUpdate(mLast);
                            updateUI();
                        } else {
                            mLastEnded.setText("Ride has not started");
                        }
                        mRealm.commitTransaction();

                        // Reset text fields
                        mNewWhat.setText("");
                        mNewWhere.setText("");
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }

    private void updateUI() {
        if (mLast.getId() == -1) {
            mLastEnded.setText("");
        } else {
            String rideStr = mLast.getBike().getName() + " ended at " +
                    mLast.getEndLocation() + " on " + mLast.getEndDate();
            mLastEnded.setText(rideStr);
        }
    }
}
