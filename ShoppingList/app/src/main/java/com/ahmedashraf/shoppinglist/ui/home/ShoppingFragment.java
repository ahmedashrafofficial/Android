package com.ahmedashraf.shoppinglist.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedashraf.shoppinglist.R;
import com.ahmedashraf.shoppinglist.ui.Product;

import java.util.ArrayList;

public class ShoppingFragment extends Fragment  {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView rvShoppingList = root.findViewById(R.id.rvShoppingList);
        ArrayList<Product> shoppingList = new ArrayList<>();
        shoppingList.add(new Product("https://image.shutterstock.com/image-photo/black-smart-phone-isolated-600w-237055303.jpg", "Samsung galaxy note 8", "Ram 8GB, Internal memory 128GB, 120HZ Amoled Screen, 5.6In, 64MP Back camera, 32MP Front camera", 10000, 11999, 7));
        shoppingList.add(new Product("https://image.shutterstock.com/image-photo/ukraine-dnepropetrovsk-region-city-krivoy-600w-1133418689.jpg", "Oppo reno 4", "Ram 6GB, Internal memory 64GB, 60HZ Amoled Screen, 5.2In, 16MP Back camera, 32MP Front camera", 4500, 4799, 5));
        shoppingList.add(new Product("https://image.shutterstock.com/image-photo/moscow-russia-2020-june-6-600w-1753499363.jpg", "Redmi note 8 pro", "Ram 4GB, Internal memory 32GB, 60HZ Amoled Screen, 5.0In, 12MP Back camera, 32MP Front camera", 5600, 5999, 1));
        shoppingList.add(new Product("https://image.shutterstock.com/image-photo/bangkok-thailand-realme-launch-new-600w-1682832355.jpg", "Realme 6", "Ram 8GB, Internal memory 128GB, 90HZ Amoled Screen, 4.5In, 32MP Back camera, 16MP Front camera", 8700, 9999, 0));


        rvShoppingAdapter adapter = new rvShoppingAdapter(shoppingList);
        rvShoppingList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvShoppingList.setItemAnimator(new DefaultItemAnimator());
        rvShoppingList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        rvShoppingList.setAdapter(adapter);

        return root;
    }
}