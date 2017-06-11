package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.adapters.TripsAdapter;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;

public class TripFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Activity activity;
    private RecyclerView.LayoutManager mLayoutManager;

    public static TripFragment newInstance() {
        TripFragment tripFragment = new TripFragment();
        return tripFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trip, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.trip_recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        /*Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.trip_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
*/
        List<Trip> trips;
        trips = new ArrayList<>();
        Date dateInit = new Date();
        Date dateEnd = new Date();

        trips.add(new Trip("España", dateInit, dateEnd, R.drawable.spain));
        trips.add(new Trip("Nueva Zelanda", dateInit, dateEnd, R.drawable.newzealand));

        mAdapter = new TripsAdapter(trips);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
