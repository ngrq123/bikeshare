package dk.itu.mmad.bikeshare.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Ride;
import dk.itu.mmad.bikeshare.model.User;
import dk.itu.mmad.bikeshare.util.PictureUtils;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.Sort;

public class StartRideActivity extends AppCompatActivity {

    private static final String TAG = "StartRideActivity";

    private static final int REQUEST_PHOTO = 0;

    // GUI variables
    private Button mAddRide;
    private TextView mLastAdded;
    private Spinner mNewWhatSpinner;
    private TextView mNewWhere;
    private Button mPhotoButton;
    private ImageView mPhotoView;

    private String mSelectedBikeId;
    private String mLastAddedStr;

    // Photo file
    private File mPhotoFile;

    // Location
    private static ArrayList<String> mPermissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private double mLongitude = -1;
    private double mLatitude = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);

        mLastAdded = (TextView) findViewById(R.id.last_ride);

        mNewWhatSpinner = (Spinner) findViewById(R.id.what_text_spinner);

        try (Realm realm = Realm.getDefaultInstance()) {
            BikeSelectionAdaptor mAdaptor = new BikeSelectionAdaptor(realm.where(Bike.class)
                    .equalTo("mIsInUse", false)
                    .findAllAsync());
            mNewWhatSpinner.setAdapter(mAdaptor);

            mNewWhatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // https://www.mkyong.com/android/android-spinner-drop-down-list-example/
                    mSelectedBikeId = ((Bike) adapterView.getItemAtPosition(i)).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
        }

        mNewWhere = (TextView) findViewById(R.id.where_text);

        // Image and camera button
        mPhotoView = (ImageView) findViewById(R.id.ride_photo);
        updatePhotoView();

        mPhotoButton = (Button) findViewById(R.id.ride_camera);

        final File fileDir = this.getFilesDir();
        mPhotoFile = new File(fileDir, "IMG_ride_photo.jpg");

        // From textbook listing 16.8 (page 312)
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(getPackageManager()) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                        "dk.itu.mmad.bikeshare.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getApplicationContext()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getApplicationContext().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        // Location
        getLocationCoordinates();

        // Button
        mAddRide = (Button) findViewById(R.id.add_button);

        // Add ride click event
        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mSelectedBikeId != null && mNewWhere.getText().length() > 0) {
                    // Get user inputs
                    final String startRide = mNewWhere.getText().toString().trim();
                    final Date startDate = Calendar.getInstance().getTime();

                    try (Realm mRealm = Realm.getDefaultInstance()) {
                        mRealm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm bgRealm) {
                                // Find bike in database
                                Bike bike = bgRealm.where(Bike.class)
                                        .equalTo("mId", mSelectedBikeId)
                                        .findFirst();

                                if (bike == null) {
                                    throw new RuntimeException("Bike is not found.\nPlease select another bike or register a new bike.");
                                }

                                // Check if bike is in use
                                if (bike.isInUse()) {
                                    throw new RuntimeException("Bike is in use.\nPlease select another bike, or register a new bike.");
                                }

                                // Find user in database
                                User user = bgRealm.where(User.class)
                                        .equalTo("mEmail", PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                                .getString("user", null))
                                        .findFirst();

                                // Check account balance if bike is not user's
                                if (!bike.getUserEmail().equals(user.getEmail()) && user.getBalance() <= 0.0) {
                                    throw new RuntimeException("Balance is too low.\nPlease top up.");
                                }

                                // Increment ride id
                                Ride maxIdRide = bgRealm.where(Ride.class)
                                        .sort("mId", Sort.DESCENDING)
                                        .findFirst();
                                int rideId = (maxIdRide == null) ? 1 : (maxIdRide.getId() + 1);

                                // Create Ride object, update bike and insert ride
                                Ride ride  = new Ride(rideId, startRide, startDate,
                                        mLongitude, mLatitude, toBitmap(),
                                        user.getEmail(), bike.getId(), bike.getName(),
                                        bike.getPricePerHr());
                                bike.setInUse(true);
                                bgRealm.insert(ride);
                                bgRealm.insertOrUpdate(bike);

                                mPhotoFile.delete();
                                mLastAddedStr = ride.toString();
                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                updateUI();
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                showDialogBox(view, error.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    private void updateUI() {
        // Reset text and image
//        mNewWhat.setText("");
        mNewWhere.setText("");
        mPhotoView.setImageDrawable(null);

        if (mLastAddedStr != null && !mLastAddedStr.isEmpty()) {
            mLastAdded.setText(mLastAddedStr);
        }
    }

    // Adapted from textbook listing 16.12 (page 315)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(this,
                    "dk.itu.mmad.bikeshare.fileprovider",
                    mPhotoFile);
            this.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    // From textbook listing 16.11 (page 315)
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), this);
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void showDialogBox(View view, String message) {
        new AlertDialog.Builder(view.getContext())
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    private Bitmap toBitmap() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            return null;
        }

        return PictureUtils.getScaledBitmap(mPhotoFile.getPath(), this);
    }

    private void getLocationCoordinates() {
        mPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        mPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        ArrayList<String> permissionsToRequest = permissionsToRequest(mPermissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
            }
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    mLongitude = location.getLongitude();
                    mLatitude = location.getLatitude();
                }
            }
        };

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> permissions) {
        ArrayList<String> result = new ArrayList<>();
        for (String permission : permissions)
            if (!hasPermission(permission))
                result.add(permission);
        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Objects.requireNonNull(this).checkSelfPermission(permission) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    private void startLocationUpdates() {
        if (checkPermission()) {
            return;
        }
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }
}
