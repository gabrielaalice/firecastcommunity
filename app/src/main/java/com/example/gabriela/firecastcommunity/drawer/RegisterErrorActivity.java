package com.example.gabriela.firecastcommunity.drawer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;

public class RegisterErrorActivity extends AppCompatActivity {


    EditText subject, description;
    Button startBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_error);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        subject = (EditText) findViewById(R.id.edt_subject);
        description = (EditText) findViewById(R.id.edt_description);
        startBtn = (Button) findViewById(R.id.sendEmail);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });
    }


    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"g.alices@hotmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        String sub = ((EditText)findViewById(R.id.edt_subject)).getText().toString();
        String message = ((EditText)findViewById(R.id.edt_description)).getText().toString();
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RegisterErrorActivity.this, "Você não possui nenhum servidor de e-mail instalado.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed(){
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

            default:break;
        }

        return true;
    }
}
