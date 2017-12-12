package com.example.gabriela.firecastcommunity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity{

    private SeekBar seekbar;
    private Spinner spinner;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView txtSeekBarRadius;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getPreferences(MODE_PRIVATE);
        editor = preferences.edit();

        //method call
        if(!isFirstTime()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    FinishSplash();
                }
            }, 3000);
        }
    }

    private void FinishSplash() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isFirstTime() {
        boolean firstTime = preferences.getBoolean("isFirstTime", true);

        if (firstTime) {

            //Create Dialog
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_filter);
            SetSeekBar();
            SetSpinnerCities();

            //show dialog if app never launch
            dialog.show();
        }

        return firstTime;
    }

    public void SetSeekBar(){
        txtSeekBarRadius = (TextView) dialog.findViewById(R.id.txtSeekBarRadius);
        txtSeekBarRadius.setText("Distância (Raio): " + String.valueOf(10) +" km");

        seekbar = (SeekBar) dialog.findViewById(R.id.distance_seekbar);
        seekbar.setMax(100);
        seekbar.setProgress(10);
        seekbar.setOnSeekBarChangeListener(ChangeSeekBar());
    }

    public void SetSpinnerCities(){
        spinner = (Spinner) dialog.findViewById(R.id.city_spinner);
        List<City> cities = DataBaseTemp.cities();
        ArrayAdapter adapterCities = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, cities);
        spinner.setAdapter(adapterCities);
        spinner.setVisibility(View.VISIBLE);
    }

    public void setInitialPreferences(View v){
        editor.putInt("CityDefault", ((City)spinner.getSelectedItem()).id);
        editor.putInt("RadiusDefault", seekbar.getProgress());
        editor.putBoolean("isFirstTime", false);

        List<OccurrenceType> arrayOT = DataBaseTemp.typesOccurrences();
        for (OccurrenceType ot: arrayOT){
            editor.putBoolean(ot.name,true);
        }

        editor.commit();

        FinishSplash();
    }

    public static int ElementInPositionSpinner(List<City> lista, City objetoSalvo) {
        int posicao = -1;
        int i = 0;
        for (City objeto : lista) {
            if (objeto.id == objetoSalvo.id) {
                posicao = i;
                break;
            }
            i++;
        }
        return posicao;
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

}