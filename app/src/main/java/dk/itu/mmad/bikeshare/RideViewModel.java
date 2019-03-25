package dk.itu.mmad.bikeshare;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RideViewModel extends AndroidViewModel {

    private Realm mRealm;

    // Logging variable
    private static final String TAG = "RideViewModel";

    public RideViewModel(Application application) {
        super(application);
        mRealm = Realm.getDefaultInstance();
    }

    public void insert(final Ride ride) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealm(ride);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Insertion of ride successful");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "Insertion of ride failed");
            }
        });
    }

    public void update(int id, String endRide, Date endDate) {
        Ride ride = getRide(id);

        mRealm.beginTransaction();
        if (ride != null) {
            ride.setEndRide(endRide);
            ride.setEndDate(endDate);
        }
        mRealm.commitTransaction();
    }

    public void delete(final Ride ride) {
        mRealm.beginTransaction();
        ride.deleteFromRealm();
        mRealm.commitTransaction();
    }

    public void deleteAll() {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.deleteAll();
            }
        });
    }

    RealmResults<Ride> getAllRides() {
        mRealm.beginTransaction();
        RealmResults<Ride> mRides = mRealm.where(Ride.class).findAll();
        mRealm.commitTransaction();

        return mRides;
    }

    public Ride getLatestRide(String bikeName) {
        mRealm.beginTransaction();
        Ride ride = mRealm.where(Ride.class)
                .equalTo("mBikeName", bikeName)
                .sort("id", Sort.DESCENDING)
                .findFirst();
        mRealm.commitTransaction();
        return ride;
    }

    public Ride getRide(int id) {
        mRealm.beginTransaction();
        Ride ride = mRealm.where(Ride.class)
                .equalTo("id", id)
                .findFirst();
        mRealm.commitTransaction();
        return ride;
    }
}
