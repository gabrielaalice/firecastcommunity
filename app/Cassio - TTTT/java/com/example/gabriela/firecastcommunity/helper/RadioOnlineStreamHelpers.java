package com.example.gabriela.firecastcommunity.helper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.domain.RadioCity;

import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

public class RadioOnlineStreamHelpers {

    MediaPlayer mediaPlayer;
    boolean started = false;
    boolean prepared = false;
    List<RadioCity> radios;

    public RadioOnlineStreamHelpers(Context context, Button play, String city) {

        radios = stream(DataBaseTemp.radiosCities()).select(x->new RadioCity(x.first,null)).toList();
        RadioCity cityRadio = LoadRadioCityLocation(city,radios);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (started) {
                    mediaPlayer.pause();
                    started = false;
                    play.setText("Play | Cidade: "+ cityRadio.city.name);
                } else {
                    mediaPlayer.start();
                    started = true;
                    play.setText("Pause | Cidade: "+ cityRadio.city.name);
                }

            }
        });

        new PlayTask().execute(cityRadio.urlRadio);

        play.setEnabled(true);
        play.setText("Play | Cidade: "+ cityRadio.city.name);
    }

    private RadioCity LoadRadioCityLocation(String city, List<RadioCity> radios) {

        city = MetodsHelpers.normalizeString(city);

        for (RadioCity radioCity:radios) {
            String cityName = MetodsHelpers.normalizeString(radioCity.city.name);
            if(city.equalsIgnoreCase(cityName)){
                return radioCity;
            }
        }

        RadioCity cityDefault = new RadioCity("http://radio.cbm.sc.gov.br:8000/radio",
                stream(radios).first(x->x.city.name.equalsIgnoreCase("Florianópolis")).city);

        RadioCity url = stream(radios)
                .where(c->c.city.name.equalsIgnoreCase("florianópolis"))
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
