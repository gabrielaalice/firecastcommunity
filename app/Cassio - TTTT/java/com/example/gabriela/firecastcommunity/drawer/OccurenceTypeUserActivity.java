package com.example.gabriela.firecastcommunity.drawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;

public class OccurenceTypeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occurence_type_user);

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
            return true;
        }
        if( id == android.R.id.home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
