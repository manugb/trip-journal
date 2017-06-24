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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.adapters.TripsAdapter;
import utn_frba_mobile.dadm_diario_viajes.models.Note;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Trip> trips = new ArrayList<>();
        Date dateInit = new Date();
        Date dateEnd = new Date();

        Date date = new Date();

        //Spain notes
        Note note1 = new Note("City Tour","Barcelona",date);
        Note note2 = new Note("Circuito Gastronómico","Barcelona",date);
        Note note3 = new Note("Recorrido Histórico","Barcelona",date);
        Note note4 = new Note("Circuito de Bares","Barcelona",date);

        final ArrayList<Note> notesSpain = new ArrayList<>();
        notesSpain.add(note1);
        notesSpain.add(note2);
        notesSpain.add(note3);
        notesSpain.add(note4);

        //New Zealand notes
        Note note5 = new Note("City Tour","Auckland",date);
        Note note6 = new Note("Circuito Gastronómico","Rotorua",date);
        Note note7 = new Note("Recorrido Histórico","Wellington",date);

        final ArrayList<Note> notesNZ = new ArrayList<>();
        notesNZ.add(note5);
        notesNZ.add(note6);
        notesNZ.add(note7);

        trips.add(new Trip("España", dateInit, dateEnd, R.drawable.spain, notesSpain));
        trips.add(new Trip("Nueva Zelanda", dateInit, dateEnd, R.drawable.newzealand,notesNZ));

        mAdapter = new TripsAdapter(trips);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.trip_recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
