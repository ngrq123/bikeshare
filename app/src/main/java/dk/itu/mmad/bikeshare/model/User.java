package dk.itu.mmad.bikeshare.model;

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

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public double getBalance() {
        return mBalance;
    }

    public void addBalance(double balance) {
        mBalance += balance;
    }

    public void deductBalance(double balance) {
        mBalance -= balance;
    }
}
