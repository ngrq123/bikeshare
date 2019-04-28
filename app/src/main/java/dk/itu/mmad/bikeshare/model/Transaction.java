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

    public Transaction() {

    }

    public Transaction(String userEmail, double amount, String bikeOwnerEmail, String bikeId, String bikeName) {
        mUserEmail = userEmail;
        mDate = Calendar.getInstance().getTime();
        mAmount = (bikeOwnerEmail == null || bikeOwnerEmail.equals(userEmail)) ? amount : amount * -1;

        if (mAmount > 0 && bikeOwnerEmail == null) {
            mDescription = "Top Up";
        } else if (mUserEmail.equals(bikeOwnerEmail)) {
            mDescription = "Credit from Usage of " + bikeName;
        } else {
            mDescription = "Ended ride on " + bikeName;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        mId = mUserEmail.substring(0, mUserEmail.indexOf("@")) + bikeId + simpleDateFormat.format(mDate);
    }

    public Transaction(String userEmail, double amount) {
        this(userEmail, amount, null, null, null);
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
