package utn_frba_mobile.dadm_diario_viajes.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.activities.MainActivity;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;
import utn_frba_mobile.dadm_diario_viajes.models.User;

public class TripFragment extends Fragment {

    private EditText title;
    private ImageView photo;
    private Button btn;

    public static TripFragment newInstance() {
        TripFragment tripFragment = new TripFragment();
        return tripFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trip, container, false);

        title = (EditText) v.findViewById(R.id.title);
        photo = (ImageView) v.findViewById(R.id.photo);
        photo.setImageResource(R.drawable.tripdefault);
        photo.setTag(R.drawable.tripdefault);
        btn = (Button) v.findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleTrip = title.getText().toString();
                User currentUser = ((MainActivity)getActivity()).getLoggedUser();
                Date date = new Date();
                createTripFor(currentUser,titleTrip,date,date,(int) photo.getTag());
            }
        });

        return v;
    }


    private Trip createTripFor(User currentUser, String title, Date inicio, Date fin, int photo) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String key = database.child("trips").push().getKey();
        Trip trip = new Trip(key, currentUser.getId() , title, inicio, fin, photo);
        database.child("trips").child(key).setValue(trip);
        return trip;
    }

}
