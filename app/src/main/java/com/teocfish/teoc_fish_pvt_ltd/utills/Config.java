package com.teocfish.teoc_fish_pvt_ltd.utills;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.teocfish.teoc_fish_pvt_ltd.Api;
import com.teocfish.teoc_fish_pvt_ltd.CartListAdapter;
import com.teocfish.teoc_fish_pvt_ltd.CartListResponse;
import com.teocfish.teoc_fish_pvt_ltd.ChoosePaymentMethod;
import com.teocfish.teoc_fish_pvt_ltd.activity.LoginActivity;
import com.teocfish.teoc_fish_pvt_ltd.MainActivity;
import com.teocfish.teoc_fish_pvt_ltd.MyCartList;
import com.teocfish.teoc_fish_pvt_ltd.MyOrderedProductsDetailPage;
import com.teocfish.teoc_fish_pvt_ltd.OrderCanceled;
import com.teocfish.teoc_fish_pvt_ltd.OrderConfirmed;
import com.teocfish.teoc_fish_pvt_ltd.R;
import com.teocfish.teoc_fish_pvt_ltd.activity.SignUp;
import com.teocfish.teoc_fish_pvt_ltd.SignUpResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Config {
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    // id to handle the notification in the notification tray
    public static final String SHARED_PREF = "ah_firebase";

    public static void moveTo(Context context, Class targetClass) {
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
    }
    public static boolean validateEmail(EditText editText,Context context) {
        String email = editText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            editText.setError(context.getString(R.string.err_msg_email));
            editText.requestFocus();
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static void showCustomAlertDialog(Context context, String title, String msg,int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public static void showLoginCustomAlertDialog(final Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);
        alertDialog.setCancelText("LoginActivity");
        alertDialog.setCancelClickListener( new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Config.moveTo(context, LoginActivity.class);

            }
        });
        alertDialog.setConfirmText("Signup");
        alertDialog.setConfirmClickListener( new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Config.moveTo(context, SignUp.class);

            }
        });
        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    public static void getCartList(final Context context, final boolean b) {
        if (b)
            MainActivity.progressBar.setVisibility(View.VISIBLE);
        MainActivity.cartCount.setVisibility(View.GONE);
        Api.getClient().getCartList(MainActivity.userId, new Callback<CartListResponse>() {
            @Override
            public void success(CartListResponse cartListResponse, Response response) {
                MainActivity.progressBar.setVisibility(View.GONE);
                try {
                    if (cartListResponse.getProducts().size() <= 0) {
                        MainActivity.cartCount.setVisibility(View.GONE);
                    } else {
                        MainActivity.cartCount.setText(cartListResponse.getProducts().size() + "");
                        if (!b) {
//                            Log.d("equals", "equals");
                            MainActivity.cartCount.setVisibility(View.GONE);

                        } else {
                            MainActivity.cartCount.setVisibility(View.VISIBLE);

                        }
                    }
                } catch (Exception e) {
                    MainActivity.cartCount.setVisibility(View.GONE);

                }

            }

            @Override
            public void failure(RetrofitError error) {
                MainActivity.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public static void addOrder(final Context context, String transactionId, String paymentMode) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api.getClient().addOrder(MainActivity.userId,
                MyCartList.cartListResponseData.getCartid(),
                ChoosePaymentMethod.address,
                ChoosePaymentMethod.mobileNo,
                transactionId,
                "succeeded",
                CartListAdapter.totalAmountPayable,
                paymentMode,
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(context, OrderConfirmed.class);
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
    public static void rateReview(final Context context, String strRate, String strReview, String strProdId) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api.getClient().rateReview(MainActivity.userId,
                strProdId,
                strRate,
                strReview,
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
    }

   public static void ordercancel(final Context context,final int position)
   {
       final ProgressDialog progressDialog = new ProgressDialog(context);
       progressDialog.setMessage("Please Wait");
       progressDialog.setCancelable(false);
       progressDialog.show();

       Api.getClient().ordercancel(
               MyOrderedProductsDetailPage.orderes.get(position).getOrderid(),
               MainActivity.order_case,
               new Callback<SignUpResponse>() {
                   @Override
                   public void success(SignUpResponse signUpResponse, Response response) {
                       progressDialog.dismiss();
//                       Config.moveTo(context,OrderCanceled.class);
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