package com.example.gabriela.firecastcommunity.drawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.data.FirecastDB;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.domain.User;

import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

public class FilterOcActivity extends AppCompatActivity {

    private SeekBar seekbar;
    private Spinner spinner;
    private TextView txtSeekBarRadius;
    private CheckBox oc_accident_car;
    private CheckBox oc_paramedics;
    private CheckBox oc_support;
    private CheckBox oc_tree_cutting;
    private CheckBox oc_insect;
    private CheckBox oc_action_preventive;
    private CheckBox oc_other;
    private CheckBox oc_fire;
    private CheckBox oc_nao_atendida;
    private CheckBox oc_dangerous_product;
    private CheckBox oc_search_rescue;
    private User user;
    private FirecastDB repository;
    private List<Integer> typesSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_oc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //button back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        repository =new FirecastDB(this);
        user = repository.ListAllUser().get(0);

        SetSeekBar();
        SetSpinnerCities();
        SetCheckBoxesOccurrenceTypes();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_occurence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            SaveChanges();
            Intent i = new Intent(this, MainActivity.class);
            setResult(Activity.RESULT_OK, i);
            finish();
            return true;
        }
        if( id == android.R.id.home){
            Intent i = new Intent(this, MainActivity.class);
            setResult(Activity.RESULT_CANCELED, i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void SetSeekBar(){
        seekbar = (SeekBar) findViewById(R.id.distance_seekbar);
        seekbar.setMax(100);

        int radius = user.getRadiusKilometers();

        seekbar.setProgress(radius);
        seekbar.setOnSeekBarChangeListener(ChangeSeekBar());

        txtSeekBarRadius = (TextView) findViewById(R.id.txtSeekBar);
        txtSeekBarRadius.setText("Distância (Raio): " + String.valueOf(seekbar.getProgress()) +" km");
    }

    public void SetSpinnerCities(){
        spinner = (Spinner) findViewById(R.id.city_spinner);
        List<City> cities = DataBaseTemp.cities();
        ArrayAdapter adapterCities = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, cities);
        spinner.setAdapter(adapterCities);
        spinner.setVisibility(View.VISIBLE);

        int city = user.getId_city_occurrence();
        spinner.setSelection(ElementInPositionSpinner(cities, city));
    }

    private void SetCheckBoxesOccurrenceTypes() {
        typesSave = stream(user.getOccurrenceTypes()).select(x->x.id).toList();
        oc_accident_car = findViewById(R.id.oc_accident_car);
        oc_accident_car.setChecked(typesSave.contains(DataBaseTemp.ID_ACIDENT));

        oc_paramedics = findViewById(R.id.oc_paramedics);
        oc_paramedics.setChecked(typesSave.contains(DataBaseTemp.ID_PARAMEDICS));

        oc_support = findViewById(R.id.oc_support);
        oc_support.setChecked(typesSave.contains(DataBaseTemp.ID_SUPORT));

        oc_tree_cutting = findViewById(R.id.oc_tree_cutting);
        oc_tree_cutting.setChecked(typesSave.contains(DataBaseTemp.ID_CUTTING_TREE));

        oc_insect = findViewById(R.id.oc_insect);
        oc_insect.setChecked(typesSave.contains(DataBaseTemp.ID_INSECT));

        oc_action_preventive = findViewById(R.id.oc_action_preventive);
        oc_action_preventive.setChecked(typesSave.contains(DataBaseTemp.ID_PREVENTIVE));

        oc_other = findViewById(R.id.oc_other);
        oc_other.setChecked(typesSave.contains(DataBaseTemp.ID_OTHERS));

        oc_fire = findViewById(R.id.oc_fire);
        oc_fire.setChecked(typesSave.contains(DataBaseTemp.ID_FIRE));

        oc_nao_atendida = findViewById(R.id.oc_nao_atendida);
        oc_nao_atendida.setChecked(typesSave.contains(DataBaseTemp.ID_NOT_SERVICE));

        oc_dangerous_product = findViewById(R.id.oc_dangerous_product);
        oc_dangerous_product.setChecked(typesSave.contains(DataBaseTemp.ID_DANGEROUS));

        oc_search_rescue = findViewById(R.id.oc_search_rescue);
        oc_search_rescue.setChecked(typesSave.contains(DataBaseTemp.ID_RESCUES));
    }

    public static int ElementInPositionSpinner(List<City> list, int city_id) {
        int position = -1;
        int i = 0;
        for (City obj : list) {
            if (obj.id == city_id) {
                position = i;
                break;
            }
            i++;
        }
        return position;
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



    private void SaveChanges() {
        user.setId_city_occurrence(((City)spinner.getSelectedItem()).id);
        user.setRadiusKilometers(seekbar.getProgress());

        List<OccurrenceType> types = DataBaseTemp.typesOccurrences();
        types = DeleteTypeOnList(types,DataBaseTemp.ID_ACIDENT,oc_accident_car.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_PARAMEDICS,oc_paramedics.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_SUPORT,oc_support.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_CUTTING_TREE,oc_tree_cutting.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_INSECT,oc_insect.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_PREVENTIVE,oc_action_preventive.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_OTHERS,oc_other.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_FIRE,oc_fire.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_NOT_SERVICE,oc_nao_atendida.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_DANGEROUS,oc_dangerous_product.isChecked());
        types = DeleteTypeOnList(types,DataBaseTemp.ID_RESCUES,oc_search_rescue.isChecked());

        repository.Delete_List_User_OccurrenceType(user);

        user.setOccurrenceTypes(types);

        repository.UpdateUser(user);
    }

    private List<OccurrenceType> DeleteTypeOnList(List<OccurrenceType> types, int id_type, boolean condition) {
        if(!condition) {
            return stream(types).where(x -> x.id != id_type).toList();
        }
        return types;
    }
}
