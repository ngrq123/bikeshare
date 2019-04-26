package dk.itu.mmad.bikeshare.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Transaction extends RealmObject {

    @PrimaryKey
    private String mId;
    private User mUser;
    private String mUserEmail;
    private Date mDate;
    private double mAmount;
    private String mBikeName;

    public Transaction() {

    }

    public Transaction(User user, Date date, double amount, String bikeName) {
        mUser = user;
        mUserEmail = (user == null ? null : user.getEmail());
        mDate = date;
        mAmount = bikeName == null ? amount : amount * -1;
        mBikeName = bikeName;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        mId = mUserEmail + simpleDateFormat.format(mDate);
    }

    public Transaction(User user, Date date, double amount) {
        this(user, date, amount, null);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

    public String getBikeName() {
        return mBikeName;
    }

    public void setBikeName(String bikeName) {
        mBikeName = bikeName;
    }

    public String getDescription() {
        return mBikeName == null ? "Top Up" : "Ended ride on " + mBikeName;
    }
}
