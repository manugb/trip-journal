package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.activities.MainActivity;
import utn_frba_mobile.dadm_diario_viajes.adapters.TripsAdapter;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;
import utn_frba_mobile.dadm_diario_viajes.models.User;

public class TripsFragment extends Fragment implements OnMapReadyCallback {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Trip> trips = new ArrayList<>();
    private GoogleMap mMap;
    private MapFragment mapFragment;
    private RelativeLayout noTripsLegend;

    public static TripsFragment newInstance() {
        TripsFragment tripsFragment = new TripsFragment();
        return tripsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User currentUser = ((MainActivity) getActivity()).getLoggedUser();
        findTripsOf(currentUser);
        mAdapter = new TripsAdapter(trips);
    }

    private void findTripsOf(final User currentUser) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("trips").orderByChild("userId").equalTo(currentUser.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Trip trip = child.getValue(Trip.class);
                    trips.add(trip);

                    //Si hay al menos un viaje, oculto el mensaje de no_trips
                    hideNoTripsLegend();
                }
                setTripsMarkers();
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void hideNoTripsLegend() {
        if (!trips.isEmpty()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            noTripsLegend.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.trip_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        noTripsLegend = (RelativeLayout) view.findViewById(R.id.no_trips_legend);

        TabHost host = (TabHost) view.findViewById(R.id.tab_host);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab List");
        spec.setContent(R.id.tab_list);
        spec.setIndicator("Tab List");
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_view_list_black_24dp));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Map");
        spec.setContent(R.id.tab_map);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_map_black_24dp));
        host.addTab(spec);

        // Nested Fragments, dont create mapFragment twice
        if (mapFragment == null) {
            mapFragment = new MapFragment();
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.map_container, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }

        mapFragment.getMapAsync(this);

        hideNoTripsLegend();

        return view;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(4);
        setTripsMarkers();
    }

    public void setTripsMarkers() {
        if (mMap != null) {
            for (Trip trip : trips) {
                // Add a marker for the trips
                LatLng ll = new LatLng(trip.getLatitude(), trip.getLongitude());
                Marker m = mMap.addMarker(new MarkerOptions().position(ll).title(trip.getName()));
                m.showInfoWindow();
                mMap.setOnMarkerClickListener(onTripClick);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
            };
        }
    }

    private GoogleMap.OnMarkerClickListener onTripClick = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Trip selectedTrip = null;
            for (Trip trip : trips) {
                if (trip.getName().equals(marker.getTitle())) {
                    selectedTrip = trip;
                    break;
                }
            }

            if (selectedTrip != null) {
                NotesFragment fragment = new NotesFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("trip", selectedTrip);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            return false;
        }
    };

}
