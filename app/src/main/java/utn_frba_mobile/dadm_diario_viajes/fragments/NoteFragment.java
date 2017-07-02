package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.Manifest;
import android.app.Fragment;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.models.Note;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;
import utn_frba_mobile.dadm_diario_viajes.storage.ImageLoader;

import static android.app.Activity.RESULT_OK;

//TODO: Para todas las referencias a Date, deberia haber usado calendar, pero no le dedique tiempo a entenderlo
public class NoteFragment extends Fragment {

    private EditText name;
    private DatePicker datePicker;
    private EditText description;
    private EditText location;
    private ImageView mainPic;
    private Button btnNewNote;
    private Button btnMainPic;

    private String mainPicPath;
    private String picUrlDefault = "https://firebasestorage.googleapis.com/v0/b/dadm-diario-viajes.appspot.com/o/images%2Ftrips%2Ftripdefault.jpg?alt=media&token=65e3621c-8f72-4dc5-a273-5e0a69e08bb0";


    private Note note;
    private Trip trip;

    private static int RESULT_LOAD_IMG = 1;

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
        datePicker = (DatePicker) view.findViewById(R.id.date);
        description = (EditText) view.findViewById(R.id.description);
        location = (EditText) view.findViewById(R.id.location);
        mainPic = (ImageView) view.findViewById(R.id.main_pic);
        btnNewNote = (Button) view.findViewById(R.id.new_note);
        btnMainPic = (Button) view.findViewById(R.id.change_main_pic);

        if (note != null) {
            name.setText(note.getName());

            Date date = note.getDate();
            datePicker.init(date.getYear(), date.getMonth(), date.getDay(), null);

            description.setText(note.getDescription());
            location.setText(note.getLocation());
            ImageLoader.instance.loadImage(note.getImageUrl(), mainPic);
        } else {
            ImageLoader.instance.loadImage(picUrlDefault, mainPic);
        }


        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (note != null) {
                    note.setName(name.getText().toString());
                    note.setDescription(description.getText().toString());
                    note.setDate(new Date(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                    updateNote(note);

                } else {
                    String noteName = name.getText().toString();
                    String noteDescr = description.getText().toString();
                    String noteLocation = location.getText().toString();

                    Date savedDate = new Date(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());

                    createNoteFor(noteName, noteDescr, savedDate, noteLocation, trip);
                }
                openNotesFragment(v);
            }
        });

        btnMainPic.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mainPicPath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(mainPicPath);
            mainPic.setImageBitmap(bitmap);
        }
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

    private void createNoteFor(String noteName, String noteDescr, Date savedDate, String location, Trip trip) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final String key = database.child("notes").push().getKey();
        final String picPath;
        final Note note;

        note = new Note(key, trip.getId(), noteName, location, savedDate, noteDescr);
        if (mainPicPath != null) {
            StorageReference storage = FirebaseStorage.getInstance().getReference();

            Uri file = Uri.fromFile(new File(mainPicPath));
            StorageReference imagen = storage.child("images").child("trips").child(file.getLastPathSegment());
            UploadTask uploadTask = imagen.putFile(file);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    note.setImageUrl(downloadUrl.toString());
                    database.child("notes").child(key).setValue(note);
                }
            });
        } else {
            note.setImageUrl(picUrlDefault);
            database.child("notes").child(key).setValue(note);
        }
    }

    private void updateNote(final Note note) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        if (mainPicPath != null) {
            StorageReference storage = FirebaseStorage.getInstance().getReference();

            Uri file = Uri.fromFile(new File(mainPicPath));
            StorageReference imagen = storage.child("images").child("trips").child(file.getLastPathSegment());
            UploadTask uploadTask = imagen.putFile(file);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    note.setImageUrl(downloadUrl.toString());
                    database.child("notes").child(note.getId()).setValue(note);
                }
            });
        } else {
            database.child("notes").child(note.getId()).setValue(note);
        }
    }

    private void startGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

}
