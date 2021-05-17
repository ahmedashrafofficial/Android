package com.codeprecious.pokemonapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.codeprecious.adapters.PokemonAdapter;
import com.codeprecious.pokemonapp.databinding.ActivityFavBinding;
import com.codeprecious.pokemonapp.model.Pokemon;
import com.codeprecious.pokemonapp.viewModel.PokemonViewModel;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavActivity extends AppCompatActivity {

    PokemonAdapter pokemonAdapter;
    PokemonViewModel viewModel;

    ActivityFavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pokemonAdapter = new PokemonAdapter();
        binding.rvFav.setAdapter(pokemonAdapter);
        binding.rvFav.setHasFixedSize(true);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        viewModel = new ViewModelProvider(this).get(PokemonViewModel.class);
        viewModel.getFavPokemons().observe(this, pokemons -> {
            pokemonAdapter.setList(pokemons);
        });

        viewModel.deleteLiveData().observe(FavActivity.this, aBoolean -> {
            pokemonAdapter.notifyDataSetChanged();
            Toast.makeText(FavActivity.this, "Pokemon deleted from favorites", Toast.LENGTH_SHORT).show();
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
                viewModel.deletePokemon(swipedPokemon.getName());

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvFav);
    }

}