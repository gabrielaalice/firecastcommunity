package com.example.gabriela.firecastcommunity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.data.FirecastDB;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.RadioCity;
import com.example.gabriela.firecastcommunity.domain.User;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static br.com.zbra.androidlinq.Linq.stream;

public class SplashActivity extends AppCompatActivity{

    private SeekBar seekbar;
    private Spinner spinner;
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

            //Create Dialog
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_filter);
            SetSeekBar();

            cities = DataBaseTemp.cities();
            SetSpinnerCities(cities);

            //show dialog if app never launch
            dialog.show();

            //SaveCities(cities);

            //radiosCities = DataBaseTemp.radios();
            //SaveRadiosCities(radiosCities);
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

    public void SetSpinnerCities(List<City> cities){
        spinner = dialog.findViewById(R.id.city_spinner);
        ArrayAdapter adapterCities = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, cities);
        spinner.setAdapter(adapterCities);
        spinner.setVisibility(View.VISIBLE);
    }

    public void setInitialPreferences(View v){
        //user.setId(1);
        user.setId_city_occurrence(((City)spinner.getSelectedItem()).id);
        //user.setRadio_city(getCityFromId(user.getCity_occurrence().getId()));
        user.setRadiusKilometers(seekbar.getProgress());
        user.setOccurrenceTypes(DataBaseTemp.typesOccurrences());

        repository.InsertUser(user);

        FinishSplash();
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
        return stream(cities).first(x->x.name == name);
    }
}