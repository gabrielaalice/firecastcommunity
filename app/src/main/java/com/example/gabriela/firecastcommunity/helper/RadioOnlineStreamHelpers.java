package com.example.gabriela.firecastcommunity.helper;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.DataBaseTemp;
import com.example.gabriela.firecastcommunity.domain.RadioCity;
import com.example.gabriela.firecastcommunity.fragment.RadioFragment;

import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

public class RadioOnlineStreamHelpers {

    MediaPlayer mediaPlayer;
    boolean started = false;
    boolean prepared = false;

    public RadioOnlineStreamHelpers(View view, ImageButton play, String city, String type_radio) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (started) {
                    mediaPlayer.pause();
                    started = false;
                    play.setImageResource(R.drawable.pause_btn);
                } else {
                    mediaPlayer.start();
                    started = true;
                    play.setImageResource(R.drawable.play_btn);

//                    String url = RadioFragment.UpdateRadio(view);
//                    if(url == null){
//                        Toast.makeText(view.getContext(),"Cidade inválida", Toast.LENGTH_LONG).show();
//                    }
                }
            }
        });

        new PlayTask().execute("http://radio.cbm.sc.gov.br:8000/radio");
        play.setEnabled(true);
    }

    private RadioCity LoadRadioCityLocation(String city, String typeCity) {
        String cityName = MetodsHelpers.normalizeString(city);
        return stream(DataBaseTemp.radios())
                .firstOrNull(x->x.type_radio.equalsIgnoreCase(typeCity) &&
                        MetodsHelpers.normalizeString(x.cityName).equalsIgnoreCase(cityName));
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
