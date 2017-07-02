package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.models.Note;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;

public class NoteFragment extends Fragment {

    private EditText name;
    private EditText date;
    private EditText description;

    private Note note;
    private Trip trip;
    private Button btnNewNote;

    public static NoteFragment newInstance() {
        NoteFragment noteFragment = new NoteFragment();
        return noteFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey("note")) {
                note = (Note) bundle.getSerializable("note");

                getTripByNote(note);

            } else if (bundle.containsKey("trip")){
                trip = (Trip) bundle.getSerializable("trip");
            }
        }
    }

    private void getTripByNote(Note note) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("trips").orderByChild("id").equalTo(note.getTripId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    trip = child.getValue(Trip.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        name = (EditText) view.findViewById(R.id.name);
        date = (EditText) view.findViewById(R.id.date);
        description = (EditText) view.findViewById(R.id.description);

        if (note != null) {
            name.setText(note.getName());
            date.setText(note.getDate().toString());
            description.setText(note.getDescription());
        }

        btnNewNote = (Button) view.findViewById(R.id.new_note);

        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (note != null) {
                    note.setName(name.getText().toString());
                    note.setDescription(description.getText().toString());
                    note.setDate(new Date());
                    updateNote(note);

                } else {
                    String noteName = name.getText().toString();
                    String noteDescr = description.getText().toString();
                    Date savedDate = new Date();

                    createNoteFor(noteName, noteDescr, savedDate, trip);
                }
                openNotesFragment(v);
            }
        });

        return view;
    }

    private void openNotesFragment(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        NotesFragment fragment = new NotesFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("trip", trip);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createNoteFor(String noteName, String noteDescr, Date savedDate, Trip trip) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final String key = database.child("notes").push().getKey();

        final Note note = new Note(key, trip.getId(), noteName, "sarasa", savedDate, noteDescr);

        database.child("notes").child(key).setValue(note);
    }

    private void updateNote(Note note) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("notes").child(note.getId()).setValue(note);
    }


}
