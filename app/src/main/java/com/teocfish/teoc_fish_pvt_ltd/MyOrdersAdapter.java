package com.teocfish.teoc_fish_pvt_ltd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.teocfish.teoc_fish_pvt_ltd.utills.Config;
import com.teocfish.teoc_fish_pvt_ltd.utills.Constant;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersViewHolder> {

    Context tContext;
    MyOrdersResponse myOrdersResponse;

    public MyOrdersAdapter(Context tContext, MyOrdersResponse myOrdersResponse) {
        this.tContext = tContext;
        this.myOrdersResponse = myOrdersResponse;
    }

    @Override
    public MyOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(tContext).inflate(R.layout.my_orders_list_items, null);
        MyOrdersViewHolder MyOrdersViewHolder = new MyOrdersViewHolder(tContext, view);
        return MyOrdersViewHolder;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyOrdersViewHolder holder, final int position)
    {
        setProductsData(holder, position);
//        final String strProId = myOrdersResponse.getOrderes().get(position).getOrdredproduct().get(0).getProductId();
        holder.date.setText("Date: " + myOrdersResponse.getOrderes().get(position).getDate());
        String strStatus = myOrdersResponse.getOrderes().get(position).getOrderstatus();
        switch (strStatus) {
            case "Order Cancelled":
                holder.btnCancelOrder.setText("Order Cancelled");
                holder.btnCancelOrder.setEnabled(false);
                holder.btnCancelOrder.setBackgroundResource(R.drawable.bg_red);
                break;
            case "Dispatched":
                holder.btnCancelOrder.setText("Dispatched");
                holder.btnCancelOrder.setEnabled(false);
                holder.btnCancelOrder.setBackgroundResource(R.drawable.bg_golden);
                break;
            case "Completed":
                holder.btnCancelOrder.setText("Completed");
                holder.btnCancelOrder.setEnabled(false);
                holder.btnCancelOrder.setBackgroundResource(R.drawable.bg_green);
//            holder.tvWriteReview.setVisibility(View.VISIBLE);
                break;
                default:
                    holder.btnCancelOrder.setText("Cancel Order");
                    holder.btnCancelOrder.setEnabled(true);
                    holder.btnCancelOrder.setBackgroundResource(R.drawable.bg_btn_main);
        }
//        holder.tvWriteReview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(tContext);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.dialog_rate_review);
//                dialog.setTitle("Rate and Review");
//                dialog.setCancelable(true);
//
//                // set the custom dialog components - text, image and button
//                final EditText etReview =  dialog.findViewById(R.id.et_rateReview);
//                final RatingBar rbRateReivew = dialog.findViewById(R.id.rb_rateReview);
//
//                final Button btnRateReview =  dialog.findViewById(R.id.btn_rateReview);
//                btnRateReview.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String strRate = String.valueOf(rbRateReivew.getRating());
//                        String strReview = etReview.getText().toString().trim();
//
//                        if (strRate.equals(0.0)){
//                            Toast.makeText(tContext, "Kindly rate the product", Toast.LENGTH_LONG).show();
//                        }
//                        else if (strReview.equals(""))
//                        {
//                            etReview.setError("Write your review before submit...");
//                        }
//                        else
//                        {
//
//
//                        Log.e(Constant.TAG, "Rate : "+strRate+"\nReview : "+strReview);
////                        final String strProId = myOrdersResponse.getOrderes().get(position).getOrdredproduct().get(0).getProductId();
//
////                        Config.rateReview(tContext, strRate, strReview, strProId);
//                        final ProgressDialog progressDialog = new ProgressDialog(tContext);
//                        progressDialog.setMessage("Please Wait");
//                        progressDialog.setCancelable(false);
//                        progressDialog.show();
//
//                        Api.getClient().rateReview(MainActivity.userId, strProId, strReview, strRate,
//                                new Callback<SignUpResponse>() {
//                                    @Override
//                                    public void success(SignUpResponse signUpResponse, Response response) {
//                                        progressDialog.dismiss();
//                                        dialog.dismiss();
//                                        btnRateReview.setVisibility(View.GONE);
//                                    }
//
//                                    @Override
//                                    public void failure(RetrofitError error) {
//                                        progressDialog.dismiss();
//                                        dialog.show();
//
//                                    }
//                                });
//                        }
////
//                    }
//                });
//                dialog.show();
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                MyOrderedProductsDetailPage.orderes = myOrdersResponse.getOrderes();
                MyOrderedProductsDetailPage.pos = position;
                ((MainActivity) tContext).loadFragment(new MyOrderedProductsDetailPage(), true);
            }
        });
        holder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog alertDialog = new SweetAlertDialog(tContext, SweetAlertDialog.WARNING_TYPE);
                alertDialog.setTitleText("Cancel Order");
                alertDialog.setCancelText("No");
                alertDialog.setCancelClickListener( new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setConfirmText("Yes");
                alertDialog.setConfirmClickListener( new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ordercancel(tContext,position);
                        Toast.makeText(tContext,"Order Cancelled Successfully...",Toast.LENGTH_LONG).show();
                        Config.moveTo(tContext,OrderCanceled.class);
                    }
                });
                    alertDialog.setContentText("Do you really want to cancel the order?");
                alertDialog.show();
                Button btn =  alertDialog.findViewById(R.id.confirm_button);
                btn.setBackgroundColor(ContextCompat.getColor(tContext, R.color.colorPrimary));
                Button btn1 =  alertDialog.findViewById(R.id.cancel_button);
                btn1.setBackgroundColor(ContextCompat.getColor(tContext, R.color.colorPrimary));


            }
        });
    }

    @Override
    public int getItemCount()
    {
        return myOrdersResponse.getOrderes().size();
    }

    private void setProductsData(MyOrdersViewHolder holder, int position)
    {
        Log.d(Constant.TAG, myOrdersResponse.getOrderes().get(position).getOrdredproduct() + "");
//        Log.d(Constant.TAG, orderes.get(position).getOrdredproduct().size() + "");
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(tContext, 1);
        holder.orderedProductsRecyclerView.setLayoutManager(gridLayoutManager);
        OrderProductListAdapter myOrdersAdapter = new OrderProductListAdapter(tContext, myOrdersResponse.getOrderes().get(position).getOrdredproduct());
        holder.orderedProductsRecyclerView.setAdapter(myOrdersAdapter);
    }

    public void ordercancel(final Context context, int position)
    {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api.getClient().ordercancel(myOrdersResponse.getOrderes().get(position).getOrderid(),
                MainActivity.order_case,
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(context, OrderCanceled.class);
                        context.startActivity(intent);
                        ((Activity) context).finishAffinity();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
    }
}
