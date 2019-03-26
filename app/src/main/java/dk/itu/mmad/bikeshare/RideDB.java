package dk.itu.mmad.bikeshare;

import android.app.Application;
import android.util.Log;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncUser;

public class RideDB {

    private Realm mRealm;

    // Logging variable
    private static final String TAG = "RideViewModel";

    public RideDB() {
        mRealm = Realm.getDefaultInstance();
    }

    public void insert(final Ride ride) {
        // Increment id
        ride.setId(getMaxId() + 1);

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

    public void update(Ride rideToUpdate, String endRide, Date endDate) {
        Ride ride = getRide(rideToUpdate.getId());

        if (ride != null) {
            mRealm.beginTransaction();
            ride.setEndRide(endRide);
            ride.setEndDate(endDate);
            mRealm.commitTransaction();
            Log.d(TAG, "Updating of ride successful");
        } else {
            Log.d(TAG, "Updating of ride failed");
        }
    }

    public void delete(Ride ride) {
        mRealm.beginTransaction();
        ride.deleteFromRealm();
        mRealm.commitTransaction();
        Log.d(TAG, "Deletion of ride successful");
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

    public int getMaxId() {
        mRealm.beginTransaction();
        Ride ride = mRealm.where(Ride.class)
                .sort("id", Sort.DESCENDING)
                .findFirst();
        mRealm.commitTransaction();

        return (ride == null) ? 0 : ride.getId();
    }

    public void close() {
        mRealm.close();
    }
}
