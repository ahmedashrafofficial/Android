package com.codeprecious.shoppinglistmvvm_hilt_rxjava.ui;

import com.codeprecious.shoppinglistmvvm_hilt_rxjava.room.ItemEntity;

public interface DialogListener {
    void onAddButtonClicked(ItemEntity item);
}
