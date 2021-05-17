package com.codeprecious.shoppinglistmvvm_hilt_rxjava.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = ItemEntity.class, version = 1)
public abstract class ItemDB extends RoomDatabase {

    public static final String DB_NAME = "ShoppingListDB";

    public abstract ItemDao itemDao();
}
