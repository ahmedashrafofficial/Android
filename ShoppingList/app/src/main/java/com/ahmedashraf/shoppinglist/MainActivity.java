package com.ahmedashraf.shoppinglist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmedashraf.shoppinglist.ui.CartListener;
import com.ahmedashraf.shoppinglist.ui.Product;
import com.ahmedashraf.shoppinglist.ui.Update;
import com.ahmedashraf.shoppinglist.ui.dashboard.CartFragment;
import com.ahmedashraf.shoppinglist.ui.home.ShoppingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, CartListener, Update {
    CartFragment cartFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.cont);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        ShoppingFragment shoppingFragment = new ShoppingFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.cont, shoppingFragment).commit();

        navView.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.navigation_home){
            ShoppingFragment shoppingFragment = new ShoppingFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.cont, shoppingFragment).commit();
        } else if(item.getItemId() == R.id.navigation_dashboard){
            if(cartFragment == null){
                cartFragment = new CartFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.cont, cartFragment).commit();
        }
        return true;
    }

    @Override
    public void onCart(ArrayList<Product> cart) {
        if(cartFragment == null){
            cartFragment = new CartFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("cart", cart);
        cartFragment.setArguments(bundle);
    }

    void deleteShared(){
        SharedPreferences pref = getSharedPreferences("cart", Context.MODE_PRIVATE);
        pref.edit().clear().commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteShared();
    }

    @Override
    public void updatePrice() {
        cartFragment.updatePrice(cartFragment.updateCart());
    }

    @Override
    public void deleteSharedPref() {
        deleteShared();
    }
}