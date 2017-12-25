package com.example.gabriela.firecastcommunity.drawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.FirecastDB;
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.utility.Constant;
import com.example.gabriela.firecastcommunity.utility.UtilitaryHelper;

import java.util.Date;

public class NotificationActivity extends AppCompatActivity {

    private Switch notifyGeral;
    private User user;
    private FirecastDB repository;
    private Switch silenceSwitch;
    private Switch soundSwitch;
    private Switch vibrateSwitch;
    private TimePicker timeStart;
    private TimePicker timeFinish;
    private LinearLayout layoutTimeSilence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        repository = new FirecastDB(this);
        user = repository.getUser();

        notifyGeral = findViewById(R.id.geralSwitch);
        notifyGeral.setChecked(user.isNotify());

        silenceSwitch = findViewById(R.id.silenceSwitch);

        soundSwitch = findViewById(R.id.soundSwitch);
        soundSwitch.setChecked(user.isSound());

        vibrateSwitch = findViewById(R.id.vibrateSwitch);
        vibrateSwitch.setChecked(user.isVibrate());

        SwitchNotify();

        layoutTimeSilence = findViewById(R.id.layoutTimeSilence);

        if (user.isTimeSilence()) {
            silenceSwitch.setChecked(true);
            layoutTimeSilence.setVisibility(View.VISIBLE);
            if(user.getTimeFinishSilence()!=null && user.getTimeStartSilence()!=null) {
                timeStart = UtilitaryHelper.ConfigurarTimePicker24Horas(this, R.id.timeStartSilence, user.getTimeStartSilence().getHours(), user.getTimeStartSilence().getMinutes());
                timeFinish = UtilitaryHelper.ConfigurarTimePicker24Horas(this, R.id.timeFinishSilence, user.getTimeFinishSilence().getHours(), user.getTimeFinishSilence().getMinutes());
            }else{
                timeStart = UtilitaryHelper.ConfigurarTimePicker24Horas(this, R.id.timeStartSilence, 0, 0);
                timeFinish = UtilitaryHelper.ConfigurarTimePicker24Horas(this, R.id.timeFinishSilence, 0, 0);
            }
        } else {
            silenceSwitch.setChecked(false);
            layoutTimeSilence.setVisibility(View.INVISIBLE);
            timeStart = UtilitaryHelper.ConfigurarTimePicker24Horas(this, R.id.timeStartSilence, 0, 0);
            timeFinish = UtilitaryHelper.ConfigurarTimePicker24Horas(this, R.id.timeFinishSilence, 0, 0);
        }

        notifyGeral.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                SwitchNotify();
            }
        });

        silenceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(silenceSwitch.isChecked()){
                    layoutTimeSilence.setVisibility(View.VISIBLE);
                }else{
                    layoutTimeSilence.setVisibility(View.INVISIBLE);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void SwitchNotify() {
        if(notifyGeral.isChecked()){
            soundSwitch.setVisibility(View.VISIBLE);
            vibrateSwitch.setVisibility(View.VISIBLE);
        }else{
            soundSwitch.setVisibility(View.INVISIBLE);
            vibrateSwitch.setVisibility(View.INVISIBLE);
        }
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
        user.setSound(soundSwitch.isChecked());
        user.setVibrate(vibrateSwitch.isChecked());
        user.setTimeSilence(silenceSwitch.isChecked());
        if(silenceSwitch.isChecked()){
            user.setTimeStartSilence(getDate(timeStart));
            user.setTimeFinishSilence(getDate(timeFinish));
        }else{
            user.setTimeStartSilence(null);
            user.setTimeFinishSilence(null);
        }
        return repository.UpdateUser(user);
    }

    private Date getDate(TimePicker timePicker) {
        Date date = new Date();
        date.setHours(timePicker.getCurrentHour());
        date.setMinutes(timePicker.getCurrentMinute());
        date.setSeconds(0);
        return date;
    }
}
