package dk.itu.mmad.bikeshare;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StartRideActivity extends AppCompatActivity {

    // GUI variables
    private Button mAddRide;
    private TextView mLastAdded;
    private TextView mNewWhat;
    private TextView mNewWhere;

    // Database
    private RideDB mRideDB;

    // Last ride information
    private Ride mLast = new Ride("", "", null, "", null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);

        mLastAdded = (TextView) findViewById(R.id.last_ride);
        updateUI();

        // Button
        mAddRide = (Button) findViewById(R.id.add_button);

        // Texts
        mNewWhat = (TextView) findViewById(R.id.what_text);
        mNewWhere = (TextView) findViewById(R.id.where_text);

        // Database
        mRideDB = new RideDB();

        // Add ride click event
        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNewWhat.getText().length() > 0 && mNewWhere.getText().length() > 0) {
                    String bikeName = mNewWhat.getText().toString().trim();
                    String startRide = mNewWhere.getText().toString().trim();
                    Date startDate = Calendar.getInstance().getTime();

                    mLast.setBikeName(bikeName);
                    mLast.setStartRide(startRide);
                    mLast.setStartDate(startDate);

                    mRideDB.insert(mLast);

                    // Reset text fields
                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    updateUI();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRideDB != null) {
            mRideDB.close();
        }
    }

    private void updateUI() {
        if (mLast.getBikeName().isEmpty() && mLast.getStartRide().isEmpty()) {
            mLastAdded.setText("");
        } else {
            mLastAdded.setText(mLast.toString());
        }
    }
}
