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
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.btnOrderCancel)
    Button btnCancelOrder;
//    @BindView(R.id.tvWriteReview)
//    Button tvWriteReview;
    public static int pos;

    public MyOrdersViewHolder(final Context context, View itemView)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
