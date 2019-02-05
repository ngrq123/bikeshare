package com.example.bikeshare;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BikeShareActivity extends AppCompatActivity {

    // GUI variables
    private Button mAddRide;
    private TextView mLastAdded;
    private TextView mNewWhat;
    private TextView mNewWhere;
    private TextView mBuildVersion;

    private Ride mLast = new Ride("", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);

        mLastAdded = (TextView) findViewById(R.id.last_ride);
        updateUI();

        // Button
        mAddRide = (Button) findViewById(R.id.add_button);

        // Texts
        mNewWhat = (TextView) findViewById(R.id.what_text);
        mNewWhere = (TextView) findViewById(R.id.where_text);

        // View products click event
        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNewWhat.getText().length() > 0 && mNewWhere.getText().length() > 0) {
                    mLast.setBikeName(mNewWhat.getText().toString().trim());
                    mLast.setStartRide(mNewWhere.getText().toString().trim());

                    // Reset text fields
                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    updateUI();
                }
            }
        });

        mBuildVersion = (TextView) findViewById(R.id.build_version);
        mBuildVersion.setText("API level " + Build.VERSION.SDK_INT);

    }

    private void updateUI() {
        if (mLast.getBikeName().isEmpty() && mLast.getStartRide().isEmpty()) {
            mLastAdded.setText("");
        } else {
            mLastAdded.setText(mLast.toString());
        }
    }
}
