package com.teocfish.teoc_fish_pvt_ltd;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teocfish.teoc_fish_pvt_ltd.utills.Constant;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderProductListAdapter extends RecyclerView.Adapter<OrderedProductsListViewHolder> {
    Context tContext;
    List<Product> tModels;

    public OrderProductListAdapter(Context tContext, List<Product> tModels) {
        this.tContext = tContext;
        this.tModels = tModels;
    }

    @Override
    public OrderedProductsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(tContext).inflate(R.layout.ordered_products_list_items1, null);
        OrderedProductsListViewHolder orderedProductsListViewHolder = new OrderedProductsListViewHolder(tContext, view, tModels);
        return orderedProductsListViewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderedProductsListViewHolder holder, final int position) {

        holder.productName1.setText(tModels.get(position).getProductName());
        final String strProId = tModels.get(position).getProductId();
        String strRate = tModels.get(position).getAvg_rating();
        String strRateStatus = tModels.get(position).getReview_status();

        if (strRateStatus.equals("1")){
            holder.tvWriteReview.setVisibility(View.GONE);
        }
            if (strRate != null && !strRate.isEmpty() && !strRate.equals("null")) {
                holder.rb_productRateReview.setRating(Float.parseFloat(strRate));
                holder.tvAvgRate.setText(strRate);
            } else {
                holder.rb_productRateReview.setVisibility(View.GONE);
            }

        try {
            Picasso.with(tContext)
                    .load(tModels.get(position).getImages().get(0))
                    .resize(Integer.parseInt(tContext.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(tContext.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
            holder.image1.setImageResource(R.drawable.defaultimage);
        }

        holder.tvWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(tContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_rate_review);
                dialog.setTitle("Rate and Review");
                dialog.setCancelable(true);

                // set the custom dialog components - text, image and button
                final EditText etReview =  dialog.findViewById(R.id.et_rateReview);
                final RatingBar rbRateReivew = dialog.findViewById(R.id.rb_rateReview);

                final Button btnRateReview =  dialog.findViewById(R.id.btn_rateReview);
                btnRateReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strRate = String.valueOf(rbRateReivew.getRating());
                        String strReview = etReview.getText().toString().trim();

                        if (strRate.equals(0.0)){
                            Toast.makeText(tContext, "Kindly rate the product", Toast.LENGTH_LONG).show();
                        }
                        else if (strReview.equals(""))
                        {
                            etReview.setError("Write your review before submit...");
                        }
                        else
                        {


                            Log.e(Constant.TAG, "Rate : "+strRate+"\nReview : "+strReview);
//                        final String strProId = myOrdersResponse.getOrderes().get(position).getOrdredproduct().get(0).getProductId();

//                        Config.rateReview(tContext, strRate, strReview, strProId);
                            final ProgressDialog progressDialog = new ProgressDialog(tContext);
                            progressDialog.setMessage("Please Wait");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            Api.getClient().rateReview(MainActivity.userId, strProId, strReview, strRate,
                                    new Callback<SignUpResponse>() {
                                        @Override
                                        public void success(SignUpResponse signUpResponse, Response response) {
                                            progressDialog.dismiss();
                                            dialog.dismiss();
                                            btnRateReview.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            progressDialog.dismiss();
                                            dialog.show();

                                        }
                                    });
                        }
//
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tModels.size();
    }

}
