package com.example.gabriela.firecastcommunity.drawer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.data.FirecastDB;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.utility.Constant;

import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

public class OccurenceTypeUserActivity extends AppCompatActivity {

    private User user;
    private FirecastDB repository;
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
    private List<Integer> typesSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurence_type_user);

        repository =new FirecastDB(this);
        user = repository.getUser();

        SetCheckBoxesOccurrenceTypes();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_preference_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save) {
            if(SaveChanges()) {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("UserKey", user);
                setResult(Constant.ACTIVITY_OCCURRENCE_TYPES, i);
                finish();
                return true;
            }
            return false;
        }
        if( id == android.R.id.home){
            Intent i = new Intent(this, MainActivity.class);
            setResult(Activity.RESULT_CANCELED, i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean SaveChanges() {

            List<OccurrenceType> types = DataBaseTemp.typesOccurrences();
            types = DeleteTypeOnList(types, DataBaseTemp.ID_ACIDENT, oc_accident_car.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_PARAMEDICS, oc_paramedics.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_SUPORT, oc_support.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_CUTTING_TREE, oc_tree_cutting.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_INSECT, oc_insect.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_PREVENTIVE, oc_action_preventive.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_OTHERS, oc_other.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_FIRE, oc_fire.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_NOT_SERVICE, oc_nao_atendida.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_DANGEROUS, oc_dangerous_product.isChecked());
            types = DeleteTypeOnList(types, DataBaseTemp.ID_RESCUES, oc_search_rescue.isChecked());

            repository.Delete_List_User_OccurrenceType(user);

            user.setOccurrenceTypes(types);

            return repository.UpdateUser(user);
    }

    private List<OccurrenceType> DeleteTypeOnList(List<OccurrenceType> types, int id_type, boolean condition) {
        if(!condition) {
            return stream(types).where(x -> x.id != id_type).toList();
        }
        return types;
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

}
