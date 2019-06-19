package com.teocfish.teoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.teocfish.teoc.utills.Config;
import com.teocfish.teoc.utills.Constant;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersViewHolder> {

    Context context;
    List<Ordere> orderes;

    public MyOrdersAdapter(Context context, List<Ordere> orderes) {
        this.context = context;
        this.orderes = orderes;
    }

    @Override
    public MyOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_orders_list_items, null);
        MyOrdersViewHolder MyOrdersViewHolder = new MyOrdersViewHolder(context, view);
        return MyOrdersViewHolder;
    }
    @Override
    public void onBindViewHolder(MyOrdersViewHolder holder, final int position)
    {
        setProductsData(holder, position);
        holder.date.setText("Date: " + orderes.get(position).getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                System.out.println("position"+position);
                MyOrderedProductsDetailPage.orderes = orderes;
                MyOrderedProductsDetailPage.pos = position;
                ((MainActivity) context).loadFragment(new MyOrderedProductsDetailPage(), true);
            }
        });
        holder.Order_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.moveTo(context,OrderCanceled.class);
//                System.out.println("ordrid"+orderes.get(position).getOrderid());
//                Config.ordercancel(context,position);
//                ordercancel(context,position);
//                Toast.makeText(context,"Order Id : "+orderes.get(position).getOrderid(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return orderes.size();
    }

    private void setProductsData(MyOrdersViewHolder holder, int position)
    {
        Log.d(Constant.TAG, orderes.get(position).getOrdredproduct() + "");
//        Log.d(Constant.TAG, orderes.get(position).getOrdredproduct().size() + "");
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(context, 1);
        holder.orderedProductsRecyclerView.setLayoutManager(gridLayoutManager);
        OrderProductListAdapter myOrdersAdapter = new OrderProductListAdapter(context, orderes.get(position).getOrdredproduct());
        holder.orderedProductsRecyclerView.setAdapter(myOrdersAdapter);
    }

    public void ordercancel(final Context context, int position)
    {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api.getClient().ordercancel(orderes.get(position).getOrderid(),
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
