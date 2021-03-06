package dk.itu.mmad.bikeshare.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class BikeShareActivity extends AppCompatActivity {
    // Logging variable
    private static final String TAG = "BikeShareActivity";

    // Intent variable
    private static String EXTRA_RIDE_DETAIL = "dk.itu.mmad.bikeshare.EXTRA_RIDE_DETAIL";

    public static Intent newIntent(Context packageContext, String rideDetail) {
        Intent intent = new Intent(packageContext, BikeShareActivity.class);
        intent.putExtra(EXTRA_RIDE_DETAIL, rideDetail);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);

        // Initialise Realm
        Log.d(TAG, "Initialising Realm");
        Realm.init(this);

        if (SyncUser.current() != null) {
            setUpRealmAndContinueToApp();
        } else {
            attemptLogin();
        }
    }

    private void attemptLogin() {
        Log.d(TAG, "Logging in");
        SyncCredentials credentials = SyncCredentials.nickname(Constants.USERNAME, false);
        SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
            @Override
            public void onSuccess(SyncUser user) {
                Log.i(TAG, "Login successful");
                setUpRealmAndContinueToApp();
            }

            @Override
            public void onError(ObjectServerError error) {
                Log.e(TAG, "Uh oh something went wrong! (check your logcat please)");
                Log.e("Login error", error.toString());
            }
        });
    }

    private void setUpRealmAndContinueToApp() {
        SyncConfiguration configuration = SyncUser.current()
                .createConfiguration(Constants.REALM_BASE_URL + "/~/" + Constants.USERNAME)
                .fullSynchronization()
                .build();
        Realm.setDefaultConfiguration(configuration);
//        Realm.deleteRealm(configuration);

        // Continue to app
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String userEmail = sharedPreferences.getString("user", null);

            if (userEmail == null) {
                fragment = new LoginFragment();
            } else {
                fragment = new BikeShareFragment();
            }

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
