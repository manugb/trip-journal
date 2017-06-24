package utn_frba_mobile.dadm_diario_viajes.adapters;

import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.fragments.NotesFragment;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private List<Trip> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView name;
        public ImageView photo;

        public ViewHolder(View v) {
            super(v);
            card = ((CardView) itemView.findViewById(R.id.trip_card));
            name = (TextView) v.findViewById(R.id.name);
            photo = (ImageView) v.findViewById(R.id.photo);
        }
    }

    public TripsAdapter(List<Trip> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Trip trip = mDataset.get(position);
        holder.name.setText(trip.getName());
        holder.photo.setImageResource(trip.getPhoto());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Trip tripSelected = mDataset.get(holder.getAdapterPosition());

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                NotesFragment fragment = new NotesFragment();


                Bundle bundle = new Bundle();
                bundle.putSerializable("trip",tripSelected);
                fragment.setArguments(bundle);

      
                FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    };

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}





















