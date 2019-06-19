package com.teocfish.teoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.teocfish.teoc.utills.Config;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



public class CartListAdapter extends RecyclerView.Adapter<CartListViewHolder> {
    Context context;
    List<ModelProductList> modelProductListList;
    double totalAmount = 0f, amountPayable;
    public static String totalAmountPayable;
    double tax=0f;
    public CartListAdapter(Context context, List<ModelProductList> modelProductListList) {
        this.context = context;
        this.modelProductListList = modelProductListList;
    }

    @Override
    public CartListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_items, null);
        CartListViewHolder CartListViewHolder = new CartListViewHolder(context, view, modelProductListList);
        return CartListViewHolder;
    }

    @Override
    public void onBindViewHolder(final CartListViewHolder holder, final int position)
    {
        totalAmount = totalAmount + (Double.parseDouble(modelProductListList.get(position).getSellprice()) * Double.parseDouble(modelProductListList.get(position).getQuantity()));
        if (position == modelProductListList.size() - 1) {
            holder.totalAmount.setVisibility(View.VISIBLE);
            holder.txtGurantee.setText(Html.fromHtml(context.getResources().getString(R.string.secure_payment_text)));

            holder.textViews.get(0).setText("Price (" + modelProductListList.size() + " items)");
            holder.textViews.get(1).setText(MainActivity.currency + " " + totalAmount);
            if (MyCartList.cartistResponseData.getShipping().length()>0) {

                holder.textViews.get(2).setText(MainActivity.currency + " " + MyCartList.cartistResponseData.getShipping());
                amountPayable = totalAmount +
                        Double.parseDouble(MyCartList.cartistResponseData.getShipping());
            }else {
                amountPayable=totalAmount;
                holder.textViews.get(2).setText(MainActivity.currency + " 0.0");

            }
            if (MyCartList.cartistResponseData.getTax().length()>0) {
                tax = (totalAmount / 100) * Double.parseDouble(MyCartList.cartistResponseData.getTax());
                holder.textViews.get(5).setText("Tax (" + MyCartList.cartistResponseData.getTax() + "%)");
            }
            tax = Double.parseDouble(String.format("%.2f", tax));
//            Log.d("floatTax", tax + "");
            holder.textViews.get(3).setText(MainActivity.currency + " " + tax);
            holder.textViews.get(4).setText(MainActivity.currency + " " + (String.format("%.2f", (amountPayable + tax))));
            totalAmountPayable = (String.format("%.2f", (amountPayable + tax)));
//            Log.d("totalAmountPayable", totalAmountPayable);
        } else
            holder.totalAmount.setVisibility(View.GONE);

        holder.productName1.setText(modelProductListList.get(position).getProductName());
        holder.price1.setText(MainActivity.currency + " " + modelProductListList.get(position).getSellprice());
        holder.quantity.setText("Qty: " + modelProductListList.get(position).getQuantity());
        try {
            Picasso.with(context)
                    .load(modelProductListList.get(position).getImages().get(0))
                    .resize(Integer.parseInt(context.getResources().getString(R.string.cartImageWidth)),Integer.parseInt(context.getResources().getString(R.string.cartImageWidth)))
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
        }

//        if (!modelProductListList.get(position).getSize().equalsIgnoreCase("")) {
//            Log.d("size", modelProductListList.get(position).getSize());
//            holder.size.setText("Size: " + modelProductListList.get(position).getSize());
//            holder.size.setVisibility(View.VISIBLE);
//        } else {
//            holder.size.setVisibility(View.GONE);
//        }
//        if (!modelProductListList.get(position).getProductColor().equalsIgnoreCase("")) {
//            Log.d("color", modelProductListList.get(position).getProductColor());
//            holder.color.setText("Color: " + modelProductListList.get(position).getProductColor());
//            holder.color.setVisibility(View.VISIBLE);
//        } else {
//            holder.color.setVisibility(View.GONE);
//        }
        try {
            double discountPercentage = Integer.parseInt(modelProductListList.get(position).getMrpprice()) - Integer.parseInt(modelProductListList.get(position).getSellprice());
//            Log.d("percentage", discountPercentage + "");
            discountPercentage = (discountPercentage / Integer.parseInt(modelProductListList.get(position).

                    getMrpprice())) * 100;
            if ((int) Math.round(discountPercentage) > 0)

            {
                holder.discountPercentage1.setText(((int) Math.round(discountPercentage) + "% Off"));
            }
            holder.actualPrice1.setText(MainActivity.currency + " " + modelProductListList.get(position).getMrpprice());
            holder.actualPrice1.setPaintFlags(holder.actualPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } catch (Exception e) {
        }
        holder.productName1.setOnClickListener(new View.OnClickListener()

        {
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
        holder.delete.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                updateCart(position, 0 + "");
            }
        });
        final PopupMenu popupMenu = new PopupMenu(context, holder.quantity);
        popupMenu.getMenuInflater().

                inflate(R.menu.textview_popup_menu, popupMenu.getMenu());
//        Log.d("productQuantity", Integer.parseInt(modelProductListList.get(position).getQuantity()) + "");
        for (int i = 1; i <= Integer.parseInt(modelProductListList.get(position).getPlimit()); i++)

        {
            popupMenu.getMenu().add(i + "");
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()

        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (!modelProductListList.get(position).getQuantity().trim().equalsIgnoreCase(menuItem.getTitle().toString().trim())) {
                    holder.quantity.setText("Qty: " + menuItem.getTitle() + "");
                    updateCart(position, menuItem.getTitle().toString());
                }
                return false;
            }
        });
        holder.quantity.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelProductListList.size();
    }

    private void updateCart(final int position, final String quantity) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Api.getClient().updateCart(
                quantity,
                modelProductListList.get(position).getIteam_id(),
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        progressDialog.dismiss();
//                        Log.d("addToCartResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
//                            Config.getCartList(context);
                            ((MainActivity) context).loadFragment(new MyCartList(), false);

                            Config.showCustomAlertDialog(context,
                                    "Your Cart status",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.SUCCESS_TYPE);
                        } else {

                            Config.showCustomAlertDialog(context,
                                    "Your Cart status",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.NORMAL_TYPE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();

//                        Log.e("error", error.toString());
                    }
                });
    }
}
