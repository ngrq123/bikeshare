package dk.itu.mmad.bikeshare;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class BikeShareFragment extends Fragment {
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
                            Intent intent = RideDetailActivity.newIntent(getContext(), values.get(i));
                            startActivity(intent);
                        }
                    });
                } else {
                    mListView.setAdapter(null);
                }
            }
        });

        // Show build version
        mBuildVersion = (TextView) v.findViewById(R.id.build_version);
        mBuildVersion.setText("API level " + Build.VERSION.SDK_INT);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
