package com.codeprecious.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codeprecious.pokemonapp.R;
import com.codeprecious.pokemonapp.model.Pokemon;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    List<Pokemon> pokemon = new ArrayList<>();
    Context context;

    @NonNull
    @NotNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new PokemonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PokemonViewHolder holder, int position) {
        holder.tvPokemonName.setText(pokemon.get(position).getName());
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions())
                .load(pokemon.get(position).getUrl()).into(holder.pokemonImage);
    }

    @Override
    public int getItemCount() {
        return pokemon.size();
    }

    public void setList(List<Pokemon> list) {
        this.pokemon = list;
        notifyDataSetChanged();
    }

    public Pokemon getPokemonAtPosition(int position) {
        return pokemon.get(position);
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        ImageView pokemonImage;
        TextView tvPokemonName;

        public PokemonViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            pokemonImage = itemView.findViewById(R.id.pokemonImage);
            tvPokemonName = itemView.findViewById(R.id.tvPokemonName);
        }
    }
}
