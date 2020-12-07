package com.ahmedashraf.shoppinglist.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ahmedashraf.shoppinglist.R;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class PaymentFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        EditText etEmail = root.findViewById(R.id.etEmail);
        EditText etPhone = root.findViewById(R.id.etPhone);
        EditText etShippingAddress = root.findViewById(R.id.etShippingAddress);
        EditText etShippingCity = root.findViewById(R.id.etShippingCity);
        EditText etShippingState = root.findViewById(R.id.etShippingState);
        EditText etShippingCountry = root.findViewById(R.id.etShippingCountry);
        EditText etShippingPostalcode = root.findViewById(R.id.etShippingPostalcode);
        Button btnPayment = root.findViewById(R.id.btnPayment);

        Intent in = new Intent(getContext(), PayTabActivity.class);

        Bundle bundle = getArguments();
        if(bundle != null){
            in.putExtra(PaymentParams.AMOUNT,bundle.getDouble(PaymentParams.AMOUNT));
            in.putExtra(PaymentParams.ORDER_ID, bundle.getString(PaymentParams.ORDER_ID));
            in.putExtra(PaymentParams.PRODUCT_NAME, bundle.getString(PaymentParams.PRODUCT_NAME));
            in.putExtra(PaymentParams.ADDRESS_BILLING, bundle.getString(PaymentParams.ADDRESS_BILLING));
            in.putExtra(PaymentParams.CITY_BILLING, bundle.getString(PaymentParams.CITY_BILLING));
            in.putExtra(PaymentParams.STATE_BILLING, bundle.getString(PaymentParams.STATE_BILLING));
            in.putExtra(PaymentParams.COUNTRY_BILLING, bundle.getString(PaymentParams.COUNTRY_BILLING));
            in.putExtra(PaymentParams.POSTAL_CODE_BILLING, bundle.getString(PaymentParams.POSTAL_CODE_BILLING));
        }

        btnPayment.setOnClickListener(v -> {
            if(!etPhone.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty() && !etShippingAddress.getText().toString().isEmpty() && !etShippingCity.getText().toString().isEmpty() && !etShippingState.getText().toString().isEmpty() && !etShippingCountry.getText().toString().isEmpty() && !etShippingPostalcode.getText().toString().isEmpty()) {
                in.putExtra(PaymentParams.MERCHANT_EMAIL, "ahmedashrafofficial0@gmail.com"); //this a demo account for testing the sdk
                in.putExtra(PaymentParams.SECRET_KEY, "ooseCduX5xXEBczWUN2sRUyQiUB8Q2aeqDt2QY0WvXB6aa34X6xP15FkZhsQjC4JfyM6wM21KD8UOcZMiwCjCiDCz5sSIxCX5o4B");//Add your Secret Key Here
                in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
                in.putExtra(PaymentParams.TRANSACTION_TITLE, "Cart");

                in.putExtra(PaymentParams.CURRENCY_CODE, "EGP");
                in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, etPhone.getText().toString());
                in.putExtra(PaymentParams.CUSTOMER_EMAIL, etEmail.getText().toString());
//Shipping Address
                in.putExtra(PaymentParams.ADDRESS_SHIPPING, etShippingAddress.getText().toString());
                in.putExtra(PaymentParams.CITY_SHIPPING, etShippingCity.getText().toString());
                in.putExtra(PaymentParams.STATE_SHIPPING, etShippingState.getText().toString());
                in.putExtra(PaymentParams.COUNTRY_SHIPPING, etShippingCountry.getText().toString());
                in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, etShippingPostalcode.getText().toString()); //Put Country Phone code if Postal code not available '00973'

//Payment Page Style
                in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc");

//Tokenization
                in.putExtra(PaymentParams.IS_TOKENIZATION, true);
                startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
            }
        });

        return root;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
            Log.d("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.d("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            Toast.makeText(getActivity(), data.getStringExtra(PaymentParams.RESPONSE_CODE), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), data.getStringExtra(PaymentParams.TRANSACTION_ID), Toast.LENGTH_SHORT).show();
            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
                Log.d("Tag", data.getStringExtra(PaymentParams.TOKEN));
                Log.d("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
                Log.d("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
                Toast.makeText(getActivity(), data.getStringExtra(PaymentParams.CUSTOMER_EMAIL), Toast.LENGTH_SHORT).show();
            }
        }
    }
}