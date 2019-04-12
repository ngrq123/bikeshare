package dk.itu.mmad.bikeshare.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Ride;
import dk.itu.mmad.bikeshare.util.PictureUtils;
import io.realm.Realm;
import io.realm.Sort;

public class StartRideActivity extends AppCompatActivity {

    private static final String TAG = "StartRideActivity";

    private static final int REQUEST_PHOTO = 0;

    // GUI variables
    private Button mAddRide;
    private TextView mLastAdded;
    private TextView mNewWhat;
    private TextView mNewWhere;
    private Button mPhotoButton;
    private ImageView mPhotoView;

    private String mLastAddedStr;

    // Photo file
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);

        mLastAdded = (TextView) findViewById(R.id.last_ride);

        // Texts
        mNewWhat = (TextView) findViewById(R.id.what_text);
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

        // Button
        mAddRide = (Button) findViewById(R.id.add_button);

        // Add ride click event
        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mNewWhat.getText().length() > 0 && mNewWhere.getText().length() > 0) {
                    // Get user inputs
                    final String bikeName = mNewWhat.getText().toString().trim();
                    final String startRide = mNewWhere.getText().toString().trim();
                    final Date startDate = Calendar.getInstance().getTime();

                    try (Realm mRealm = Realm.getDefaultInstance()) {
                        mRealm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm bgRealm) {
                                // Find bike in database
                                Bike bike = bgRealm.where(Bike.class)
                                        .equalTo("mName", bikeName)
                                        .findFirst();

                                if (bike == null) {
                                    throw new RuntimeException("Bike is not found.\nPlease select another bike or register a new bike.");
                                }

                                // Check if bike is in use
                                if (bike.isInUse()) {
                                    throw new RuntimeException("Bike is in use.\nPlease select another bike, or register a new bike.");
                                }

                                // Increment ride id
                                Ride maxIdRide = bgRealm.where(Ride.class)
                                        .sort("mId", Sort.DESCENDING)
                                        .findFirst();
                                int rideId = (maxIdRide == null) ? 1 : (maxIdRide.getId() + 1);

                                // Create Ride object and insert
                                Ride ride  = new Ride(rideId, startRide, startDate, toBitmap(), null, bike);
                                ride.getBike().setInUse(true);
                                bgRealm.insert(ride);

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
        mNewWhat.setText("");
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
}
