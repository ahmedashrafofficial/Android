package com.codeprecious.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codeprecious.pokemonapp.model.Pokemon;

@Database(entities = Pokemon.class, version = 1)
public abstract class PokemonDatabase extends RoomDatabase {

    public abstract PokemonDao pokemonDao();

    public static final String db_name = "pokemonDB";


}
