package com.example.gabriela.firecastcommunity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.example.gabriela.firecastcommunity.validation.MetodsHelpers;

import java.util.ArrayList;
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

        List<CityRadio> radios = LoaddingRadiosCity();
        CityRadio cityRadio = LoadRadioCityLocation(city,radios);

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

    private CityRadio LoadRadioCityLocation(String city, List<CityRadio> radios) {

        city = MetodsHelpers.normalizeString(city);

        for (CityRadio radioCity:radios) {
            String cityName = MetodsHelpers.normalizeString(radioCity.cityName);
            if(city.equalsIgnoreCase(cityName)){
                return radioCity;
            }
        }

        CityRadio cityDefault = new CityRadio("http://radio.cbm.sc.gov.br:8000/radio", "Florianópolis");

        CityRadio url = stream(radios)
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



    private List<CityRadio> LoaddingRadiosCity() {
        List<CityRadio> radiosUrl = new ArrayList<>();
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8000/radio", "Florianópolis"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8028/radio", "Balneario Camboriu"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8001/radio", "Blumenau"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8003/radio", "Canoinhas"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8004/radio", "Chapecó"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8005/radio", "Criciúma"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8006/radio", "Curitibanos"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8007/radio", "Dionisio Cerqueira"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8009/radio", "Brusque"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8013/radio", "Itajai"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8014/radio", "Itapiranga"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8025/radio", "Jaguaruna"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8011/radio", "Joaçaba"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8015/radio", "Lages"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8017/radio", "Maravilha"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8018/radio", "Palma Sola"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8020/radio", "Porto União"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8021/radio", "Rio do Sul"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8022/radio", "São Bento do Sul"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8061/radio", "São Joaquim"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8023/radio", "São Lourenço do Oeste"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8024/radio", "São Miguel do Oeste"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8026/radio", "Videira"));
        radiosUrl.add(new CityRadio("http://radio.cbm.sc.gov.br:8027/radio", "Xanxere"));
        radiosUrl.add(new CityRadio("http://gravacaofns.cbm.sc.gov.br:8000/radio", "Florianópolis"));
        radiosUrl.add(new CityRadio("http://gravacaobcu.cbm.sc.gov.br:8000/radio", "Balneário Camboriú"));
        radiosUrl.add(new CityRadio("http://gravacaobnu.cbm.sc.gov.br:8000/radio", "Blumenau"));
        radiosUrl.add(new CityRadio("http://gravacaobqe.cbm.sc.gov.br:8000/radio", "Brusque"));
        radiosUrl.add(new CityRadio("http://gravacaocbs.cbm.sc.gov.br:8000/radio", "Curitibanos"));
        radiosUrl.add(new CityRadio("http://gravacaocco.cbm.sc.gov.br:8000/radio", "Chapecó"));
        radiosUrl.add(new CityRadio("http://gravacaocni.cbm.sc.gov.br:8000/radio", "Canoinhas"));
        radiosUrl.add(new CityRadio("http://gravacaocua.cbm.sc.gov.br:8000/radio", "Criciúma"));
        radiosUrl.add(new CityRadio("http://gravacaodcq.cbm.sc.gov.br:8000/radio", "Dionísio Cerqueira"));
        radiosUrl.add(new CityRadio("http://gravacaoiai.cbm.sc.gov.br:8000/radio", "Itajaí"));
        radiosUrl.add(new CityRadio("http://gravacaoita.cbm.sc.gov.br:8000/radio", "Itapiranga"));
        radiosUrl.add(new CityRadio("http://gravacaojca.cbm.sc.gov.br:8000/radio", "Joaçaba"));
        radiosUrl.add(new CityRadio("http://gravacaojju.cbm.sc.gov.br:8000/radio", "Jaguaruna"));
        radiosUrl.add(new CityRadio("http://gravacaolgs.cbm.sc.gov.br:8000/radio", "Lages"));
        radiosUrl.add(new CityRadio("http://gravacaomvh.cbm.sc.gov.br:8000/radio", "Maravilha"));
        radiosUrl.add(new CityRadio("http://gravacaopmx.cbm.sc.gov.br:8000/radio", "Palma Sola"));
        radiosUrl.add(new CityRadio("http://gravacaopun.cbm.sc.gov.br:8000/radio", "Porto União"));
        radiosUrl.add(new CityRadio("http://gravacaorsl.cbm.sc.gov.br:8000/radio", "Rio do Sul"));
        radiosUrl.add(new CityRadio("http://gravacaosbs.cbm.sc.gov.br:8000/radio", "São Bento do Sul"));
        radiosUrl.add(new CityRadio("http://gravacaosjq.cbm.sc.gov.br:8000/radio", "São Joaquim"));
        radiosUrl.add(new CityRadio("http://gravacaosge.cbm.sc.gov.br:8000/radio", "São Miguel do Oeste"));
        radiosUrl.add(new CityRadio("http://gravacaosnx.cbm.sc.gov.br:8000/radio", "São Lourenço do Oeste"));
        radiosUrl.add(new CityRadio("http://gravacaotio.cbm.sc.gov.br:8000/radio", "Timbó"));
        radiosUrl.add(new CityRadio("http://gravacaovii.cbm.sc.gov.br:8000/radio", "Videira"));
        radiosUrl.add(new CityRadio("http://gravacaoxxe.cbm.sc.gov.br:8000/radio", "Xanxerê"));
        
        return radiosUrl;
    }
    
    public class CityRadio
    {
        String urlRadio, cityName;
        public CityRadio(String urlRadio, String cityName){
            this.urlRadio = urlRadio;
            this.cityName = cityName;
        }
    }
}
