package com.codeprecious.pokemonapp.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codeprecious.pokemonapp.model.Pokemon;
import com.codeprecious.pokemonapp.repository.Repository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class PokemonViewModel extends ViewModel {

    private final Repository repository;

    @Inject
    public PokemonViewModel(Repository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<List<Pokemon>> response = new MutableLiveData<>();
    private final MutableLiveData<Boolean> inserting = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleting = new MutableLiveData<>();

    public LiveData<List<Pokemon>> responseLiveData() {
        return response;
    }

    public LiveData<Boolean> insertLiveData() {
        return inserting;
    }

    public LiveData<Boolean> deleteLiveData() {
        return deleting;
    }

    public void getPokemons() {
        repository.getPokemons()
                .subscribeOn(Schedulers.io())
                .map(pokemonResponse -> {
                    List<Pokemon> result = pokemonResponse.getResults();
                    for (Pokemon pokemon : result) {
                        String url = pokemon.getUrl();
                        String[] split = url.split("/");
                        pokemon.setUrl("https://pokeres.bastionbot.org/images/pokemon/" + split[split.length - 1] + ".png");
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> response.setValue(result),
                        error -> Log.d("tag", "getPokemons: " + error));
    }

    public LiveData<List<Pokemon>> getFavPokemons() {
        return repository.getFavPokemons();
    }

    public void insertPokemon(Pokemon pokemon) {
        repository.insertPokemon(pokemon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> inserting.setValue(true),
                        error -> Log.d("asd", "insertPokemon: " + error));

    }

    public void deletePokemon(String pokemonName) {
        repository.deletePokemon(pokemonName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> deleting.setValue(true),
                        error -> Log.d("asd", "deletePokemon: " + error));
    }
}
