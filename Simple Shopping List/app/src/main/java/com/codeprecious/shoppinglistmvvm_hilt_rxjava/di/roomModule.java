package com.codeprecious.shoppinglistmvvm_hilt_rxjava.di;

import android.content.Context;

import androidx.room.Room;

import com.codeprecious.shoppinglistmvvm_hilt_rxjava.room.ItemDB;
import com.codeprecious.shoppinglistmvvm_hilt_rxjava.room.ItemDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class roomModule {

    @Singleton
    @Provides
    public static ItemDB provideRoom(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, ItemDB.class, ItemDB.DB_NAME)
                .fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    public static ItemDao provideItemDao(ItemDB itemDB) {
        return itemDB.itemDao();
    }
}
