package com.teocfish.teoc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.orderedProductsRecyclerView)
    RecyclerView orderedProductsRecyclerView;
    @BindView(R.id.viewOrderDetails)
    TextView viewOrderDetails;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.orderCancel)
    Button Order_Cancel;
    public static int pos;

    public MyOrdersViewHolder(final Context context, View itemView)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);
        Order_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("Orderid"+MyOrderedProductsDetailPage.orderes.get(pos).getOrderid());
////                System.out.println(MyCartList.cartistResponseData.getCartid());
//                Config.ordercancel(context,"COD","COD");
////                Config.moveTo(context,OrderCanceled.class);
            }
        });
    }
}
