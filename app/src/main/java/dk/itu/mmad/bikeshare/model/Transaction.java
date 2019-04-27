package dk.itu.mmad.bikeshare.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Transaction extends RealmObject {

    @PrimaryKey
    private String mId;
    private String mUserEmail;
    private Date mDate;
    private double mAmount;
    private String mDescription;
    private Bike mBike;

    public Transaction() {

    }

    public Transaction(String userEmail, double amount, Bike bike) {
        mUserEmail = userEmail;
        mDate = Calendar.getInstance().getTime();
        mAmount = (bike == null || bike.getUserEmail().equals(userEmail)) ? amount : amount * -1;
        mBike = bike;

        if (bike == null) {
            mDescription = "Top Up";
        } else if (mUserEmail.equals(bike.getUserEmail())) {
            mDescription = "Credit from Usage of " + bike.getName();
        } else {
            mDescription = "Ended ride on " + bike.getName();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        mId = mUserEmail.substring(0, mUserEmail.indexOf("@")) + (bike == null ? "" : bike.getId()) + simpleDateFormat.format(mDate);
    }

    public Transaction(String userEmail, double amount) {
        this(userEmail, amount, null);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public double getAmount() {
        return mAmount;
    }

    public String getDescription() {
        return mDescription;
    }
}
