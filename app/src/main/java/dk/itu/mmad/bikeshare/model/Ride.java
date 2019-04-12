package dk.itu.mmad.bikeshare.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Ride extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mStartLocation;
    private Date mStartDate;
    private String mEndLocation;
    private Date mEndDate;
    private byte[] mPicture;
    private User mUser;
    private String mUserEmail;
    private Bike mBike;
    private String mBikeId;

    public Ride() {

    }

    public Ride(int id, String startLocation, Date startDate,
                String endLocation, Date endDate, Bitmap bitmap, User user,
                Bike bike) {
        mId = id;
        mStartLocation = startLocation;
        mStartDate = startDate;
        mEndLocation = endLocation;
        mEndDate = endDate;
        mPicture = null;
        if (bitmap != null) {
            // Adapted from https://stackoverflow.com/a/7620610
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
            mPicture = blob.toByteArray();
        }
        mUser = user;
        mUserEmail = (user == null ? null : user.getEmail());
        mBike = bike;
        mBikeId = (bike == null ? null : bike.getId());
    }

    public Ride(int id, String startLocation, Date startDate,
                Bitmap bitmap, User user, Bike bike) {
        this(id, startLocation, startDate, null, null, bitmap, user, bike);
    }

    public int getId() {
        return mId;
    }

    public String getStartLocation() {
        return mStartLocation;
    }

    public String getEndLocation() {
        return mEndLocation;
    }

    public void setEndLocation(String endLocation) {
        mEndLocation = endLocation;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public Bitmap getPicture() {
        if (mPicture == null || mPicture.length == 0) {
            return null;
        }

        return BitmapFactory.decodeByteArray(mPicture, 0, mPicture.length);
    }

    public User getUser() {
        return mUser;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public Bike getBike() {
        return mBike;
    }

    public String getBikeId() {
        return mBikeId;
    }

    public String toString() {
        String rideStr = mBike.getName();

        if (mStartLocation != null) {
            rideStr += " started at " + mStartLocation + " on " + mStartDate.toString();
        }

        if (mStartLocation != null && mEndLocation != null) {
            rideStr += " and";
        }

        if (mEndLocation != null) {
            rideStr += " ended at " + mEndLocation + " on " + mEndDate.toString();
        }

        return rideStr;
    }
}