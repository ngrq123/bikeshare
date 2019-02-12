package dk.itu.mmad.bikeshare;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import dk.itu.mmad.bikeshare.R;

public class BikeShareActivity extends AppCompatActivity {

    // GUI variables
    private Button mAddRide;
    private Button mEndRide;

    private Ride mLast = new Ride("", "", "");

    private static RidesDB sRidesDB;
    private RideArrayAdaptor mAdaptor;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);

        // Button
        mAddRide = (Button) findViewById(R.id.add_button);
        mEndRide = (Button) findViewById(R.id.end_button);

        // Click events
        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BikeShareActivity.this, StartRideActivity.class);
                startActivity(intent);
            }
        });

        mEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BikeShareActivity.this, EndRideActivity.class);
                startActivity(intent);
            }
        });

        // Singleton to share an object between the app activities
        sRidesDB = RidesDB.get(this);
        List<Ride> values = sRidesDB.getRidesDB();

        // Create the adaptor
        mAdaptor = new RideArrayAdaptor(this, values);
        mListView = (ListView) findViewById(R.id.main_list_view);
        mListView.setAdapter(mAdaptor);
    }
}
