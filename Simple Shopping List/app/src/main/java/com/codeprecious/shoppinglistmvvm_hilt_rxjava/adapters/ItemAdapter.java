package com.codeprecious.shoppinglistmvvm_hilt_rxjava.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.codeprecious.shoppinglistmvvm_hilt_rxjava.databinding.ShoppingItemBinding;
import com.codeprecious.shoppinglistmvvm_hilt_rxjava.room.ItemEntity;
import com.codeprecious.shoppinglistmvvm_hilt_rxjava.ui.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemEntity> items;
    MainViewModel viewModel;


    public ItemAdapter(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        viewModel = new ViewModelProvider((ViewModelStoreOwner) parent.getContext()).get(MainViewModel.class);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ShoppingItemBinding binding = ShoppingItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ItemEntity item = items.get(position);
        holder.binding.tvName.setText(item.getName());
        holder.binding.tvAmount.setText("" + item.getAmount());
        holder.binding.imgDelete.setOnClickListener(v -> {
            viewModel.deleteItem(item);
        });
        holder.binding.imgPlus.setOnClickListener(v -> {
            item.setAmount(item.getAmount() + 1);
            viewModel.insertItem(item);
        });
        holder.binding.imgMinus.setOnClickListener(v -> {
            item.setAmount(item.getAmount() - 1);
            viewModel.insertItem(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ShoppingItemBinding binding;

        public ViewHolder(@NonNull @NotNull ShoppingItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
