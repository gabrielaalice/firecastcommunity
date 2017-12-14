package com.example.gabriela.firecastcommunity.data;

import java.util.List;

import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.domain.OccurrenceRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bonet on 9/13/16.
 */

public class FirecastClient implements OccurrenceRepository {

    private FirecastApi api;

    public FirecastClient() {
        api = buildApi();
    }

    public void getOccurrences(String city) {
        api.getOccurrences("Florianopolis")
                .enqueue(new Callback<List<Occurrence>>() {
                    public void onResponse(Call<List<Occurrence>> call, Response<List<Occurrence>> response) {

                    }

                    @Override
                    public void onFailure(Call<List<Occurrence>> call, Throwable t) {

                    }
                });
    }

    public FirecastApi buildApi() {
        return new Retrofit.Builder()
                .baseUrl("http://aplicativosweb.cbm.sc.gov.br/e193comunitario/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FirecastApi.class);
    }

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://aplicativosweb.cbm.sc.gov.br/e193comunitario/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final String URL_WS_LOGIN = "http://192.168.1.15:8080/firecast-ws/rest/user/login";
}