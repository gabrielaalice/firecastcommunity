package com.example.gabriela.firecastcommunity.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.OccurenceAdapter;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.data.FirecastApi;
import com.example.gabriela.firecastcommunity.data.FirecastClient;
import com.example.gabriela.firecastcommunity.data.FirecastDB;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.helper.DistanceCalculator;
import com.google.android.gms.maps.model.LatLng;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static br.com.zbra.androidlinq.Linq.stream;
import static com.facebook.FacebookSdk.getApplicationContext;


public class OccurenceFragment extends Fragment {

    static final List<Occurrence> result = new ArrayList<>();
    private static RecyclerView recycler;
    private static OccurenceAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public OccurenceFragment(){}

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
        this.swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        try {
            LoadingOccurrence();
        } finally {
            UpdateRecicleViewList(getActivity().getApplicationContext());
        }

        return view;

    }
    private static Occurrence bindLatitudeLongitudeStreet(Occurrence occurrence) {
        return occurrence;
    }

    private static Occurrence getLocationFromCity(Occurrence occurrence) {
        if (occurrence.city == null) {
            return occurrence;
        }
        return bindLatitudeLongitudeCity(occurrence);
    }

    private static Occurrence bindLatitudeLongitudeCity(Occurrence occurrence) {
        return occurrence;
    }

    private static Occurrence getLocationFromAdress(Occurrence occurrence) {
        if (occurrence.adressStreet == null) {
            return getLocationFromCity(occurrence);
        }
        return bindLatitudeLongitudeStreet(occurrence);
    }

    private static Occurrence getLocation(Occurrence occurrence) {
        if (occurrence.latitude != null || occurrence.longitude != null) {
            return occurrence;
        }
        return getLocationFromAdress(occurrence);
    }

    private static String getCityLocation() {
        return "Florianópolis";
    }

    private void LoadingOccurrence() {
        swipeRefreshLayout.setRefreshing(true);

        try{
            UpdateRecicleViewList(getActivity().getApplicationContext());
        }finally {
            MapsFragment.UpdateMapMarkersRadius();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private static List<Occurrence> ExecuteFilter() {
        List<Occurrence> list;
        User user = MainActivity.getUser();

        List<Integer> typesOccurrences = stream(user.getOccurrenceTypes()).select(y->y.id).toList();
        int radius = user.getRadiusKilometers();
        int id_city = user.getId_city_occurrence();

        if(typesOccurrences.size() > 0){
            list = stream(result)
                    .distinct()
                    .where(x -> typesOccurrences.contains(x.type.id))
                    .where(x-> ((x.distance !=null ?
                                    x.distance <= radius
                                    : x.city.id == id_city)
                            || x.city.id == id_city))
                    .orderBy(c -> c.distance == null ? 1000000 : c.distance)
                    .thenBy(x -> x.city.name)
                    .thenBy(x -> x.date)
                    .toList();
        }else{
            list = stream(result)
                    .distinct()
                    .where(x-> ((x.distance !=null ?
                            x.distance <= radius
                            : x.city.id == id_city)
                            || x.city.id == id_city))
                    .orderBy(c -> c.distance == null ? 1000000 : c.distance)
                    .thenBy(x -> x.city.name)
                    .thenBy(x -> x.date)
                    .toList();
        }
        return list;
    }

    public static void UpdateRecicleViewList(Context context){
        if(recycler!=null) {
            adapter = new OccurenceAdapter(context, ExecuteFilter());
            adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
            recycler.setLayoutManager(new LinearLayoutManager(context));
            recycler.setAdapter(adapter);
        }
    }



    public static void callApiGetAllOccurrence(LatLng actualPosition) {
        result.removeAll(result);
        FirecastClient fire = new FirecastClient();
        FirecastApi api = fire.retrofit.create(FirecastApi.class);
        List<City> listCities = DataBaseTemp.cities();

        for (City cidade : listCities) {
            api.getOccurrences(cidade.name)
                    .enqueue(new Callback<List<Occurrence>>() {

                        public void onResponse(Call<List<Occurrence>> call, Response<List<Occurrence>> response) {

                            List<Occurrence> list = response.body();
                            if (list != null) {
                                List<Integer> listIds = stream(result).select(c -> c.id).toList();
                                List<Occurrence> listList = stream(list).where(c -> !listIds.contains(c.id)).toList();

                                for (Occurrence occ : listList) {
                                    Double distance = new DistanceCalculator()
                                            .distancia(actualPosition, getLocation(occ));
                                    if (distance == 0 || distance < 0) {
                                        occ.distance = null;
                                    } else {
                                        occ.distance = distance / 1000;
                                    }
                                }

                                result.addAll(listList);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Occurrence>> call, Throwable t) {

                        }
                    });
        }
    }

    public static List<Occurrence> getListOccurrence(){
        return ExecuteFilter();
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            LoadingOccurrence();
        }
    };

}