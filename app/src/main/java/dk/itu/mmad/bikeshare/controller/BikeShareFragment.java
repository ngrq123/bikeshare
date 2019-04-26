package dk.itu.mmad.bikeshare.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Ride;
import io.realm.Realm;

public class BikeShareFragment extends Fragment {
    // Logging variable
    private static final String TAG = "BikeShareFragment";

    // Intent variable
    private static String EXTRA_RIDE_DETAIL = "dk.itu.mmad.bikeshare.EXTRA_RIDE_DETAIL";

    // GUI variables
    private TextView mEmail;
    private Button mTopUp;
    private Button mListTransactions;
    private Button mRegisterBike;
    private Button mListBikes;
    private Button mAddRide;
    private Button mEndRide;
    private Button mListRides;
    private Button mLogout;
    private TextView mBuildVersion;

    // Shared preferences, database, adaptor and list view variables
    private SharedPreferences mSharedPreferences;
    private Realm mRealm;
    private RideAdaptor mAdaptor;
    private View mDivider;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedBundleState) {
        View v = inflater.inflate(R.layout.fragment_bike_share, container, false);

        mEmail = (TextView) v.findViewById(R.id.email_text);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String emailText = "Welcome\n" + mSharedPreferences.getString("user", null);
        mEmail.setText(emailText);

        // Buttons
        mTopUp = (Button) v.findViewById(R.id.top_up_button);
        mListTransactions = (Button) v.findViewById(R.id.list_transactions_button);
        mRegisterBike = (Button) v.findViewById(R.id.register_bike_button);
        mListBikes = (Button) v.findViewById(R.id.list_bikes_button);
        mAddRide = (Button) v.findViewById(R.id.add_ride_button);
        mEndRide = (Button) v.findViewById(R.id.end_ride_button);
        mListRides = (Button) v.findViewById(R.id.list_rides_button);
        mLogout = (Button) v.findViewById(R.id.logout_button);

        // Click events
        mTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TopUpActivity.class);
                startActivity(intent);
            }
        });

        mListTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListTransactionsActivity.class);
                startActivity(intent);
            }
        });

        mRegisterBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RegisterBikeActivity.class);
                startActivity(intent);
            }
        });

        mListBikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListBikesActivity.class);
                startActivity(intent);
            }
        });

        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StartRideActivity.class);
                startActivity(intent);
            }
        });

        mEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EndRideActivity.class);
                startActivity(intent);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreferences.edit().remove("user").apply();
                Intent intent = new Intent(getContext(), BikeShareActivity.class);
                startActivity(intent);
            }
        });

        // Show build version
        mBuildVersion = (TextView) v.findViewById(R.id.build_version);
        mBuildVersion.setText("API level " + Build.VERSION.SDK_INT);

        // Database
        mRealm = Realm.getDefaultInstance();

        // Create list view with divider
        mDivider = (View) v.findViewById(R.id.divider);
        mDivider.setVisibility(LinearLayout.GONE);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setVisibility(RecyclerView.GONE);

        mAdaptor = new RideAdaptor(mRealm.where(Ride.class)
                .equalTo("mUserEmail", mSharedPreferences.getString("user", null))
                .findAllAsync());

        mRecyclerView.setAdapter(mAdaptor);

        mListRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecyclerView.getVisibility() == RecyclerView.GONE) {
                    mDivider.setVisibility(LinearLayout.VISIBLE);
                    mRecyclerView.setVisibility(RecyclerView.VISIBLE);
                } else {
                    mDivider.setVisibility(LinearLayout.GONE);
                    mRecyclerView.setVisibility(RecyclerView.GONE);
                }
            }
        });

        Intent intent = getActivity().getIntent();

        if (intent != null) {
            String rideDetail = intent.getStringExtra(EXTRA_RIDE_DETAIL);
            if (rideDetail != null) {
                // Set toast message
                String toastMessage = rideDetail + " has been deleted";

                Toast toast = Toast.makeText(getContext(), toastMessage, Toast.LENGTH_LONG);
                toast.show();
            }
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdaptor != null) {
            mAdaptor.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
