package dk.itu.mmad.bikeshare.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String email;
    private String password;
    private double balance;

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.balance = 0.0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double balance) {
        this.balance += balance;
    }

    public void deductBalance(double balance) {
        this.balance -= balance;
    }
}
