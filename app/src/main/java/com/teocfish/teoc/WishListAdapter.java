package com.teocfish.teoc;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.teocfish.teoc.utills.Config;
import com.teocfish.teoc.utills.Constant;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class WishListAdapter extends RecyclerView.Adapter<HomeProductsViewHolder> {
    Context context;
    List<ModelProductList> modelProductListList;

    public WishListAdapter(Context context, List<ModelProductList> modelProductListList) {
        this.context = context;
        this.modelProductListList = modelProductListList;
    }

    @Override
    public HomeProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wish_list_items, null);
        HomeProductsViewHolder homeProductsViewHolder = new HomeProductsViewHolder(context, view, modelProductListList);
        return homeProductsViewHolder;
    }

    @Override
    public void onBindViewHolder(final HomeProductsViewHolder holder, final int position) {


        holder.cardView.setVisibility(View.GONE);
        holder.cardView1.setVisibility(View.VISIBLE);
        holder.productName1.setText(modelProductListList.get(position).getProductName());
        holder.price1.setText(MainActivity.currency + " " + modelProductListList.get(position).getSellprice());
        try {
            Log.d(Constant.TAG, "Images Url : "+ modelProductListList.get(position).getImages().get(0));
            Picasso.with(context)
                    .load(modelProductListList.get(position).getImages().get(0))
                    .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
        }
        try {
            double discountPercentage = Integer.parseInt(modelProductListList.get(position).getMrpprice()) - Integer.parseInt(modelProductListList.get(position).getSellprice());
//            Log.d("percentage", discountPercentage + "");
            discountPercentage = (discountPercentage / Integer.parseInt(modelProductListList.get(position).getMrpprice())) * 100;
            if ((int) Math.round(discountPercentage) > 0) {
                holder.discountPercentage1.setText(((int) Math.round(discountPercentage) + "% Off"));
            }
            holder.actualPrice1.setText(MainActivity.currency + " " + modelProductListList.get(position).getMrpprice());
            holder.actualPrice1.setPaintFlags(holder.actualPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }catch (Exception e){}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetail.modelProductListList.clear();
                ProductDetail.modelProductListList.addAll(modelProductListList);
                ProductDetail productDetail = new ProductDetail();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                productDetail.setArguments(bundle);
                ((MainActivity) context).loadFragment(productDetail, true);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToWishList(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelProductListList.size();
    }

    private void addToWishList(final int position) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().addToWishList(modelProductListList.get(position).getProductId(),
                MainActivity.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();

//                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            ((MainActivity) context).loadFragment(new MyWishList(), false);
                            Config.showCustomAlertDialog(context,
                                    "Your wishlist status",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.SUCCESS_TYPE);
                        }else {
                            Config.showCustomAlertDialog(context,
                                    "Your wishlist status",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.NORMAL_TYPE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

//                        Log.e("error", error.toString());
                    }
                });
    }
}
