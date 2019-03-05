package dk.itu.mmad.bikeshare;

import java.util.Date;

public class Ride {

    private String mBikeName;
    private String mStartRide;
    private Date mStartDate;
    private String mEndRide;
    private Date mEndDate;

    public Ride(String bikeName, String startRide, Date startDate, String endRide, Date endDate) {
        mBikeName = bikeName;
        mStartRide = startRide;
        mStartDate = startDate;
        mEndRide = endRide;
        mEndDate = endDate;
    }

    public String getBikeName() {
        return mBikeName;
    }

    public void setBikeName(String bikeName) {
        mBikeName = bikeName;
    }

    public String getStartRide() {
        return mStartRide;
    }

    public void setStartRide(String startRide) {
        mStartRide = startRide;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public String getEndRide() {
        return mEndRide;
    }

    public void setEndRide(String endRide) {
        mEndRide = endRide;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public String toString() {
        String rideStr = mBikeName;

        if (!mStartRide.equals("")) {
            rideStr += " started here: " + mStartRide + " at " + mStartDate.toString();
        }

        if (!mStartRide.equals("") && !mEndRide.equals("")) {
            rideStr += " and";
        }

        if (!mEndRide.equals("") && mEndDate != null) {
            rideStr += " ended here: " + mEndRide + " at " + mEndDate.toString();
        }

        return rideStr;
    }

}