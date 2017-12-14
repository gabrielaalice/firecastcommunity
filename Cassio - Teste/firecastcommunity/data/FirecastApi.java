package com.example.gabriela.firecastcommunity.data;

import com.example.gabriela.firecastcommunity.domain.Occurrence;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by bonet on 9/13/16.
 */

public interface FirecastApi {

    @GET("listar_ocorrencia_aberta_by_servidor_json.php?")
    Call<List<Occurrence>> getOccurrences(
            @Query("cidade") String city);
}
