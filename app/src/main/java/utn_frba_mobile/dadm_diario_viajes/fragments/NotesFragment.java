package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import utn_frba_mobile.dadm_diario_viajes.Manifest;
import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.adapters.NotesAdapter;
import utn_frba_mobile.dadm_diario_viajes.models.Note;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;
import utn_frba_mobile.dadm_diario_viajes.models.User;
import utn_frba_mobile.dadm_diario_viajes.storage.ImageLoader;

public class NotesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Activity activity;
    private RecyclerView.LayoutManager mLayoutManager;
    private Trip trip;
    private ArrayList<Note> notes = new ArrayList<>();
    private TextView tripName;
    private ImageView tripPhoto;
    private ImageButton editButton;

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
        // specify an adapter (see also next example)

        notes = new ArrayList<>();
        mAdapter = new NotesAdapter(notes);
        findNotesOf(trip);
    }

    private void findNotesOf(Trip trip) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("notes").orderByChild("tripId").equalTo(trip.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    notes.add(child.getValue(Note.class));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        tripName = (TextView) view.findViewById(R.id.name_trip);
        tripName.setText(trip.getName());
        tripPhoto = (ImageView) view.findViewById(R.id.photo_trip);
        ImageLoader.instance.loadImage(trip.getPhotoUrl(), tripPhoto);
        editButton = (ImageButton) view.findViewById(R.id.edit_btn);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.notes_recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                TripFragment fragment = TripFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putSerializable("trip", trip);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

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

    public Trip getTrip(){
        return trip;
    }

}
