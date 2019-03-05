package dk.itu.mmad.bikeshare;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RidesDB {
    // Logging variable
    private static final String TAG = "RidesDB";

    private static RidesDB sRidesDB;
    private ArrayList<Ride> mAllRides;
    private Ride mLastRide;

    private RidesDB(Context context) {
        mLastRide = new Ride("", "", null, "", null);

        // Add some rides for testing purposes
        mAllRides = new ArrayList<>();
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", setDate(2019, 0, 1, 11, 00, 00),
                "Fields", setDate(2019, 0, 1, 11, 20, 00)));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", setDate(2019, 0, 1, 11, 30, 00),
                "Kongens Nytorv", setDate(2019, 0, 1, 12, 31, 12)));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", setDate(2019, 0, 1, 11, 45, 13),
                "Kobenhavns Hovedbanegard", setDate(2019, 0, 1, 12, 45, 55)));
    }

    private Date setDate(int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hourOfDay, minute, second);
        return calendar.getTime();
    }

    public static RidesDB get(Context context) {
        if (sRidesDB == null) {
            sRidesDB = new RidesDB(context);
        }
        return sRidesDB;
    }

    public List<Ride> getRidesDB() {
        return mAllRides;
    }

    public void addRide(String what, String where, Date startDate) {
        Ride ride = new Ride(what, where, startDate, "", null);
        mAllRides.add(ride);
        mLastRide = ride;
    }

    public void endRide(String what, String where, Date endDate) {
        if (mLastRide.getBikeName().equals(what) &&
                mLastRide.getEndRide().equals("")) {
            mLastRide.setEndRide(where);
            mLastRide.setEndDate(endDate);
            mLastRide = new Ride("", "", null, "", null);
        } else {
            boolean isUpdated = false;
            for (int idx = mAllRides.size()-1; !isUpdated && idx >= 0; idx--) {
                Ride ride = mAllRides.get(idx);
                if (ride.getBikeName().equals(what) &&
                        ride.getEndRide().equals("")) {
                    ride.setEndRide(where);
                    ride.setEndDate(endDate);
                    isUpdated = true;
                }
            }
        }
    }

    public void deleteRide(int position) {
        mAllRides.remove(position);
    }

}
