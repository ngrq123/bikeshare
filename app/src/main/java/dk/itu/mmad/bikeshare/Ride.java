package dk.itu.mmad.bikeshare;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Ride extends RealmObject {

    @PrimaryKey
    private int id;
    private String mBikeName;
    private String mStartRide;
    private Date mStartDate;
    private String mEndRide;
    private Date mEndDate;

    public Ride() {

    }

    public Ride(String bikeName, String startRide, Date startDate, String endRide, Date endDate) {
        mBikeName = bikeName;
        mStartRide = startRide;
        mStartDate = startDate;
        mEndRide = endRide;
        mEndDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

        if (!mStartRide.isEmpty()) {
            rideStr += " started here: " + mStartRide + " at " + mStartDate.toString();
        }

        if (!mStartRide.isEmpty() && !mEndRide.isEmpty()) {
            rideStr += " and";
        }

        if (!mEndRide.isEmpty() && mEndDate != null) {
            rideStr += " ended here: " + mEndRide + " at " + mEndDate.toString();
        }

        return rideStr;
    }

//    public static class Converters {
//        @TypeConverter
//        public Date fromTimestamp(Long value) {
//            return value == null ? null : new Date(value);
//        }
//
//        @TypeConverter
//        public Long dateToTimestamp(Date date) {
//            if (date == null) {
//                return null;
//            } else {
//                return date.getTime();
//            }
//        }
//    }

}