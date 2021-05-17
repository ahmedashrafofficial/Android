package com.codeprecious.pokemonapp.retrofit;

import com.codeprecious.pokemonapp.model.PokemonResponse;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface PokemonApiService {

    @GET("pokemon")
    Flowable<PokemonResponse> getPokemons();
}
