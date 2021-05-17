package com.codeprecious.shoppinglistmvvm_hilt_rxjava.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.codeprecious.shoppinglistmvvm_hilt_rxjava.room.ItemDao;
import com.codeprecious.shoppinglistmvvm_hilt_rxjava.room.ItemEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Repository {

    private ItemDao itemDao;

    @Inject()
    public Repository(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public Completable insert(ItemEntity item) {
        return itemDao.insert(item);
    }

    public Completable delete(ItemEntity item) {
        return itemDao.delete(item);
    }

    public LiveData<List<ItemEntity>> getItem() {
        return LiveDataReactiveStreams.fromPublisher(itemDao.getAll()
                .subscribeOn(Schedulers.io()));
    }

}
