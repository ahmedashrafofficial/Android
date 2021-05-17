package com.codeprecious.shoppinglistmvvm_hilt_rxjava.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codeprecious.shoppinglistmvvm_hilt_rxjava.repository.Repository;
import com.codeprecious.shoppinglistmvvm_hilt_rxjava.room.ItemEntity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


@HiltViewModel
public class MainViewModel extends ViewModel {

    private static final String TAG = "tag";
    private Repository repository;
    private CompositeDisposable disposable;


    @Inject
    public MainViewModel(Repository repository, CompositeDisposable disposable) {
        this.repository = repository;
        this.disposable = disposable;
    }

    public void insertItem(ItemEntity item) {
        disposable.add(repository.insert(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    public void deleteItem(ItemEntity item) {
        disposable.add(repository.delete(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    public LiveData<List<ItemEntity>> getItems() {
        return repository.getItem();
    }
}
