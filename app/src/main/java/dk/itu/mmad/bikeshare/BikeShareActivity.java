package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class BikeShareActivity extends AppCompatActivity {
    // Logging variable
    private static final String TAG = "BikeShareActivity";

    // Intent variable
    private static String EXTRA_POSITION = "dk.itu.mmad.bikeshare.EXTRA_POSITION";

    public static Intent newIntent(Context packageContext, int position) {
        Intent intent = new Intent(packageContext, BikeShareActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new BikeShareFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
