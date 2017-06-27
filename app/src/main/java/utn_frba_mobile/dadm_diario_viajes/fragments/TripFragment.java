package utn_frba_mobile.dadm_diario_viajes.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Date;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.activities.MainActivity;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;
import utn_frba_mobile.dadm_diario_viajes.models.User;

import static android.app.Activity.RESULT_OK;

public class TripFragment extends Fragment {

    private EditText title;
    private ImageView photo;
    private Button btnPortada;
    private Button btnNewTrip;
    private static int RESULT_LOAD_IMG = 1;

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
        btnPortada = (Button) v.findViewById(R.id.portada);
        btnNewTrip = (Button) v.findViewById(R.id.new_trip);

        btnPortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                } else {
                    startGallery();
                }
            }
        });

        btnNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleTrip = title.getText().toString();
                User currentUser = ((MainActivity) getActivity()).getLoggedUser();
                Date date = new Date();
                createTripFor(currentUser, titleTrip, date, date, (int) photo.getTag());
            }
        });

        return v;
    }

    private void startGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            photo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            //TODO ver storage y dnde guardar la foto del telefono

        }
    }


    private Trip createTripFor(User currentUser, String title, Date inicio, Date fin, int photo) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String key = database.child("trips").push().getKey();
        Trip trip = new Trip(key, currentUser.getId(), title, inicio, fin, photo);
        database.child("trips").child(key).setValue(trip);
        return trip;
    }

}
