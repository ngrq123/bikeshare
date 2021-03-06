package dk.itu.mmad.bikeshare.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private Button mAddRide;
    private Button mEndRide;
    private Button mListRides;
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
        mAddRide = (Button) v.findViewById(R.id.add_ride_button);
        mEndRide = (Button) v.findViewById(R.id.end_ride_button);
        mListRides = (Button) v.findViewById(R.id.list_rides_button);

        // Click events
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Class<?> clazz;

        switch(item.getItemId()) {
            case R.id.top_up:
                clazz = TopUpActivity.class;
                break;
            case R.id.list_transactions:
                clazz = ListTransactionsActivity.class;
                break;
            case R.id.register_bike:
                clazz = RegisterBikeActivity.class;
                break;
            case R.id.list_bikes:
                clazz = ListBikesActivity.class;
                break;
            case R.id.logout:
                mSharedPreferences.edit().remove("user").apply();
                clazz = BikeShareActivity.class;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        Intent intent = new Intent(getContext(), clazz);
        startActivity(intent);
        return true;
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
        setHasOptionsMenu(true);
    }
}
