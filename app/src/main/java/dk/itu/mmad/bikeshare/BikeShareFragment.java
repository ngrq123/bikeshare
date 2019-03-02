package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class BikeShareFragment extends Fragment {
    // Logger variable
    private static final String TAG = "BikeShareFragment";

    // Intent variable
    private static String EXTRA_POSITION = "dk.itu.mmad.bikeshare.EXTRA_POSITION";

    // GUI variables
    private Button mAddRide;
    private Button mEndRide;
    private Button mListRides;
    private TextView mBuildVersion;

    // Singleton, adaptor and list view variables
    private static RidesDB sRidesDB;
    private RideArrayAdaptor mAdaptor;
    private ListView mListView;

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

        // Singleton to share an object between the app activities
        sRidesDB = RidesDB.get(getContext());
        final List<Ride> values = sRidesDB.getRidesDB();

        // Create the adaptor
        mAdaptor = new RideArrayAdaptor(getContext(), values);
        mListView = (ListView) v.findViewById(R.id.main_list_view);

        mListRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mListView.getAdapter() == null) {
                    mListView.setAdapter(mAdaptor);

                    mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = RideDetailActivity.newIntent(getContext(), values.get(i), i);
                            startActivity(intent);
                        }
                    });
                } else {
                    mListView.setAdapter(null);
                }
            }
        });

        Intent intent = getActivity().getIntent();

        Log.d(TAG, getActivity().toString());

        if (intent != null) {
            int position = intent.getIntExtra(EXTRA_POSITION, -1);
            if (position != -1) {

                // Get deleted ride for toast and delete ride
                Ride mDeletedRide = values.get(position);
                sRidesDB.deleteRide(position);

                // Set toast message
                String toastMessage = "The " + mDeletedRide.getBikeName() +
                        " ride that started from " + mDeletedRide.getStartRide();

                if (mDeletedRide.getEndRide().equals("")) {
                    toastMessage +=  " has been deleted";
                } else {
                    toastMessage += " to " + mDeletedRide.getEndRide() + " has been deleted";
                }

                Toast toast = Toast.makeText(getContext(), toastMessage, Toast.LENGTH_LONG);
                toast.show();
//                mAdaptor.notifyDataSetChanged();
            }
        }

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
