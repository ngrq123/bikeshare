package dk.itu.mmad.bikeshare;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class EndRideActivity extends AppCompatActivity {

    // GUI variables
    private Button mEndRide;
    private TextView mLastEnded;
    private TextView mNewWhat;
    private TextView mNewWhere;

    // Singleton
    private RideViewModel mRideViewModel;

    // Last ride information
    private Ride mLast = new Ride("", "", null, "", null);

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

        // Singleton to share an object between the app activities
        mRideViewModel = ViewModelProviders.of(this).get(RideViewModel.class);

        // End ride click event
        mEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNewWhat.getText().length() > 0 && mNewWhere.getText().length() > 0) {
                    String bikeName = mNewWhat.getText().toString().trim();
                    String endRide = mNewWhere.getText().toString().trim();
                    Date endDate = Calendar.getInstance().getTime();

                    mLast.setBikeName(bikeName);
                    mLast.setEndRide(endRide);
                    mLast.setEndDate(endDate);

                    Ride ride = mRideViewModel.getLatestRide(bikeName);

                    if (ride != null && ride.getEndRide().isEmpty()) {
                        mRideViewModel.update(ride.getId(), endRide, endDate);
                    } else {
                        mLastEnded.setText("Ride has not started");
                    }

                    // Reset text fields
                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    updateUI();
                }
            }
        });
    }

    private void updateUI() {
        if (mLast.getBikeName().isEmpty() && mLast.getStartRide().isEmpty()) {
            mLastEnded.setText("");
        } else {
            mLastEnded.setText(mLast.toString());
        }
    }
}
