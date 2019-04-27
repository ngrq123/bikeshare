package dk.itu.mmad.bikeshare.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.util.PictureUtils;
import dk.itu.mmad.bikeshare.util.ValidationUtils;
import io.realm.Realm;

public class RegisterBikeActivity extends AppCompatActivity {

    private static final int REQUEST_PHOTO = 0;

    private TextView mSerialNo;
    private TextView mName;
    private TextView mType;
    private TextView mPrice;
    private TextView mLocation;
    private ImageView mPhotoView;
    private Button mPhotoButton;
    private Button mRegisterButton;

    // Photo file
    private File mPhotoFile;

    private String mLastAddedBikeStr;

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
        setContentView(R.layout.activity_register_bike);

        mSerialNo = (TextView) findViewById(R.id.serial_no_text);
        mName = (TextView) findViewById(R.id.name_text);
        mType = (TextView) findViewById(R.id.type_text);
        mPrice = (TextView) findViewById(R.id.price_text);
        mLocation = (TextView) findViewById(R.id.location_text);

        mPhotoView = (ImageView) findViewById(R.id.bike_photo);
        updatePhotoView();

        mPhotoButton = (Button) findViewById(R.id.bike_camera);
        final File fileDir = this.getFilesDir();
        mPhotoFile = new File(fileDir, "IMG_bike_photo.jpg");
        setUpPhotoTaking();

        // Location
        getLocationCoordinates();

        // Button
        mRegisterButton = (Button) findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String serialNo = mSerialNo.getText().toString().trim();
                final String name = mName.getText().toString().trim();
                final String type = mType.getText().toString().trim();
                String priceStr = mPrice.getText().toString().trim();
                final String location = mLocation.getText().toString().trim();

                if (serialNo.length() > 0 &&
                        name.length() > 0 &&
                        type.length() > 0 &&
                        ValidationUtils.isValidDouble(priceStr) &&
                        location.length() > 0) {

                    final double price = Double.parseDouble(priceStr);

                    try (Realm realm = Realm.getDefaultInstance()) {
                        // Check if serial number is already registered
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm bgRealm) {
                                Bike bike = bgRealm.where(Bike.class)
                                        .equalTo("mId", serialNo)
                                        .findFirst();

                                if (bike != null) {
                                    throw new RuntimeException("Bike id has been registered.\nPlease check again.");
                                }

                                String userEmail = PreferenceManager.getDefaultSharedPreferences(view.getContext())
                                        .getString("user", null);

                                bike = new Bike(serialNo, name, type,
                                        price, location, userEmail,
                                        toBitmap(), mLongitude, mLatitude);
                                bgRealm.insert(bike);

                                mPhotoFile.delete();
                                mLastAddedBikeStr = bike.toString();
                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                showDialogBox(view, mLastAddedBikeStr + " has been added.");
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

    private void setUpPhotoTaking() {
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

//    private String getAddress(double longitude, double latitude) {
//        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            List<Address> addresses =
//                    geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses.size() > 0) {
//                Address address = addresses.get(0);
//                stringBuilder.append(address.getAddressLine(0)).append("\n");
//                stringBuilder.append(address.getLocality()).append("\n");
//                stringBuilder.append(address.getPostalCode()).append("\n");
//                stringBuilder.append(address.getCountryName());
//            } else {
//                return "No address found";
//            }
//        } catch (IOException ex) {
//            return ex.getMessage();
//        }
//        return stringBuilder.toString();
//    }
}
