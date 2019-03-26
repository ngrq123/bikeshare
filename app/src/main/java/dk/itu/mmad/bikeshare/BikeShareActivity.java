package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
        Realm.init(this);

//        if (SyncUser.current() != null) {
//            setUpRealmAndContinueToApp();
//        }
//
//        attemptLogin();
//    }
//
//    private void attemptLogin() {
//        SyncCredentials credentials = SyncCredentials.nickname(Constants.USERNAME, true);
//        SyncUser.logInAsync(credentials, Constants.AUTH_URL, new SyncUser.Callback<SyncUser>() {
//            @Override
//            public void onSuccess(SyncUser user) {
//                setUpRealmAndContinueToApp();
//            }
//
//            @Override
//            public void onError(ObjectServerError error) {
//                Log.e("Login error", "Uh oh something went wrong! (check your logcat please)");
//            }
//        });
//    }
//
//    private void setUpRealmAndContinueToApp() {
//        SyncConfiguration configuration = SyncUser.current().getDefaultConfiguration();
//        Realm.setDefaultConfiguration(configuration);

        // Continue to app
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
