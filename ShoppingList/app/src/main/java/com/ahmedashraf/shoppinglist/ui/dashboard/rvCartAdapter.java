package com.ahmedashraf.shoppinglist.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedashraf.shoppinglist.R;
import com.ahmedashraf.shoppinglist.ui.CartListener;
import com.ahmedashraf.shoppinglist.ui.Product;
import com.ahmedashraf.shoppinglist.ui.Update;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class rvCartAdapter extends RecyclerView.Adapter<rvCartAdapter.CartHolder> {

    ArrayList<Product> CartList;
    Context context;

    public rvCartAdapter(ArrayList<Product> cartList) {
        CartList = cartList;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlist_rv, parent, false);
        CartHolder cartHolder = new CartHolder(view);
        context = parent.getContext();
        return cartHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Glide.with(context).load(CartList.get(position).getImgProduct()).into(holder.imgProduct);
        holder.tvTitle.setText(CartList.get(position).getTitle());
        holder.tvAPrice.setText("$" + (CartList.get(position).getAPrice() * CartList.get(position).getAmount()));
        holder.tvAmount2.setText("" + CartList.get(position).getAmount());
        holder.tvStock.setText(CartList.get(position).getStock() + " left in stock");
        if(CartList.get(position).getStock() == CartList.get(position).getAmount()){
            holder.btnPlus.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return CartList.size();
    }

    void saveToShared(Object cart){
        SharedPreferences pref = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cart);
        edit.putString("myCart", json);
        edit.commit();
    }

    class CartHolder extends RecyclerView.ViewHolder{
         ImageView imgProduct;
         TextView tvTitle, tvAPrice, tvStock, tvAmount2, tvTotalPrice;
         Button btnDel, btnPlus, btnMinus;
        public CartHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAPrice = itemView.findViewById(R.id.tvAPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvAmount2 = itemView.findViewById(R.id.tvAmount2);
            btnDel = itemView.findViewById(R.id.btnDel);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);


            btnPlus.setOnClickListener(v -> {
                byte amount = Byte.parseByte(tvAmount2.getText().toString());
                if(amount >= 1) {
                    amount++;
                    tvAmount2.setText("" + amount);
                    if (amount >= CartList.get(getLayoutPosition()).getStock()) {
                        btnPlus.setEnabled(false);
                    }
                }
                CartList.get(getLayoutPosition()).setAmount(Integer.parseInt(tvAmount2.getText().toString()));
                tvAPrice.setText("$" + (CartList.get(getLayoutPosition()).getAPrice() * amount));
                updateCart();
            });

            btnMinus.setOnClickListener(v -> {
                byte amount = Byte.parseByte(tvAmount2.getText().toString());
                if(amount > 1){
                    amount--;
                    tvAmount2.setText("" + amount);
                    btnPlus.setEnabled(true);
                }
                CartList.get(getLayoutPosition()).setAmount(Integer.parseInt(tvAmount2.getText().toString()));
                tvAPrice.setText("$" + (CartList.get(getLayoutPosition()).getAPrice() * amount));
                updateCart();
            });

            btnDel.setOnClickListener(v -> {
                CartList.remove(getLayoutPosition());
                notifyItemRemoved(getLayoutPosition());
                updateCart();
                if(CartList.size() == 0){
                    deleteShared();
                }
            });
        }
        void updateCart(){
            if (context instanceof Update) {
                ((Update) context).updatePrice();
            }
        }

        void deleteShared(){
            if (context instanceof Update) {
                ((Update) context).deleteSharedPref();
            }
        }
    }


}