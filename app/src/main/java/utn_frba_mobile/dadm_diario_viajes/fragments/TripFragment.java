package utn_frba_mobile.dadm_diario_viajes.fragments;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.joda.time.LocalDate;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.activities.MainActivity;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;
import utn_frba_mobile.dadm_diario_viajes.models.User;
import utn_frba_mobile.dadm_diario_viajes.storage.ImageLoader;

import static android.app.Activity.RESULT_OK;

public class TripFragment extends Fragment {

    private EditText title;
    private ImageView photo;
    private Button btnPortada;
    private Button btnNewTrip;
    private EditText initDateText;
    private static int RESULT_LOAD_IMG = 1;
    private String photoPath;
    private String photoUrlDefault = "https://firebasestorage.googleapis.com/v0/b/dadm-diario-viajes.appspot.com/o/images%2Ftrips%2Ftripdefault.jpg?alt=media&token=65e3621c-8f72-4dc5-a273-5e0a69e08bb0";
    private DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private DatePickerDialog initDatePickerDialog;

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

        ImageLoader.instance.loadImage(photoUrlDefault, photo);

        btnPortada = (Button) v.findViewById(R.id.portada);
        btnNewTrip = (Button) v.findViewById(R.id.new_trip);

        initDateText = (EditText) v.findViewById(R.id.initDate_text);
        initDateText.setInputType(InputType.TYPE_NULL);

        setDateTimeField(v);

        initDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePickerDialog.show();
            }
        });

        btnPortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
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
                createTripFor(currentUser, titleTrip, date, date, photoPath);
                openTripsFragment(v);
            }
        });

        return v;
    }

    private void setDateTimeField(View v) {
        Calendar newDate = Calendar.getInstance();

        initDatePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                initDateText.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH));

    }

    private void openTripsFragment(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        TripsFragment fragment = new TripsFragment();
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void startGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            photoPath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            photo.setImageBitmap(bitmap);
        }
    }


    Trip createTripFor(User currentUser, String title, Date inicio, Date fin, String photoPath) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final String key = database.child("trips").push().getKey();
        final Trip trip = new Trip(key, currentUser.getId(), title, inicio, fin);

        if (photoPath != null) {
            StorageReference storage = FirebaseStorage.getInstance().getReference();

            Uri file = Uri.fromFile(new File(photoPath));
            StorageReference imagen = storage.child("images").child("trips").child(file.getLastPathSegment());
            UploadTask uploadTask = imagen.putFile(file);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    trip.setPhotoUrl(downloadUrl.toString());
                    database.child("trips").child(key).setValue(trip);
                }
            });
        } else {
            trip.setPhotoUrl(photoUrlDefault);
            database.child("trips").child(key).setValue(trip);
        }

        return trip;
    }

}
