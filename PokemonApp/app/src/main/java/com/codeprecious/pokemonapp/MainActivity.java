package com.codeprecious.pokemonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.codeprecious.adapters.PokemonAdapter;
import com.codeprecious.pokemonapp.databinding.ActivityMainBinding;
import com.codeprecious.pokemonapp.model.Pokemon;
import com.codeprecious.pokemonapp.viewModel.PokemonViewModel;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    PokemonAdapter pokemonAdapter;
    PokemonViewModel viewModel;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pokemonAdapter = new PokemonAdapter();
        binding.rv.setAdapter(pokemonAdapter);
        binding.rv.setHasFixedSize(true);

        binding.btnFav.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FavActivity.class)));

        viewModel = new ViewModelProvider(this).get(PokemonViewModel.class);
        viewModel.getPokemons();
        viewModel.responseLiveData().observe(this, pokemonAdapter::setList);

        viewModel.insertLiveData().observe(MainActivity.this, aBoolean -> {
            Toast.makeText(MainActivity.this, "Pokemon added to favorites", Toast.LENGTH_SHORT).show();
            pokemonAdapter.notifyDataSetChanged();
        });

        swipe();
    }

    public void swipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int adapterPosition = viewHolder.getAdapterPosition();
                Pokemon swipedPokemon = pokemonAdapter.getPokemonAtPosition(adapterPosition);
                viewModel.insertPokemon(swipedPokemon);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rv);
    }
}