package dk.itu.mmad.bikeshare.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.Bike;
import dk.itu.mmad.bikeshare.model.Ride;
import io.realm.Realm;

public class ListBikesActivity extends AppCompatActivity {

    private CheckBox mOnlyFreeCheckBox;
    private RecyclerView mRecyclerView;
    private BikeAdaptor mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bikes);

        mOnlyFreeCheckBox = (CheckBox) findViewById(R.id.only_free_check_box);
        mOnlyFreeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try (Realm realm = Realm.getDefaultInstance()) {
                    if (mOnlyFreeCheckBox.isChecked()) {
                        mAdaptor = new BikeAdaptor(realm.where(Bike.class)
                                .equalTo("mIsInUse", false)
                                .findAllAsync());
                    } else {
                        mAdaptor = new BikeAdaptor(realm.where(Bike.class).findAllAsync());
                    }
                    mRecyclerView.setAdapter(mAdaptor);
                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.bike_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        try (Realm realm = Realm.getDefaultInstance()) {
            mAdaptor = new BikeAdaptor(realm.where(Bike.class).findAllAsync());
            mRecyclerView.setAdapter(mAdaptor);
        }

    }
}
