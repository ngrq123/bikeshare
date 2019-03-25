package dk.itu.mmad.bikeshare;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

    // Singleton, adaptor and list view variables
    private RideViewModel mRideViewModel;
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

        // Singleton to share an object between the app activities
        mRideViewModel = ViewModelProviders.of(this).get(RideViewModel.class);

        mRideViewModel.getAllRides().observe(this, new Observer<List<Ride>>() {
            @Override
            public void onChanged(@Nullable List<Ride> rides) {
                mAdaptor.setRides(rides);
            }
        });

        // Create the adaptor
        mAdaptor = new RideAdaptor(getContext());

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

        // Create list view with divider
        mDivider = (View) v.findViewById(R.id.divider);
        mDivider.setVisibility(LinearLayout.GONE);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mListRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mRecyclerView.getAdapter() == null) {
                    mDivider.setVisibility(LinearLayout.VISIBLE);
                    mRecyclerView.setAdapter(mAdaptor);
                } else {
                    mDivider.setVisibility(LinearLayout.GONE);
                    mRecyclerView.setAdapter(null);
                }
            }
        });

//        mRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
//            // Adapted from https://stackoverflow.com/a/26196831
//
//            GestureDetector mGestureDetector = new GestureDetector(getContext(),
//                    new GestureDetector.SimpleOnGestureListener() {
//                        @Override
//                        public boolean onSingleTapUp(MotionEvent e) {
//                            return true;
//                        }
//                    });
//
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                rv.setOnLongClickListener(new RecyclerView.OnLongClickListener() {
//
//                    @Override
//                    public boolean onLongClick(View view) {
//                        Log.d(TAG, "Clicked");
//                        return true;
//                    }
//                });
//                boolean isPressed = false;
//                long startTime = System.currentTimeMillis();
//
//                if (e.getAction() == MotionEvent.ACTION_DOWN) {
//                    isPressed = true;
//                    startTime = System.currentTimeMillis();
//                    Log.d(TAG, "Start Time: " + startTime);
//                }
//
//                if (e.getAction() == MotionEvent.ACTION_UP) {
//                    isPressed = false;
//                    long duration = System.currentTimeMillis() - startTime;
//                    Log.d(TAG, "Duration " + duration);
//                }
//                View childView = rv.findChildViewUnder(e.getX(), e.getY());
//                if (childView != null && mGestureDetector.onTouchEvent(e)) {
//                    int position = rv.getChildAdapterPosition(childView);
//                    Intent intent = RideDetailActivity.newIntent(getContext(),
//                            mRideViewModel.getAllRides().getValue().get(position));
//                    startActivity(intent);
//                    return true;
//                }
//                return false;
//            }
//        });

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
