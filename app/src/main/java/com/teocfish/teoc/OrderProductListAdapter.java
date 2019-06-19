package com.teocfish.teoc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.teocfish.teoc.utills.Constant;

import java.util.List;

public class OrderProductListAdapter extends RecyclerView.Adapter<OrderedProductsListViewHolder> {
    Context context;
    List<ModelProductList> tModels;

    public OrderProductListAdapter(Context context, List<ModelProductList> tModels) {
        this.context = context;
        this.tModels = tModels;
    }

    @Override
    public OrderedProductsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ordered_products_list_items1, null);
        OrderedProductsListViewHolder orderedProductsListViewHolder = new OrderedProductsListViewHolder(context, view, tModels);
        return orderedProductsListViewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderedProductsListViewHolder holder, final int position) {

        holder.productName1.setText(tModels.get(position).getProductName());

        try {
            Log.d(Constant.TAG, "Images Url : "+ tModels.get(position).getImages().get(0));
            Picasso.with(context)
                    .load(tModels.get(position).getImages().get(0))
                    .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
            holder.image1.setImageResource(R.drawable.defaultimage);
        }

    }

    @Override
    public int getItemCount() {
        return tModels.size();
    }

}
