package com.codeprecious.shoppinglistmvvm_hilt_rxjava.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ItemEntity item);

    @Delete()
    Completable delete(ItemEntity item);

    @Query("Select * from items")
    Flowable<List<ItemEntity>> getAll();

}
