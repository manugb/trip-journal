package utn_frba_mobile.dadm_diario_viajes.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import utn_frba_mobile.dadm_diario_viajes.R;

public class NoteFragment extends Fragment {

    TextView textView;
    String note;

    public static NoteFragment newInstance() {
        NoteFragment noteFragment = new NoteFragment();
        return noteFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            note = bundle.getString("name");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        textView = (TextView) view.findViewById(R.id.note_name);
        textView.setText(note);
        return view;
    }

}
