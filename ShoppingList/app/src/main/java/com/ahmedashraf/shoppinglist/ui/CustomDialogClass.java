package com.ahmedashraf.shoppinglist.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.ahmedashraf.shoppinglist.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    private Dialog d;
    private Button btnYes, btnNo;
    private boolean Yes;

    public boolean isYes() {
        return Yes;
    }
    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                Yes = true;
                dismiss();
                break;
            case R.id.btn_no:
                Yes = false;
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}