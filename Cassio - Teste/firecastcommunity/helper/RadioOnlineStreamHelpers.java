package com.example.gabriela.firecastcommunity.helper;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.domain.RadioCity;

import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;


/**
 * Created by user on 11/06/2017.
 */

public class RadioOnlineStreamHelpers {

    MediaPlayer mediaPlayer;
    boolean started = false;
    boolean prepared = false;

    public RadioOnlineStreamHelpers(Button play, String city) {

        List<RadioCity> radios = DataBaseTemp.radios();
        RadioCity cityRadio = LoadRadioCityLocation(city,radios);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (started) {
                    mediaPlayer.pause();
                    started = false;
                    play.setText("Play | Cidade: "+ cityRadio.cityName);
                } else {
                    mediaPlayer.start();
                    started = true;
                    play.setText("Pause | Cidade: "+ cityRadio.cityName);
                }

            }
        });

        new PlayTask().execute(cityRadio.urlRadio);

        play.setEnabled(true);
        play.setText("Play | Cidade: "+ cityRadio.cityName);
    }

    private RadioCity LoadRadioCityLocation(String city, List<RadioCity> radios) {

        city = MetodsHelpers.normalizeString(city);

        for (RadioCity radioCity:radios) {
            String cityName = MetodsHelpers.normalizeString(radioCity.cityName);
            if(city.equalsIgnoreCase(cityName)){
                return radioCity;
            }
        }

        RadioCity cityDefault = new RadioCity("http://radio.cbm.sc.gov.br:8000/radio", "Florianópolis");

        RadioCity url = stream(radios)
                .where(c->c.cityName.equalsIgnoreCase("florianópolis"))
                .firstOrDefault(cityDefault);

        return url;
    }

    private class PlayTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return prepared;
        }
    }

}
