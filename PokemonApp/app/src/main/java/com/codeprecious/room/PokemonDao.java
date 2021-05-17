package com.codeprecious.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.codeprecious.pokemonapp.model.Pokemon;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


@Dao
public interface PokemonDao {
    @Insert()
    Completable insert(Pokemon pokemon);

    @Query("delete from Pokemons where name = :pokemon")
    Completable delete(String pokemon);

    @Query("select * from Pokemons")
    Flowable<List<Pokemon>> getPokemons();
}
