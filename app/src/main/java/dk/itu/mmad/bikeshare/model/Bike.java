package dk.itu.mmad.bikeshare.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

public class Bike extends RealmObject {

    @PrimaryKey
    private String mId;
    private String mName;
    private String mType;
    private byte[] mPicture;
    private double mPricePerMin;
    private boolean mIsInUse;

    public Bike() {

    }

    public Bike(String id, String name, String type, Bitmap bitmap, double pricePerMin) {
        mId = id;
        mName = name;
        mType = type;
        mPicture = null;
        mPicture = null;
        if (bitmap != null) {
            // Adapted from https://stackoverflow.com/a/7620610
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
            mPicture = blob.toByteArray();
        }
        mPricePerMin = pricePerMin;
        mIsInUse = false;
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

    public Bitmap getPicture() {
        if (mPicture == null || mPicture.length == 0) {
            return null;
        }

        return BitmapFactory.decodeByteArray(mPicture, 0, mPicture.length);
    }

    public double getPricePerMin() {
        return mPricePerMin;
    }

    public void setPricePerMin(double pricePerMin) {
        mPricePerMin = pricePerMin;
    }

    public boolean isInUse() {
        return mIsInUse;
    }

    public void setInUse(boolean inUse) {
        mIsInUse = inUse;
    }

    public String toString() {
        return "Bike " + mName + " (" + mId + ") of " + mType + " type with rental price of " + mPricePerMin + "kr/min";
    }
}
