package service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RickAndMortyClient {

    private static RickAndMortyClient instance = null;
    private RickAndMortyService rickAndMortyService;
    private Retrofit retrofit;

    public RickAndMortyClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rickAndMortyService = retrofit.create(RickAndMortyService.class);
    }

    public static RickAndMortyClient getInstance() {
        if(instance==null) {
            instance = new RickAndMortyClient();
        }

        return instance;
    }

    public RickAndMortyService getRickAndMortyService() {
        return rickAndMortyService;
    }
}
