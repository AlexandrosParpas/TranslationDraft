package com.example.alex.finalproject;

import java.util.List;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;


public class TranslateAPI {
    private static final String ENDPOINT = "http://api.mymemory.translated.net/";
    private final TranslateService mService;

    public interface TranslateService {
        @GET("get")
        Call<List<TranslatedData>> getTranslation(String request);
    }

    public TranslateAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(TranslateService.class);
    }

    public void translate(String request) {
        System.out.println("The requested path is " + request);
        mService.getTranslation(request).enqueue(new Callback<List<TranslatedData>>() {
            @Override
            public void onResponse(Response<List<TranslatedData>> response, Retrofit retrofit) {
                System.out.println("[DEBUG]" + " RestApi onResponse Number of repositories- " + response.body().size());
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("[DEBUG]" + " RestApi onFailure - " + "");
            }
        });
    }
}
