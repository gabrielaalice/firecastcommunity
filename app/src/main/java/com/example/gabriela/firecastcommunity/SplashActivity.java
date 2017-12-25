package com.example.gabriela.firecastcommunity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.data.FirecastDB;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.helper.MetodsHelpers;
import com.example.gabriela.firecastcommunity.utility.Constant;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static br.com.zbra.androidlinq.Linq.stream;

public class SplashActivity extends AppCompatActivity{

    private SeekBar seekbar;
    private AutoCompleteTextView cityAutoComplete;
    private TextView txtSeekBarRadius;
    private Dialog dialog;
    private FirecastDB repository;
    private List<City> cities;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {}
        }, 3000);

        if (!isFirstTime()) {
            FinishSplash();
        }
    }

    private void FinishSplash() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isFirstTime() {
        user = new User();
        repository = new FirecastDB(this);

        boolean firstTime = repository.ListAllUser().size() == 0;

        if (firstTime) {

            dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_filter);
            SetSeekBar();

            cities = stream(DataBaseTemp.cities()).orderBy(x->x.name).toList();
            cities.add(0,new City(Constant.ALL_CITIES_ID,Constant.ALL_CITIES_TEXT));

            SetAutoCompleteCities(cities);

            dialog.show();
        }

        return firstTime;
    }

    public void SetSeekBar(){
        txtSeekBarRadius = dialog.findViewById(R.id.txtSeekBarRadius);
        txtSeekBarRadius.setText("Distância (Raio): " + String.valueOf(10) +" km");

        seekbar = dialog.findViewById(R.id.distance_seekbar);
        seekbar.setMax(100);
        seekbar.setProgress(10);
        seekbar.setOnSeekBarChangeListener(ChangeSeekBar());
    }

    public void SetAutoCompleteCities(List<City> cities){
        cityAutoComplete = dialog.findViewById(R.id.cityAutoComplete);
        ArrayAdapter adapterCities = new AutoCompleteTextViewAdapter(this,android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, stream(cities).select(x->x.name).toList());
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

    public boolean setInitialPreferences(View v){

        String cityName = cityAutoComplete.getText().toString();
        City city = null;

        if(cityName.trim().equalsIgnoreCase("")) {
            city = new City(Constant.NOTHING_CITY_ID,"");
        }else {
            if (cityName.trim().equalsIgnoreCase(Constant.ALL_CITIES_TEXT)) {
                city = new City(Constant.ALL_CITIES_ID, Constant.ALL_CITIES_TEXT);
            } else {
                city = getCityFromName(cityName);
                if (city == null) {
                    Toast.makeText(this, "Cidade inválida", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

            user.setId_city_occurrence(city.id);

            user.setRadiusKilometers(seekbar.getProgress());
            user.setOccurrenceTypes(DataBaseTemp.typesOccurrences());

            user.setNotify(true);
            user.setSound(true);
            user.setVibrate(true);

            repository.SaveOrUpdate(user);

            FinishSplash();

            return true;
    }

    public SeekBar.OnSeekBarChangeListener ChangeSeekBar() {
        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                txtSeekBarRadius.setText("Distância (Raio): " + String.valueOf(progress) +" km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };
    }

//    private void SaveCities(List<City> cities) {
//        Repository<City> cityRepository = new Repository(this);
//
//        for (City c :cities) {
//            cityRepository.InsertSave(c);
//        }
//    }
//
//    private void SaveRadiosCities(List<Pair<String,String>> radiosCities) {
//        radioCityRepository = new Repository(this);
//
//        RadioCity radioCity;
//
//        for (Pair<String,String> r :radiosCities) {
//            radioCity = new RadioCity(r.first, getCityFromName(r.second));
//            radioCityRepository.SaveOrUpdate(radioCity);
//        }
//    }
//
//    private RadioCity getCityFromId(int id_city) {
//        List<RadioCity> radioCities = new Repository(this).ListAll(new RadioCity());
//        return stream(radioCities).first(x->x.city.getId() == id_city);
//    }

    private City getCityFromName(String name) {
        return stream(cities).firstOrNull(x-> MetodsHelpers.normalizeString(x.name)
                            .equalsIgnoreCase(MetodsHelpers.normalizeString(name)));
    }
}