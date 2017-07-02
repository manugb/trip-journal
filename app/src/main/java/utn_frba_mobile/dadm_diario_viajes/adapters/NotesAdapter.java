package utn_frba_mobile.dadm_diario_viajes.adapters;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.fragments.NoteFragment;
import utn_frba_mobile.dadm_diario_viajes.models.Note;
import utn_frba_mobile.dadm_diario_viajes.storage.ImageLoader;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private List<Note> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView card;
        public TextView name;
        public TextView location;
        public TextView day;
        public TextView month;
        public ImageView photo;
        public TextView comments;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            location = (TextView) v.findViewById(R.id.location);
            day = (TextView) v.findViewById(R.id.day);
            month = (TextView) v.findViewById(R.id.month);
            card = (CardView) v.findViewById(R.id.card);
            photo = (ImageView) v.findViewById(R.id.photo);
            comments = (TextView) v.findViewById(R.id.comments);
        }
    }

    public NotesAdapter(List<Note> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Note note = mDataset.get(position);

        if (note.getImageUrl() != null){
            ImageLoader.instance.loadImage(note.getImageUrl(), holder.photo);
        }

        holder.name.setText(note.getName());
        holder.location.setText(note.getLocation());

        DateFormat dayFormat = new SimpleDateFormat("dd");
        DateFormat monthFormat = new SimpleDateFormat("MMMM");
        holder.day.setText(dayFormat.format(note.getDate()));
        holder.month.setText(monthFormat.format(note.getDate()));
        holder.comments.setText(note.getComments());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note noteSelected = mDataset.get(holder.getAdapterPosition());

                AppCompatActivity activity = scanForActivity(v.getContext());
                NoteFragment fragment = new NoteFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("note", noteSelected);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    private static AppCompatActivity scanForActivity(Context context) {
        if (context == null)
            return null;
        else if (context instanceof AppCompatActivity)
            return (AppCompatActivity) context;
        else if (context instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper)context).getBaseContext());
        return null;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
