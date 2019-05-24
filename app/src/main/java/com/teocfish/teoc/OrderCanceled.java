package com.teocfish.teoc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderCanceled extends AppCompatActivity {
@BindView(R.id.continueShopping_orderCancel)
    Button continueShopping_orderCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_canceled);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.continueShopping_orderCancel)
    public void onClick(View view) {

        Intent intent = new Intent(OrderCanceled.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
