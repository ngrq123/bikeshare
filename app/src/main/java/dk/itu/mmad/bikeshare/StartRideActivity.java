package dk.itu.mmad.bikeshare;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class StartRideActivity extends AppCompatActivity {

    private static final int REQUEST_PHOTO = 0;

    // GUI variables
    private Button mAddRide;
    private TextView mLastAdded;
    private TextView mNewWhat;
    private TextView mNewWhere;
    private Button mPhotoButton;
    private ImageView mPhotoView;

    // Database
    private Realm mRealm;

    // Last ride information
    private Ride mLast = new Ride("", "", null, "", null);

    // Photo file
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);

        mLastAdded = (TextView) findViewById(R.id.last_ride);
        updateUI();

        // Texts
        mNewWhat = (TextView) findViewById(R.id.what_text);
        mNewWhere = (TextView) findViewById(R.id.where_text);

        // Image and camera button
        mPhotoView = (ImageView) findViewById(R.id.bike_photo);
        updatePhotoView();

        mPhotoButton = (Button) findViewById(R.id.bike_camera);

        final File fileDir = this.getFilesDir();
        mPhotoFile = new File(fileDir, "IMG_bike_photo.jpg");

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

        // Database
        mRealm = Realm.getDefaultInstance();

        // Add ride click event
        mAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNewWhat.getText().length() > 0 && mNewWhere.getText().length() > 0) {
                    String bikeName = mNewWhat.getText().toString().trim();
                    String startRide = mNewWhere.getText().toString().trim();
                    Date startDate = Calendar.getInstance().getTime();

                    mLast.setBikeName(bikeName);
                    mLast.setStartRide(startRide);
                    mLast.setStartDate(startDate);

                    mRealm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            Ride maxIdRide = bgRealm.where(Ride.class)
                                    .sort("id", Sort.DESCENDING)
                                    .findFirst();
                            int rideId = (maxIdRide == null) ? 1 : (maxIdRide.getId() + 1);

                            mLast.setId(rideId);
                            bgRealm.insert(mLast);

                            mPhotoFile.renameTo(new File(fileDir, "bike_photo_" + rideId + ".jpg"));
                        }
                    });

                    // Reset fields
                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    mPhotoView.setImageDrawable(null);
                    updateUI();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }

    private void updateUI() {
        if (mLast.getBikeName().isEmpty() && mLast.getStartRide().isEmpty()) {
            mLastAdded.setText("");
        } else {
            mLastAdded.setText(mLast.toString());
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
}
