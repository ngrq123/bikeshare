package dk.itu.mmad.bikeshare;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "ride")
public class Ride {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "bike_name")
    private String mBikeName;

    @ColumnInfo(name = "start_ride")
    private String mStartRide;

    @ColumnInfo(name = "start_date")
    @TypeConverters({Converters.class})
    private Date mStartDate;

    @ColumnInfo(name = "end_ride")
    private String mEndRide;

    @ColumnInfo(name = "end_date")
    @TypeConverters({Converters.class})
    private Date mEndDate;

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

    public static class Converters {
        @TypeConverter
        public Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public Long dateToTimestamp(Date date) {
            if (date == null) {
                return null;
            } else {
                return date.getTime();
            }
        }
    }

}