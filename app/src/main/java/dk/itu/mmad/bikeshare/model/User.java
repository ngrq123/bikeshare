package dk.itu.mmad.bikeshare.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String mEmail;
    private String mPassword;
    private double mBalance;

    public User() {

    }

    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
        mBalance = 0.0;
    }

    public String getEmail() {
        return mEmail;
    }

    public double getBalance() {
        // Adapted from https://stackoverflow.com/a/2808648
        BigDecimal bd = new BigDecimal(mBalance);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        mBalance = bd.doubleValue();
        return mBalance;
    }

    public void addBalance(double balance) {
        mBalance += balance;
    }

    public void deductBalance(double balance) {
        mBalance -= balance;
    }
}
