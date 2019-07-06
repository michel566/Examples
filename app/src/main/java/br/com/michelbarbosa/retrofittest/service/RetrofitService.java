package br.com.michelbarbosa.retrofittest.service;

import br.com.michelbarbosa.retrofittest.domain.RespostaServidor;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitService {

    @Headers("X-RapidAPI-Key: YZaoaf9GcVmsh3lobS37cJ9Utd0op1eNl3fjsnbZ3RfqnDy8eA")

    @GET("cards")
    Call<RespostaServidor> getCardById(@Query("name") String name);

}
