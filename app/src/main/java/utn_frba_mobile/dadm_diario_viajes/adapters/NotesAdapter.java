package utn_frba_mobile.dadm_diario_viajes.adapters;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.fragments.NoteFragment;
import utn_frba_mobile.dadm_diario_viajes.models.Note;

/**
 * Created by manu on 07/05/17.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private List<Note> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView card;
        public TextView name;
        public TextView location;
        public TextView date;
        public Button delete;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            location = (TextView) v.findViewById(R.id.location);
            date = (TextView) v.findViewById(R.id.date);
            card = (CardView) v.findViewById(R.id.card);
            delete = (Button) v.findViewById(R.id.delete_note);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotesAdapter(List<Note> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // ...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Note note = mDataset.get(position);

        holder.name.setText(note.getName());
        holder.location.setText(note.getLocation());

        DateFormat format = SimpleDateFormat.getDateInstance();
        holder.date.setText(format.format(note.getDate()));

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                NoteFragment fragment = new NoteFragment();

                Bundle bundle = new Bundle();
                bundle.putString("name", note.getName());
                fragment.setArguments(bundle);

                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset.remove(note);
                notifyDataSetChanged();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
