package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.models.Note;

public class NoteFragment extends Fragment {

    TextView textView;
    Note note;

    public static NoteFragment newInstance() {
        NoteFragment noteFragment = new NoteFragment();
        return noteFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            note = (Note)bundle.getSerializable("note");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        textView = (TextView) view.findViewById(R.id.name);
        textView.setText(note.getName());

        textView = (TextView) view.findViewById(R.id.date);
        textView.setText(note.getDate().toString());

        textView = (TextView) view.findViewById(R.id.description);
        textView.setText(note.getDescription());

        return view;
    }

}
