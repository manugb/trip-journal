package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.models.Note;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;
import utn_frba_mobile.dadm_diario_viajes.storage.ImageLoader;

import static android.app.Activity.RESULT_OK;

public class NoteFragment extends Fragment {

    private EditText name;
    private EditText location;
    private EditText dateText;
    private EditText comments;
    private ImageView photo;
    private ImageButton btnAddPhoto;
    private ImageButton btnTakePhoto;
    private Button btnNewNote;
    private String photoPath;


    private Date date = new Date();
    private DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private DatePickerDialog datePickerDialog;

    private Note note;
    private Trip trip;

    private static int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

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

            } else if (bundle.containsKey("trip")) {
                trip = (Trip) bundle.getSerializable("trip");
            }
        }
    }

    private void getTripByNote(Note note) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("trips").orderByChild("id").equalTo(note.getTripId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
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
        location = (EditText) view.findViewById(R.id.location);
        dateText = (EditText) view.findViewById(R.id.dateText);
        dateText.setInputType(InputType.TYPE_NULL);
        dateText.setText(dateFormatter.format(new Date()));

        comments = (EditText) view.findViewById(R.id.comments);
        photo = (ImageView) view.findViewById(R.id.photo);

        btnTakePhoto = (ImageButton) view.findViewById(R.id.take_photo);
        btnAddPhoto = (ImageButton) view.findViewById(R.id.add_photo);
        btnNewNote = (Button) view.findViewById(R.id.new_note);

        if (note != null) {
            name.setText(note.getName());
            location.setText(note.getLocation());
            dateText.setText(dateFormatter.format(note.getDate()));
            comments.setText(note.getComments());
            if (note.getImageUrl() != null) {
                ImageLoader.instance.loadImage(note.getImageUrl(), photo);
            }
        }

        setDateTimeField(view);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
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

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (note != null) {
                    note.setName(name.getText().toString());
                    note.setLocation(location.getText().toString());
                    note.setDate(date);
                    note.setComments(comments.getText().toString());
                    if (photoPath != null) {
                        note.setImageUrl(photoPath);
                    }
                    updateNote(note);

                } else {
                    String noteName = name.getText().toString();
                    String noteDescr = comments.getText().toString();
                    String noteLocation = location.getText().toString();
                    Date savedDate = new Date();
                    createNoteFor(noteName, noteDescr, savedDate, noteLocation, trip);
                }
                openNotesFragment(v);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            photoPath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            photo.setImageBitmap(bitmap);
        }

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);

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
        if (photoPath != null) {
            StorageReference storage = FirebaseStorage.getInstance().getReference();

            Uri file = Uri.fromFile(new File(photoPath));
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
            database.child("notes").child(key).setValue(note);
        }
    }

    private void updateNote(final Note note) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        if (photoPath != null) {
            StorageReference storage = FirebaseStorage.getInstance().getReference();

            Uri file = Uri.fromFile(new File(photoPath));
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

    private void setDateTimeField(View v) {
        datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.setDate(dayOfMonth);
                date.setMonth(monthOfYear);
                date.setYear(year);
                dateText.setText(dateFormatter.format(date));
            }
        }, date.getYear(), date.getMonth(), date.getDay());
    }

}
