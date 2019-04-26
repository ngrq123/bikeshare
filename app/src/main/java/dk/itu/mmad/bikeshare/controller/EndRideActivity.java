package dk.itu.mmad.bikeshare.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Ride;
import dk.itu.mmad.bikeshare.model.Transaction;
import dk.itu.mmad.bikeshare.model.User;
import io.realm.Realm;
import io.realm.Sort;

public class EndRideActivity extends AppCompatActivity {
    private static final String TAG = "EndRideActivity";

    // GUI variables
    private Button mEndRide;
    private TextView mLastEnded;
    private Spinner mNewWhatSpinner;
    private TextView mNewWhere;

    private String mSelectedBikeId;
    private String mLastEndedStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_ride);

        mLastEnded = (TextView) findViewById(R.id.last_ride);

        // Button
        mEndRide = (Button) findViewById(R.id.end_button);

        mNewWhatSpinner = (Spinner) findViewById(R.id.what_text_spinner);

        try (Realm realm = Realm.getDefaultInstance()) {
            RideSelectionAdaptor mAdaptor = new RideSelectionAdaptor(realm.where(Ride.class)
                    .isNull("mEndLocation")
                    .and()
                    .equalTo("mUserEmail", PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .getString("user", null))
                    .findAllAsync());
            mNewWhatSpinner.setAdapter(mAdaptor);

            mNewWhatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // https://www.mkyong.com/android/android-spinner-drop-down-list-example/
                    mSelectedBikeId = ((Ride) adapterView.getItemAtPosition(i)).getBike().getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
        }

        mNewWhere = (TextView) findViewById(R.id.where_text);


        // End ride click event
        mEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mNewWhere.getText().length() > 0) {
                    final String endRide = mNewWhere.getText().toString().trim();
                    final Date endDate = Calendar.getInstance().getTime();

                    try (Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm bgRealm) {
                                // Find bike in database
                                Bike bike = bgRealm.where(Bike.class)
                                        .equalTo("mId", mSelectedBikeId)
                                        .findFirst();

                                if (bike == null) {
                                    throw new RuntimeException("Bike is not found.\nPlease check again.");
                                }

                                // Check for ride
                                Ride ride = bgRealm.where(Ride.class)
                                        .equalTo("mBikeId", bike.getId())
                                        .sort("mId", Sort.DESCENDING)
                                        .findFirst();

                                if (ride == null || ride.getEndLocation() != null) {
                                    // No ride found
                                    throw new RuntimeException("No ride found.\nPlease check again.");
                                }

                                ride.setEndLocation(endRide);
                                ride.setEndDate(endDate);
                                ride.getBike().setInUse(false);
                                bgRealm.insertOrUpdate(ride);

                                // Add transaction and deduct balance
                                User user = ride.getUser();
                                user.deductBalance(ride.getAmount());

                                bgRealm.insert(new Transaction(user, ride.getEndDate(), ride.getAmount(), ride.getBike().getName()));
                                bgRealm.insertOrUpdate(user);

                                mLastEndedStr = ride.getBike().getName() + " ended at " +
                                        ride.getEndLocation() + " on " + ride.getEndDate();
                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                updateUI();
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                showDialogBox(view, error.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    private void updateUI() {
        // Reset text fields
        mNewWhere.setText("");

        if (mLastEndedStr != null && !mLastEndedStr.isEmpty()) {
            mLastEnded.setText(mLastEndedStr);
        }
    }

    private void showDialogBox(View view, String message) {
        new AlertDialog.Builder(view.getContext())
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }
}
