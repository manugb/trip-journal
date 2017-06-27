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
        Note note1 = new Note("City Tour","Barcelona",date, R.drawable.citytour1,"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum aliquet justo ac purus mollis gravida vel in quam. Sed vulputate magna tortor, a bibendum purus cursus nec. Phasellus in nunc scelerisque, aliquet mauris venenatis, tempus tortor. Cras dapibus urna dictum ex euismod aliquet. Integer a ornare sem, quis tincidunt ligula.");
        Note note2 = new Note("Circuito Gastronómico","Barcelona",date, R.drawable.comida,"Sed vulputate magna tortor, a bibendum purus cursus nec.");
        Note note3 = new Note("Recorrido Histórico","Barcelona",date, R.drawable.historico,"Vestibulum aliquet justo ac purus mollis gravida vel in quam. Sed vulputate magna tortor, a bibendum purus cursus nec. Phasellus in nunc scelerisque, aliquet mauris venenatis, tempus tortor. Cras dapibus urna dictum ex euismod aliquet. Integer a ornare sem, quis tincidunt ligula.");
        Note note4 = new Note("Circuito de Bares","Barcelona",date, R.drawable.bares,"El mejor trago de la historia!");

        final ArrayList<Note> notesSpain = new ArrayList<>();
        notesSpain.add(note1);
        notesSpain.add(note2);
        notesSpain.add(note3);
        notesSpain.add(note4);

        //New Zealand notes
        Note note5 = new Note("City Tour","Auckland",date, R.drawable.citytour1,"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum aliquet justo ac purus mollis gravida vel in quam. Sed vulputate magna tortor, a bibendum purus cursus nec. Phasellus in nunc scelerisque, aliquet mauris venenatis, tempus tortor. Cras dapibus urna dictum ex euismod aliquet. Integer a ornare sem, quis tincidunt ligula.");
        Note note6 = new Note("Circuito Gastronómico","Rotorua",date, R.drawable.comida,"Vestibulum aliquet justo ac purus mollis gravida vel in quam. Sed vulputate magna tortor, a bibendum purus cursus nec. Phasellus in nunc scelerisque, aliquet mauris venenatis, tempus tortor. Cras dapibus urna dictum ex euismod aliquet. Integer a ornare sem, quis tincidunt ligula.");
        Note note7 = new Note("Recorrido Histórico","Wellington",date, R.drawable.historico,"Phasellus in nunc scelerisque, aliquet mauris venenatis, tempus tortor. Cras dapibus urna dictum ex euismod aliquet. Integer a ornare sem, quis tincidunt ligula.");

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
