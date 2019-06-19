package com.teocfish.teoc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import butterknife.ButterKnife;

public class DetailOrderedProductsListViewHolder extends RecyclerView.ViewHolder {

    ImageView image1;
    TextView productName1,qty,price;

    public DetailOrderedProductsListViewHolder(final Context context, View itemView, List<ModelProductList> modelProductListList) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        image1 = (ImageView) itemView.findViewById(R.id.productImage1);
        productName1 = (TextView) itemView.findViewById(R.id.productName1);
        qty = (TextView) itemView.findViewById(R.id.quantity);
        price = (TextView) itemView.findViewById(R.id.price);



    }
}
