package service;

import response.RickAndMortyResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RickAndMortyService {

    @GET("character")
    Call<RickAndMortyResponse> getCharacters();

    @GET("character")
    Call<RickAndMortyResponse> getCharactersByPage(@Query("page") Integer page);
}
