package com.codeprecious.pokemonapp.di;

import android.content.Context;

import androidx.room.Room;

import com.codeprecious.room.PokemonDao;
import com.codeprecious.room.PokemonDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Singleton
    @Provides
    public static PokemonDatabase providePokemonDb(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, PokemonDatabase.class, PokemonDatabase.db_name)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    public static synchronized PokemonDao providePokemonDao(PokemonDatabase pokemonDatabase) {
        return pokemonDatabase.pokemonDao();
    }
}
