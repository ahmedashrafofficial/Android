package com.codeprecious.shoppinglistmvvm_hilt_rxjava.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.codeprecious.shoppinglistmvvm_hilt_rxjava.R;
import com.codeprecious.shoppinglistmvvm_hilt_rxjava.adapters.ItemAdapter;
import com.codeprecious.shoppinglistmvvm_hilt_rxjava.databinding.ActivityMainBinding;
import com.codeprecious.shoppinglistmvvm_hilt_rxjava.room.ItemEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements DialogListener {
    MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ItemAdapter adapter = new ItemAdapter(viewModel);
        adapter.setItems(new ArrayList<>());

        binding.rv.setAdapter(adapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setHasFixedSize(true);

        viewModel.getItems().observe(this, itemEntities -> {
            adapter.setItems(itemEntities);
            adapter.notifyDataSetChanged();
        });

        binding.fab.setOnClickListener(v -> {
            new ItemDialog(this, this).show();

        });

    }

    @Override
    public void onAddButtonClicked(ItemEntity item) {
        Log.d("tag", "onAddButtonClicked: " + item.getName());
        viewModel.insertItem(item);
    }

    @Inject
    CompositeDisposable disposable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag", "onDestroy: " + disposable.size());
        disposable.clear();
    }
}