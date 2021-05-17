package com.codeprecious.shoppinglistmvvm_hilt_rxjava.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;

import com.codeprecious.shoppinglistmvvm_hilt_rxjava.databinding.DialogBinding;
import com.codeprecious.shoppinglistmvvm_hilt_rxjava.room.ItemEntity;

public class ItemDialog extends AppCompatDialog {
    DialogListener dialog;

    public ItemDialog(Context context, DialogListener dialogListener) {
        super(context);
        dialog = dialogListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogBinding binding = DialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvCancel.setOnClickListener(v -> {
            cancel();
        });

        binding.tvAdd.setOnClickListener(v -> {
            String name = binding.etName.getText().toString();
            String amount = binding.etAmount.getText().toString();

            if (name.isEmpty() && amount.isEmpty()) {
                Toast.makeText(getContext(), "Please enter item info", Toast.LENGTH_SHORT).show();

            } else {
                ItemEntity itemEntity = new ItemEntity(name, Integer.parseInt(amount));
                dialog.onAddButtonClicked(itemEntity);
                dismiss();
            }
        });
    }
}
