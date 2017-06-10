package utn_frba_mobile.dadm_diario_viajes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.adapters.TripsAdapter;
import utn_frba_mobile.dadm_diario_viajes.models.Trip;

public class TripFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static TripFragment newInstance(){
        TripFragment tripFragment = new TripFragment();
        return tripFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }

}
