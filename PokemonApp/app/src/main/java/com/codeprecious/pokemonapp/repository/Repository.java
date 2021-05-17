package com.codeprecious.pokemonapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.codeprecious.pokemonapp.model.Pokemon;
import com.codeprecious.pokemonapp.model.PokemonResponse;
import com.codeprecious.pokemonapp.retrofit.PokemonApiService;
import com.codeprecious.room.PokemonDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Repository {

    private PokemonApiService pokemonApiService;
    private PokemonDao pokemonDao;

    @Inject()
    public Repository(PokemonApiService pokemonApiService, PokemonDao pokemonDao) {
        this.pokemonApiService = pokemonApiService;
        this.pokemonDao = pokemonDao;
    }

    public Flowable<PokemonResponse> getPokemons() {
        return pokemonApiService.getPokemons();
    }

    public Completable insertPokemon(Pokemon pokemon) {
        return pokemonDao.insert(pokemon);
    }

    public Completable deletePokemon(String pokemonName) {
        return pokemonDao.delete(pokemonName);
    }

    public LiveData<List<Pokemon>> getFavPokemons() {
        return LiveDataReactiveStreams.fromPublisher(pokemonDao.getPokemons()
                .subscribeOn(Schedulers.io()));
    }
}
