package dk.itu.mmad.bikeshare;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class RidesDB {

    private static RidesDB sRidesDB;
    private ArrayList<Ride> mAllRides;
    private Ride mLastRide;

    private RidesDB(Context context) {
        mLastRide = new Ride("", "", "");

        // Add some rides for testing purposes
        mAllRides = new ArrayList<>();
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "ITU", "Fields"));
        mAllRides.add(new Ride("Chuck Norris bike",
                "Fields", "Kongens Nytorv"));
        mAllRides.add(new Ride("Bruce Lee bike",
                "Kobenhavns Lufthavn", "Kobenhavns Hovedbanegard"));
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

    public void addRide(String what, String where) {
        mLastRide.setBikeName(what);
        mLastRide.setStartRide(where);
        mAllRides.add(mLastRide);
    }

    public void endRide(String what, String where) {
        if (mLastRide.getBikeName().equals(what) &&
                mLastRide.getEndRide().equals("")) {
            mLastRide.setEndRide(where);
            mLastRide = new Ride("", "", "");
        }
    }

}
