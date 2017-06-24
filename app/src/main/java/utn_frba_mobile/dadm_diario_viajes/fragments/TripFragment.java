package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.adapters.TripsAdapter;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;
import utn_frba_mobile.dadm_diario_viajes.models.User;

public class TripFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Activity activity;
    private RecyclerView.LayoutManager mLayoutManager;

    public static TripFragment newInstance() {
        TripFragment tripFragment = new TripFragment();
        return tripFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Trip spainTrip = createTripFor(currentUser.getUid(), "España", R.drawable.spain);
        Trip newZelandTrip = createTripFor(currentUser.getUid(), "Nueva Zelanda", R.drawable.newzealand);

        List<Trip> trips = new ArrayList<>();
        trips.add(spainTrip);
        trips.add(newZelandTrip);

        //TODO: Despues todo esto lo voy a reempleazar por un get de los trips que tiene el usuario en la base
        mAdapter = new TripsAdapter(trips);
    }

    private Trip createTripFor(String userUID, String name, int photo) {
        // Create new trip at /user-trips/$userid/$tripid and at
        // /trips/$tripid simultaneously
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String key = database.child("trips").push().getKey();

        Date dateInit = new Date();
        Date dateEnd = new Date();
        Trip trip = new Trip(key, name, dateInit, dateEnd, photo);
        Map<String, Object> tripValues = trip.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/trips/" + key, tripValues);
        childUpdates.put("/users/" + userUID + "/trips/" + key, tripValues);

        database.updateChildren(childUpdates);
        return trip;
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
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
