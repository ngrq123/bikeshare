package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class BikeShareFragment extends Fragment {
    // Logging variable
    private static final String TAG = "BikeShareFragment";

    // Intent variable
    private static String EXTRA_RIDE_DETAIL = "dk.itu.mmad.bikeshare.EXTRA_RIDE_DETAIL";

    // GUI variables
    private Button mAddRide;
    private Button mEndRide;
    private Button mListRides;
    private TextView mBuildVersion;

    // Database, adaptor and list view variables
    private Realm mRealm;
    private RideAdaptor mAdaptor;
    private View mDivider;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedBundleState) {
        View v = inflater.inflate(R.layout.fragment_bike_share, container, false);
        // Buttons
        mAddRide = (Button) v.findViewById(R.id.add_button);
        mEndRide = (Button) v.findViewById(R.id.end_button);
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

        mRealm.beginTransaction();
        mAdaptor = new RideAdaptor(mRealm.where(Ride.class).findAllAsync());
        mRealm.commitTransaction();

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
