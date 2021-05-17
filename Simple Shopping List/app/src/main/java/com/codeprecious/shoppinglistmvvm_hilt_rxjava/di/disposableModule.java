package com.codeprecious.shoppinglistmvvm_hilt_rxjava.di;

import com.codeprecious.shoppinglistmvvm_hilt_rxjava.ui.ItemDialog;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@Module
@InstallIn(SingletonComponent.class)
public class disposableModule {

    @Singleton
    @Provides
    public static CompositeDisposable provideDisposable() {
        return new CompositeDisposable();
    }

}
