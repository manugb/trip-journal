package utn_frba_mobile.dadm_diario_viajes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.adapters.TripsAdapter;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;

public class TripActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.os.Debug.waitForDebugger();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        mRecyclerView = (RecyclerView) findViewById(R.id.trip_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Trip> trips;
        trips = new ArrayList<>();
        Calendar dateInit = Calendar.getInstance();
        Calendar dateEnd = Calendar.getInstance();

        dateInit.set(2015,10,05);
        dateEnd.set(2015,10,30);
        trips.add(new Trip("España",dateInit,dateEnd, R.drawable.spain));

        dateInit.set(2017,03,8);
        dateEnd.set(2015,03,30);
        trips.add(new Trip("Nueva Zelanda",dateInit,dateEnd,R.drawable.newzealand));

        mAdapter = new TripsAdapter(trips);
        mRecyclerView.setAdapter(mAdapter);
    }
}
