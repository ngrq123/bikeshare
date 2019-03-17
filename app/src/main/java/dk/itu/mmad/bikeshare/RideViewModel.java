package dk.itu.mmad.bikeshare;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class RideViewModel extends AndroidViewModel {

    private RideRepository mRideRepository;
    private LiveData<List<Ride>> mAllRides;

    public RideViewModel(Application application) {
        super(application);
        mRideRepository = new RideRepository(application);
        mAllRides = mRideRepository.getAllRides();
    }

    public void insert(Ride ride) {
        mRideRepository.insert(ride);
    }

    public void update(Ride ride) {
        mRideRepository.update(ride);
    }

    public void delete(Ride ride) {
        mRideRepository.delete(ride);
    }

    public void deleteAll() {
        mRideRepository.deleteAll();
    }

    LiveData<List<Ride>> getAllRides() {
        return mAllRides;
    }

    public Ride getLatestRide(String bikeName) {
        return mRideRepository.getLatestRide(bikeName);
    }

    public Ride getRide(int id) {
        return mRideRepository.getRide(id);
    }
}
