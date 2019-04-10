package dk.itu.mmad.bikeshare.model;

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
    private User mUser;
    private String mUserEmail;
    private Bike mBike;
    private String mBikeId;

    public Ride() {

    }

    public Ride(int id, String startLocation, Date startDate,
                String endLocation, Date endDate, User user,
                Bike bike) {
        mId = id;
        mStartLocation = startLocation;
        mStartDate = startDate;
        mEndLocation = endLocation;
        mEndDate = endDate;
        mUser = user;
        mUserEmail = (user == null ? null : user.getEmail());
        mBike = bike;
        mBikeId = (bike == null ? null : bike.getId());
    }

    public Ride(int id, String startLocation, Date startDate,
                User user, Bike bike) {
        this(id, startLocation, startDate, null, null, user, bike);
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