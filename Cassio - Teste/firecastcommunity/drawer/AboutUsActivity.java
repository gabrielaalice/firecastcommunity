package com.example.gabriela.firecastcommunity.drawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;

public class AboutUsActivity extends AppCompatActivity {

    ImageView line_firecast, line_firecast_community;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        line_firecast = (ImageView) findViewById(R.id.firecast_draw_line);
        line_firecast_community = (ImageView) findViewById(R.id.firecast_community_draw_line);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();

                break;

            default:
                break;
        }

        return true;
    }
}