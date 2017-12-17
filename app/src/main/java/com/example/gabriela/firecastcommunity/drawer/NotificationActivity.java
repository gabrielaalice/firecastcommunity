package com.example.gabriela.firecastcommunity.drawer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.FirecastDB;
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.utility.Constant;

public class NotificationActivity extends AppCompatActivity {

    private Switch notifyGeral;
    private User user;
    private FirecastDB repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        repository = new FirecastDB(this);
        user = repository.getUser();

        notifyGeral = findViewById(R.id.geralSwitch);
        notifyGeral.setChecked(user.isNotify());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
                setResult(Constant.ACTIVITY_NOTIFICATION, i);
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
        user.setNotify(notifyGeral.isChecked());
        return repository.UpdateUser(user);
    }
}
