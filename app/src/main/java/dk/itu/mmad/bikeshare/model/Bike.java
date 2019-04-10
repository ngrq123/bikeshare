package dk.itu.mmad.bikeshare.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

public class Bike extends RealmObject {

    @PrimaryKey
    private String mId;
    private String mName;
    private String mType;
    private String mPicture;
    private double mPricePerMin;

    public Bike() {

    }

    public Bike(String id, String name, String type, String picture, double pricePerMin) {
        mId = id;
        mName = name;
        mType = type;
        mPicture = picture;
        mPricePerMin = pricePerMin;
    }

    public Bike(String id, String name, String type, double pricePerMin) {
        this(id, name, type, null, pricePerMin);
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    public double getPricePerMin() {
        return mPricePerMin;
    }

    public void setPricePerMin(double pricePerMin) {
        mPricePerMin = pricePerMin;
    }
}
