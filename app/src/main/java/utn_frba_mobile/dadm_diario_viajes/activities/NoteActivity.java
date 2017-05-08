package utn_frba_mobile.dadm_diario_viajes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import utn_frba_mobile.dadm_diario_viajes.R;

public class NoteActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intent = getIntent();
        String noteName = intent.getStringExtra(Intent.EXTRA_TEXT);

        textView = (TextView) findViewById(R.id.note_name);
        textView.setText(noteName);
    }
}
