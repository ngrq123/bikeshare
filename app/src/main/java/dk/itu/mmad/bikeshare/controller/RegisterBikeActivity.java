package dk.itu.mmad.bikeshare.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.util.PictureUtils;
import io.realm.Realm;

public class RegisterBikeActivity extends AppCompatActivity {

    private static final int REQUEST_PHOTO = 0;

    private TextView mSerialNo;
    private TextView mName;
    private TextView mType;
    private TextView mPrice;
    private ImageView mPhotoView;
    private Button mPhotoButton;
    private Button mRegisterButton;

    // Photo file
    private File mPhotoFile;

    private String mLastAddedBikeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bike);

        mSerialNo = (TextView) findViewById(R.id.serial_no_text);
        mName = (TextView) findViewById(R.id.name_text);
        mType = (TextView) findViewById(R.id.type_text);
        mPrice = (TextView) findViewById(R.id.price_text);

        mPhotoView = (ImageView) findViewById(R.id.bike_photo);
        updatePhotoView();

        mPhotoButton = (Button) findViewById(R.id.bike_camera);
        final File fileDir = this.getFilesDir();
        mPhotoFile = new File(fileDir, "IMG_bike_photo.jpg");
        setUpPhotoTaking();

        // Button
        mRegisterButton = (Button) findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try (Realm realm = Realm.getDefaultInstance()) {
                    if (mSerialNo.getText().length() > 0 &&
                            mName.getText().length() > 0 &&
                            mType.getText().length() > 0 &&
                            mPrice.getText().length() > 0) {
                        final String serialNo = mSerialNo.getText().toString().trim();
                        final String name = mName.getText().toString().trim();
                        final String type = mType.getText().toString().trim();
                        final double price = Double.parseDouble(mPrice.getText().toString().trim());

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

                                bike = new Bike(serialNo, name, type, toBitmap(), price);
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
                } catch (NumberFormatException e) {
                    showDialogBox(view, "Please enter a valid price.");
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
}
