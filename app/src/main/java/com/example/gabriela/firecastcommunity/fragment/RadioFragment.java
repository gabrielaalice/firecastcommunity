package com.example.gabriela.firecastcommunity.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gabriela.firecastcommunity.AutoCompleteTextViewAdapter;
import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.RadioCity;
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.helper.MetodsHelpers;
import com.example.gabriela.firecastcommunity.helper.RadioOnlineStreamHelpers;

import static br.com.zbra.androidlinq.Linq.stream;

import java.util.ArrayList;
import java.util.List;


public class RadioFragment extends Fragment {
    private ImageButton play_pause_btn;
    private Spinner radio_type_spinner;
    private AutoCompleteTextView cityAutoComplete;
    private City city;
    private User user;


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

        user = MainActivity.getUser();
        city = getCityFromId(user.getId_city_occurrence());

        SetAutoCompleteCities(view);
        SetSpinnerTypesRadio(view);

        play_pause_btn= view.findViewById(R.id.play_pause_btn);
        play_pause_btn.setImageResource(R.drawable.play_btn);

        LoadingRadio(view);

        return view;
    }

    private void SetSpinnerTypesRadio(View view) {
            radio_type_spinner = view.findViewById(R.id.radio_type_spinner);
            String[] typesCities = new String[]{"Interno", "Externo"};
            ArrayAdapter adapterCities = new ArrayAdapter(view.getContext(),android.R.layout.simple_spinner_dropdown_item, typesCities);
            radio_type_spinner.setAdapter(adapterCities);
            radio_type_spinner.setVisibility(View.VISIBLE);

            radio_type_spinner.setSelection(ElementInPositionSpinner(typesCities, city.name));
    }

    public void SetAutoCompleteCities(View view) {
        List<String> cities = stream(DataBaseTemp.radios()).orderBy(x->x.cityName).select(x -> x.cityName).toList();
        cityAutoComplete = view.findViewById(R.id.cityAutoComplete);

        if (cities.contains(city.name)){
            cityAutoComplete.setText(city.name);
        }

        ArrayAdapter adapterCities = new AutoCompleteTextViewAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, cities);
        cityAutoComplete.setAdapter(adapterCities);
        cityAutoComplete.setVisibility(View.VISIBLE);
        cityAutoComplete.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                cityAutoComplete.showDropDown();
                cityAutoComplete.requestFocus();

                return false;
            }
        });
    }

    private void LoadingRadio(View view) {
        new RadioOnlineStreamHelpers(view, play_pause_btn, "Lages", "Interno");
    }

    private City getCityFromId(int id_city) {
        return stream(DataBaseTemp.cities()).firstOrNull(x-> x.id == id_city);
    }

    public static int ElementInPositionSpinner(String[] list, String city_name) {
        int position = 0;
        int i = 0;
        for (String obj : list) {
            if (obj == city_name) {
                return i;
            }
            i++;
        }
        return position;
    }

    public static String UpdateRadio(View view) {
        String cityName = MetodsHelpers.normalizeString(((AutoCompleteTextView)view.findViewById(R.id.cityAutoComplete)).getText().toString());

        if(cityName == ""){
            return null;
        }

        String typeCity = ((Spinner)view.findViewById(R.id.radio_type_spinner)).getSelectedItem().toString();

        return stream(DataBaseTemp.radios())
                .where(x->x.type_radio.equalsIgnoreCase(typeCity) &&
                        MetodsHelpers.normalizeString(x.cityName).equalsIgnoreCase(cityName))
                .select(x->x.urlRadio).firstOrNull();
    }
}
