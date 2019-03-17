package dk.itu.mmad.bikeshare;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RideDao {

    @Insert
    void insert(Ride ride);

    @Update
    void update(Ride ride);

    @Delete
    void delete(Ride ride);

    @Query("DELETE FROM ride")
    void deleteAll();

    @Query("SELECT * FROM ride " +
            "ORDER BY id")
    LiveData<List<Ride>> getAllRides();

    @Query("SELECT * FROM ride " +
            "WHERE bike_name = :bikeName AND  end_date IS NULL " +
            "ORDER BY start_date DESC " +
            "LIMIT 1")
    Ride getLatestRide(String bikeName);

    @Query("SELECT * FROM ride " +
            "WHERE id = :id ")
    Ride getRide(int id);
}
