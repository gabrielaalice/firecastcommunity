package com.example.gabriela.firecastcommunity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.domain.City;
import static br.com.zbra.androidlinq.Linq.stream;

import java.util.ArrayList;
import java.util.List;


public class RadioFragment extends Fragment {
    private ImageButton play_pause_btn;
    private Spinner citySpinner, typeRadioSpinner;
    ArrayAdapter adapterCities;
    DataBaseTemp bd;
    boolean paused = true;
    private List<String> types = new ArrayList<String>();
    private String type;


    public RadioFragment() {
        // Required empty public constructor
    }


    public static RadioFragment newInstance(String param1, String param2) {
        RadioFragment fragment = new RadioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_radio, container, false);
//        play.setEnabled(false);
  //      play.setText("Carregando...");
        LoadingRadio();
       // bd = new DataBaseTemp();
        List<City> cities = DataBaseTemp.cities();
        ArrayAdapter adapterCities = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item, cities);

        citySpinner = view.findViewById(R.id.city_spinner);
        citySpinner.setAdapter(adapterCities);
        citySpinner.setVisibility(View.VISIBLE);

        play_pause_btn= (ImageButton) view.findViewById(R.id.play_pause_btn);
        play_pause_btn.setImageResource(R.drawable.play_btn);
        play_pause_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                paused = !paused;
                if(paused){
                    play_pause_btn.setImageResource(R.drawable.pause_btn);
                }
                else{
                    play_pause_btn.setImageResource(R.drawable.play_btn);
                }
            }
        });



        return view;
    }
    private void LoadingRadio() {
       // new RadioOnlineStreamHelpers(play, getCityLocation());
    }
    private String getCityLocation() {
        return "Florianópolis";
    }



 }
