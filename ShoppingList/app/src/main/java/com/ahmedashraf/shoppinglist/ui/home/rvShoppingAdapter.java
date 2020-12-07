package com.ahmedashraf.shoppinglist.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedashraf.shoppinglist.R;
import com.ahmedashraf.shoppinglist.ui.CartListener;
import com.ahmedashraf.shoppinglist.ui.Product;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class rvShoppingAdapter extends RecyclerView.Adapter<rvShoppingAdapter.ShoppingHolder>{

    ArrayList<Product> ShoppingList, Cart;
    Context context;

    public rvShoppingAdapter(ArrayList<Product> shoppingList) {
        ShoppingList = shoppingList;
    }

    @NonNull
    @Override
    public ShoppingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_shopping_rv, parent, false);
        ShoppingHolder shoppingHolder = new ShoppingHolder(view);
        context = parent.getContext();
        Cart = new ArrayList<>();
        return shoppingHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingHolder holder, int position) {


        Glide.with(context).load(ShoppingList.get(position).getImgProduct()).into(holder.imgProduct);
        holder.tvTitle.setText(ShoppingList.get(position).getTitle());
        holder.tvDescription.setText(ShoppingList.get(position).getDescription());
        holder.tvAPrice.setText("$" + ShoppingList.get(position).getAPrice());
        holder.tvBPrice.setText("$" + ShoppingList.get(position).getBPrice());
        if(ShoppingList.get(position).getStock() == 0){
            holder.tvStock.setText( "Out of stock");
            holder.btnCart.setEnabled(false);
            holder.btnPlus.setEnabled(false);
            holder.btnMinus.setEnabled(false);
        } else{
            holder.tvStock.setText(ShoppingList.get(position).getStock() + " left in stock");
        }
    }

    @Override
    public int getItemCount() {
        return ShoppingList.size();
    }

    class ShoppingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgProduct;
        TextView tvTitle, tvDescription, tvAPrice, tvBPrice, tvStock, tvAmount;
        Button btnCart, btnPlus, btnMinus;

        public ShoppingHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAPrice = itemView.findViewById(R.id.tvAPrice);
            tvBPrice = itemView.findViewById(R.id.tvBPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            btnCart = itemView.findViewById(R.id.btnCart);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);

            btnCart.setOnClickListener(this);

            btnPlus.setOnClickListener(v -> {
                byte amount = Byte.parseByte(tvAmount.getText().toString());
                if (amount >= 1) {
                    amount++;
                    tvAmount.setText("" + amount);
                    if (amount >= ShoppingList.get(this.getLayoutPosition()).getStock()) {
                        btnPlus.setEnabled(false);
                    }
                }
                tvAPrice.setText("$" + (ShoppingList.get(this.getLayoutPosition()).getAPrice() * amount));
                tvBPrice.setText("$" + (ShoppingList.get(this.getLayoutPosition()).getBPrice() * amount));
            });

            btnMinus.setOnClickListener(v -> {
                byte amount = Byte.parseByte(tvAmount.getText().toString());
                if (amount > 1) {
                    amount--;
                    tvAmount.setText("" + amount);
                    btnPlus.setEnabled(true);
                }
                tvAPrice.setText("$" + (ShoppingList.get(this.getLayoutPosition()).getAPrice() * amount));
                tvBPrice.setText("$" + (ShoppingList.get(this.getLayoutPosition()).getBPrice() * amount));
            });
        }

        @Override
        public void onClick(View v) {
            byte amount = Byte.parseByte(tvAmount.getText().toString());
            if (amount <= 0) {
                tvAmount.setText("1");
            }
            if (Cart.contains(ShoppingList.get(this.getLayoutPosition()))) {
                Toast.makeText(context, "This product has been added before, it will be reset", Toast.LENGTH_LONG).show();
                ShoppingList.get(this.getLayoutPosition()).setAmount(Integer.parseInt(tvAmount.getText().toString()));
                byte index = (byte) Cart.indexOf(ShoppingList.get(this.getLayoutPosition()));
                Cart.set(index, ShoppingList.get(this.getLayoutPosition()));
            } else {
                Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();
                ShoppingList.get(this.getLayoutPosition()).setAmount(Integer.parseInt(tvAmount.getText().toString()));
                Cart.add(ShoppingList.get(this.getLayoutPosition()));
            }

            if (context instanceof CartListener) {
                ((CartListener) context).onCart(Cart);
            }
        }
    }


}