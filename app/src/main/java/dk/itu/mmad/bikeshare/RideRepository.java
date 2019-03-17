package dk.itu.mmad.bikeshare;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class RideRepository {

    private RideDao mRideDao;
    private LiveData<List<Ride>> mAllRides;

    public RideRepository(Application application) {
        RideDB db = RideDB.get(application);
        mRideDao = db.rideDao();
        mAllRides = mRideDao.getAllRides();
    }

    public void insert(Ride ride) {
        new InsertAsyncTask(mRideDao).execute(ride);
    }

    private static class InsertAsyncTask extends AsyncTask<Ride, Void, Void> {

        private RideDao mRideDao;

        public InsertAsyncTask(RideDao rideDao) {
            mRideDao = rideDao;
        }

        @Override
        protected Void doInBackground(final Ride... rides) {
            mRideDao.insert(rides[0]);
            return null;
        }
    }

    public void update(Ride ride) {
        new UpdateAsyncTask(mRideDao).execute(ride);
    }

    private static class UpdateAsyncTask extends AsyncTask<Ride, Void, Void> {

        private RideDao mRideDao;

        public UpdateAsyncTask(RideDao rideDao) {
            mRideDao = rideDao;
        }

        @Override
        protected Void doInBackground(final Ride... rides) {
            mRideDao.update(rides[0]);
            return null;
        }
    }

    public void delete(Ride ride) {
        new DeleteAsyncTask(mRideDao).execute(ride);
    }

    private static class DeleteAsyncTask extends AsyncTask<Ride, Void, Void> {

        private RideDao mRideDao;

        public DeleteAsyncTask(RideDao rideDao) {
            mRideDao = rideDao;
        }

        @Override
        protected Void doInBackground(final Ride... rides) {
            mRideDao.delete(rides[0]);
            return null;
        }
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(mRideDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Ride, Void, Void> {

        private RideDao mRideDao;

        public DeleteAllAsyncTask(RideDao rideDao) {
            mRideDao = rideDao;
        }

        @Override
        protected Void doInBackground(final Ride... rides) {
            mRideDao.deleteAll();
            return null;
        }
    }

    public LiveData<List<Ride>> getAllRides() {
        return mAllRides;
    }

    public Ride getLatestRide(String bikeName) {
        try {
            return new FindAsyncTask(mRideDao).execute(bikeName).get();
        } catch (Exception e) {
            return null;
        }
    }

    private static class FindAsyncTask extends AsyncTask<String, Void, Ride> {

        private RideDao mRideDao;

        public FindAsyncTask(RideDao rideDao) {
            mRideDao = rideDao;
        }

        @Override
        protected Ride doInBackground(final String... bikeNames) {
            return mRideDao.getLatestRide(bikeNames[0]);
        }
    }

    public Ride getRide(int id) {
        try {
            return new ReadAsyncTask(mRideDao).execute(id).get();
        } catch (Exception e) {
            return null;
        }
    }

    private static class ReadAsyncTask extends AsyncTask<Integer, Void, Ride> {

        private RideDao mRideDao;

        public ReadAsyncTask(RideDao rideDao) {
            mRideDao = rideDao;
        }

        @Override
        protected Ride doInBackground(final Integer... bikeIds) {
            return mRideDao.getRide(bikeIds[0]);
        }
    }
}
