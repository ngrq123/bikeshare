package dk.itu.mmad.bikeshare;

public class Ride {

    private String mBikeName;
    private String mStartRide;
    private String mEndRide;

    public Ride(String bikeName, String startRide, String endRide) {
        mBikeName = bikeName;
        mStartRide = startRide;
        mEndRide = endRide;
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

    public String getEndRide() {
        return mEndRide;
    }

    public void setEndRide(String endRide) {
        mEndRide = endRide;
    }

    public String toString() {
        String rideStr = mBikeName;

        if (!mStartRide.equals("")) {
            rideStr += " started here: " + mStartRide;
        }

        if (!mStartRide.equals("") && !mEndRide.equals("")) {
            rideStr += " and";
        }

        if (!mEndRide.equals("")) {
            rideStr += " ended here: " + mEndRide;
        }

        return rideStr;
    }

}