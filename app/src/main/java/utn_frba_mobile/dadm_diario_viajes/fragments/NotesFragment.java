package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.adapters.NotesAdapter;
import utn_frba_mobile.dadm_diario_viajes.models.Note;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;

public class NotesFragment extends Fragment {
    private Trip trip;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Activity activity;
    private RecyclerView.LayoutManager mLayoutManager;
    private Trip trip;
    private ArrayList<Note> notes;

    public static NotesFragment newInstance() {
        NotesFragment notesFragment = new NotesFragment();
        return notesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            trip = (Trip) bundle.getSerializable("trip");
        }

        notes = trip.getNotes();
        
        Note note1 = createNote("City Tour","Barcelona");
        Note note2 = createNote("Circuito Gastronómico","Barcelona");
        Note note3 = createNote("Recorrido Histórico","Barcelona");
        Note note4 = createNote("Circuito de Bares","Barcelona");


        notes.add(note1);
        notes.add(note2);
        notes.add(note3);
        notes.add(note4);

        //TODO: Despues todo esto lo voy a reempleazar por un get de las notas que tiene el viaje en la base
        mAdapter = new NotesAdapter(notes);
    }


    private Note createNote(String name, String location) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String key = database.child("notes").push().getKey();

        Note note = new Note(key, name, location, new Date());
        Map<String, Object> noteValues = note.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + key, noteValues);
        childUpdates.put("/trips/" + trip.getId() + "/notes/" + key, noteValues);

        database.updateChildren(childUpdates);
        return note;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.notes_recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                notes.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }

}
