package com.example.gabriela.firecastcommunity.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gabriela.firecastcommunity.OccurenceAdapter;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.FirecastApi;
import com.example.gabriela.firecastcommunity.data.FirecastClient;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.helper.DistanceCalculator;
import com.example.gabriela.firecastcommunity.helper.MetodsHelpers;
import com.google.android.gms.maps.model.LatLng;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.zbra.androidlinq.Linq.stream;


public class OccurenceFragment extends Fragment {

    List<Occurrence> result = new ArrayList<>();
    private RecyclerView recycler;
    private OccurenceAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    public OccurenceFragment(){}


    @SuppressLint("ValidFragment")
    public OccurenceFragment(List<Occurrence> listOccurenceEnabled){
        this.result = listOccurenceEnabled;
    }

    public static OccurenceFragment newInstance(String param1, String param2) {
        OccurenceFragment fragment = new OccurenceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            LoadingOccurrence();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_occurence, container, false);
        this.recycler = (RecyclerView) view.findViewById(R.id.recyclerViewOccurrence);
        this.recycler.setLayoutManager(new LinearLayoutManager(recycler.getContext()));
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutOccurrence);
        try {
            LoadingOccurrence();
        } finally {
            List<Occurrence> listFilter = ExecuteFilter(result);
            UpdateRecicleViewList(listFilter);
        }

        return view;

    }
    private Occurrence bindLatitudeLongitudeStreet(Occurrence occurrence) {
        return occurrence;
    }

    private Occurrence getLocationFromCity(Occurrence occurrence) {
        if (occurrence.city == null) {
            return occurrence;
        }
        return bindLatitudeLongitudeCity(occurrence);
    }

    private Occurrence bindLatitudeLongitudeCity(Occurrence occurrence) {
        return occurrence;
    }

    private Occurrence getLocationFromAdress(Occurrence occurrence) {
        if (occurrence.adressStreet == null) {
            return getLocationFromCity(occurrence);
        }
        return bindLatitudeLongitudeStreet(occurrence);
    }

    private Occurrence getLocation(Occurrence occurrence) {
        if (occurrence.latitude != null || occurrence.longitude != null) {
            return occurrence;
        }
        return getLocationFromAdress(occurrence);
    }

    private String getCityLocation() {
        return "Florianópolis";
    }

    private void LoadingOccurrence() {
        swipeRefreshLayout.setRefreshing(true);

        try{
            UpdateRecicleViewList(ExecuteFilter(this.result));
        }finally {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private List<Occurrence> ExecuteFilter(List<Occurrence> list) {
        return stream(list)
                .distinct()
                //.where(x -> filterOccurrences.contains(x.type.id))

//                .where(x-> checkBoxRadiusDistance.isChecked()?
//                        (x.distance !=null ?
//                                x.distance <= sb.getProgress()
//                                : true)
//                        : true)
//
//                .where(x-> checkBoxCidade.isChecked()?
//                        (x.city !=null ?
//                                x.city.id == ((City)spCities.getSelectedItem()).id
//                                : true)
//                        : true)

                .orderBy(c -> c.distance == null ? 1000000 : c.distance)
                .thenBy(x -> x.city.name)
                .thenBy(x -> x.date)
                .toList();
    }

    private void UpdateRecicleViewList(List<Occurrence> list){
        adapter = new OccurenceAdapter(getContext(), list);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
    }
}