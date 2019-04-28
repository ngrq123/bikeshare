package dk.itu.mmad.bikeshare.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Ride extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mStartLocation;
    private Date mStartDate;
    private double mStartLongitude;
    private double mStartLatitude;
    private String mEndLocation;
    private Date mEndDate;
    private double mEndLongitude;
    private double mEndLatitude;
    private byte[] mPicture;
    private String mUserEmail;
    private String mBikeId;
    private String mBikeName;
    private double mPricePerHour;

    public Ride() {

    }

    private Ride(int id, String startLocation, Date startDate,
                 double startLongitude, double startLatitude, String endLocation,
                 Date endDate, double endLongitude, double endLatitude, Bitmap bitmap,
                 String userEmail, String bikeId, String bikeName,
                 double pricePerHour) {
        mId = id;
        mStartLocation = startLocation;
        mStartDate = startDate;
        mStartLongitude = startLongitude;
        mStartLatitude = startLatitude;
        mEndLocation = endLocation;
        mEndDate = endDate;
        mEndLongitude = endLongitude;
        mEndLatitude = endLatitude;

        mPicture = null;
        if (bitmap != null) {
            // Adapted from https://stackoverflow.com/a/7620610
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
            mPicture = blob.toByteArray();
        }

        mUserEmail = userEmail;
        mBikeId = bikeId;
        mBikeName = bikeName;
        mPricePerHour = pricePerHour;
    }

    public Ride(int id, String startLocation, Date startDate,
                double startLongitude, double startLatitude, Bitmap bitmap,
                String userEmail, String bikeId, String bikeName,
                double pricePerHour) {
        this(id, startLocation, startDate,
                startLongitude, startLatitude, null,
                null, -1, -1,
                bitmap, userEmail, bikeId,
                bikeName, pricePerHour);
    }

    public int getId() {
        return mId;
    }

    public String getStartLocation() {
        return mStartLocation;
    }

    public double getStartLongitude() {
        return mStartLongitude;
    }

    public double getStartLatitude() {
        return mStartLatitude;
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

    public double getEndLongitude() {
        return mEndLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        mEndLongitude = endLongitude;
    }

    public double getEndLatitude() {
        return mEndLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        mEndLatitude = endLatitude;
    }

    public Bitmap getPicture() {
        if (mPicture == null || mPicture.length == 0) {
            return null;
        }

        return BitmapFactory.decodeByteArray(mPicture, 0, mPicture.length);
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public String getBikeId() {
        return mBikeId;
    }

    public String getBikeName() {
        return mBikeName;
    }

    public String toString() {
        String rideStr = mBikeName;

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

    public double getAmount() {
        if (mEndDate == null) {
            return -1;
        }

        // Calculate difference in dates
        long diff = mEndDate.getTime() - mStartDate.getTime(); // In milliseconds
        double hours = diff / 1000.0 / 60.0 / 60.0;

        // Multiply by rate
        double amount = hours * mPricePerHour;

        // Adapted from https://stackoverflow.com/a/2808648
        BigDecimal bd = new BigDecimal(amount);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public boolean isStartLocationKnown() {
        return mStartLongitude != -1 && mStartLatitude != -1;
    }

    public boolean isEndLocationKnown() {
        return mEndLongitude != -1 && mEndLatitude != -1;
    }
}