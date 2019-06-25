package com.teocfish.teoc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import com.teocfish.teoc.activity.LoginActivity;
import com.teocfish.teoc.activity.SignUp;
import com.teocfish.teoc.utills.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyOrders extends Fragment {

    private Context tContext;
    View view;
    @BindView(R.id.myOrdersRecyclerView)
    RecyclerView myOrdersRecyclerView;
    public static MyOrdersResponse myOrdersResponseData;

    @BindView(R.id.emptyOrdersLayout)
    LinearLayout emptyOrdersLayout;
    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;
    @BindView(R.id.continueShopping)
    Button continueShopping;

    @BindView(R.id.verifyEmailLayout)
    LinearLayout verifyEmailLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        ButterKnife.bind(this, view);
        tContext = getContext();
        MainActivity.title.setText("My Orders");
        if (!MainActivity.userId.equalsIgnoreCase("")) {
            getMyOrders();
        } else {
            loginLayout.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @OnClick({R.id.continueShopping, R.id.loginNow, R.id.txtSignUp, R.id.verfiyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Config.moveTo(getActivity(), MainActivity.class);
                getActivity().finish();
                break;
            case R.id.loginNow:
                Config.moveTo(getActivity(), LoginActivity.class);
                break;
            case R.id.txtSignUp:
                Config.moveTo(getActivity(), SignUp.class);
                break;

            case R.id.verfiyNow:
                Config.moveTo(getActivity(), AccountVerification.class);
                break;
        }
    }
    public void getMyOrders() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().getMyOrders(MainActivity.userId, new Callback<MyOrdersResponse>() {
            @Override
            public void success(MyOrdersResponse myOrdersResponse, Response response) {
                pDialog.dismiss();
                if (myOrdersResponse.getSuccess().equalsIgnoreCase("true")) {
                    try {
                        myOrdersResponseData = myOrdersResponse;
                        if (!myOrdersResponse.getMessage().contains("No order histroy")) {
                            setProductsData();
                        }else {
                            emptyOrdersLayout.setVisibility(View.VISIBLE);

                        }

                    } catch (Exception e) {
                        emptyOrdersLayout.setVisibility(View.VISIBLE);
                    }
                }else {
                    verifyEmailLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                emptyOrdersLayout.setVisibility(View.VISIBLE);
                pDialog.dismiss();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Config.getCartList(getActivity(), true);
    }

    private void setProductsData() {
        LinearLayoutManager tManager;
        tManager = new LinearLayoutManager(getActivity());
        myOrdersRecyclerView.setLayoutManager(tManager);
        MyOrdersAdapter myOrdersAdapter = new MyOrdersAdapter(tContext, myOrdersResponseData);
        myOrdersRecyclerView.setAdapter(myOrdersAdapter);

    }
}
