package dk.itu.mmad.bikeshare.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Bike extends RealmObject {

    @PrimaryKey
    private String mId;
    private String mName;
    private String mType;
    private double mPricePerHr;
    private String mLocation;
    private boolean mIsInUse;
    private byte[] mPicture;
    private double mLongitude;
    private double mLatitude;
    private String mUserEmail;

    public Bike() {

    }

    public Bike(String id, String name, String type,
                double pricePerHr, String location, String userEmail,
                Bitmap bitmap, double longitude, double latitude) {
        mId = id;
        mName = name;
        mType = type;
        mPricePerHr = pricePerHr;
        mLocation = location;
        mIsInUse = false;

        mPicture = null;
        if (bitmap != null) {
            // Adapted from https://stackoverflow.com/a/7620610
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
            mPicture = blob.toByteArray();
        }

        mLongitude = longitude;
        mLatitude = latitude;

        mUserEmail = userEmail;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public double getPricePerHr() {
        return mPricePerHr;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public Bitmap getPicture() {
        if (mPicture == null || mPicture.length == 0) {
            return null;
        }

        return BitmapFactory.decodeByteArray(mPicture, 0, mPicture.length);
    }

    public boolean isInUse() {
        return mIsInUse;
    }

    public void setInUse(boolean inUse) {
        mIsInUse = inUse;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public boolean isLocationKnown() {
        return mLongitude != -1 && mLatitude != -1;
    }

    public String toString() {
        String bikeStr = "The " + mType + " bike " + mName + " (" + mId + ") with rental price of " + mPricePerHr + " kr/hr is at " + mLocation;

        if (isLocationKnown()) {
            bikeStr += " (" + mLongitude + ", " + mLatitude + ")";
        }

        return bikeStr;
    }
}
