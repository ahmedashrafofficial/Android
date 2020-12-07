package com.ahmedashraf.shoppinglist.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedashraf.shoppinglist.R;
import com.ahmedashraf.shoppinglist.ui.Product;
import com.ahmedashraf.shoppinglist.ui.Update;
import com.ahmedashraf.shoppinglist.ui.notifications.PaymentFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class CartFragment extends Fragment {
    TextView tvTotalPrice;
    Button btnCheckout;
    ArrayList<Product> CartList;
    RecyclerView rvCartList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveToShared();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        tvTotalPrice = root.findViewById(R.id.tvTotalPrice);
        btnCheckout = root.findViewById(R.id.btnCheckout);
        rvCartList = root.findViewById(R.id.rvCartList);
        CartList = new ArrayList<>();

        if(getArguments() != null) {
            if (getArguments().size() != 0) {
                CartList.addAll((ArrayList<Product>) getArguments().getSerializable("cart"));
                getArguments().clear();
            }
        }

        if(!retrieveFromShard()&& CartList.size() == 0){
            Toast.makeText(getActivity(), "Your cart is empty", Toast.LENGTH_SHORT).show();
        } else{
            setAdapter();
            updatePrice(updateCart());
        }

        btnCheckout.setOnClickListener(v -> {
            if(CartList.size() != 0) {
                PaymentFragment paymentFragment = new PaymentFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble(PaymentParams.AMOUNT, updateCart());
                bundle.putString(PaymentParams.ORDER_ID, String.valueOf(new Random().nextInt()));   //should be saved in database
                bundle.putString(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");
                // should be put from database with right info of billing address
                bundle.putString(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
                bundle.putString(PaymentParams.CITY_BILLING, "Manama");
                bundle.putString(PaymentParams.STATE_BILLING, "Manama");
                bundle.putString(PaymentParams.COUNTRY_BILLING, "BHR");
                bundle.putString(PaymentParams.POSTAL_CODE_BILLING, "00973");
                paymentFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cont, paymentFragment).commit();

            } else{
                Toast.makeText(getActivity(), "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    void setAdapter(){
        rvCartAdapter adapter = new rvCartAdapter(CartList);
        rvCartList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCartList.setItemAnimator(new DefaultItemAnimator());
        rvCartList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        rvCartList.setAdapter(adapter);
    }

    public double updateCart(){
        int sum = 0;
        for (int i = 0; i < CartList.size(); i++) {
            sum += (CartList.get(i).getAPrice() * CartList.get(i).getAmount());
        }
        return sum;
    }

    public void updatePrice(double sum){
        tvTotalPrice.setText("Total price is: $"+ sum);
    }

    void saveToShared(){
        if(CartList.size() != 0) {
            SharedPreferences pref = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(CartList);
            edit.putString("myCart", json);
            edit.commit();
        }
    }

    boolean retrieveFromShard(){
        SharedPreferences pref = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("myCart", "");
        if(!json.isEmpty()) {
            Type type = new TypeToken<List<Product>>(){}.getType();
            if(CartList.size() == 0) {
                CartList.addAll(gson.fromJson(json, type));
            } else {
                boolean dialogValueBack = getDialogValueBack(getActivity());
                if (!dialogValueBack) {
                    ArrayList<Product> oldCart = new ArrayList<>(gson.fromJson(json, type));
                    for (int i = 0; i < oldCart.size(); i++) {
                        if (CartList.contains(oldCart.get(i))) {    // check if product == product
                            byte index = (byte) CartList.indexOf(oldCart.get(i));
                            byte amount = (byte) (CartList.get(index).getAmount() + oldCart.get(i).getAmount());
                            if (amount >= oldCart.get(i).getStock()) {
                                CartList.get(index).setAmount(oldCart.get(i).getStock());
                            } else {
                                CartList.get(index).setAmount(amount);
                            }
                        } else {
                            CartList.add(oldCart.get(i));
                        }
                    }
                }
            }
            return true;    // data exists
        }
        return false;       // no data
    }

    private boolean resultValue;
    public boolean getDialogValueBack(Context context)
    {
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                throw new RuntimeException();
            }
        };
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Attention");
        alert.setMessage("Do you want to reset your cart ?");
        alert.setPositiveButton("Yes", (dialog, id) -> {
            resultValue = true;
            handler.sendMessage(handler.obtainMessage());
        });
        alert.setNegativeButton("No", (dialog, id) -> {
            resultValue = false;
            handler.sendMessage(handler.obtainMessage());
        });
        alert.show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return resultValue;
    }
}