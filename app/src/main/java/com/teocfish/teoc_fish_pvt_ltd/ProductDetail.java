package com.teocfish.teoc_fish_pvt_ltd;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.teocfish.teoc_fish_pvt_ltd.utills.Config;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProductDetail extends Fragment {

    View view;
    private static ViewPager mPager;
    int position;
    @BindView(R.id.dotsRecyclerView)
    RecyclerView dotsRecyclerView;
    public static DotsAdapter dotsAdapter;
    Activity activity;
    ArrayList<String> sliderImages = new ArrayList<>();
    @BindViews({R.id.productName, R.id.price, R.id.actualPrice, R.id.discountPercentage, R.id.quantity, R.id.status})
    List<TextView> textViews;
    public static List<Product> productList = new ArrayList<>();
    @BindView(R.id.productDescWebView)
    WebView productDescWebView;
    TextView addToWishList;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;
    public static Button addToCart;
    public static String productQuantity;
    @BindView(R.id.noImageAdded)
    ImageView noImageAdded;

    @BindView(R.id.ratingProducts)
    protected RatingBar ratingProducts;
    @BindView(R.id.total_rating)
    protected TextView total_rating;
    @BindView(R.id.total_ratings_txt)
    protected TextView total_rating_txt;
    @BindView(R.id.rb_rateProdDetail)
    protected RatingBar rb_rateProdDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ButterKnife.bind(this, view);
        activity = (Activity) view.getContext();
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        addToCart = (Button) view.findViewById(R.id.addToCart);
        addToWishList = (TextView) view.findViewById(R.id.addToWishList);
        getProductDetails();
        setData();
        checkWishList();
        return view;
    }

    @OnClick({R.id.addToWishListLayout, R.id.addToWishList, R.id.addToCart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addToWishList:
            case R.id.addToWishListLayout:
                if (!MainActivity.userId.equalsIgnoreCase("")) {
                    addToWishList();
                } else {

                    Config.showLoginCustomAlertDialog(getActivity(),
                            "LoginActivity To Continue",
                            "Please login to add product in your wishlist",
                            SweetAlertDialog.WARNING_TYPE);

                }
                break;
            case R.id.addToCart:
                if (!MainActivity.userId.equalsIgnoreCase("")) {
                    if (addToCart.getText().toString().trim().equalsIgnoreCase("Add To Cart")) {
                        addToCart();
                    } else if (addToCart.getText().toString().trim().equalsIgnoreCase("Out of Stock")) {
                        Config.showCustomAlertDialog(getActivity(),
                                "Out Of Stock",
                                "This Product is out of stock.",
                                SweetAlertDialog.ERROR_TYPE);

                    } else {
                        ((MainActivity) getActivity()).loadFragment(new MyCartList(), true);
                    }
                } else {

                    Config.showLoginCustomAlertDialog(getActivity(),
                            "LoginActivity To Continue",
                            "Please login to add product in your cart",
                            SweetAlertDialog.WARNING_TYPE);
                }
                break;
        }

    }

    private void addToWishList() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().addToWishList(productList.get(position).getProductId(),
                MainActivity.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();


//                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            Config.showCustomAlertDialog(getActivity(),
                                    addToWishlistResponse.getMessage(),
                                    "",
                                    SweetAlertDialog.SUCCESS_TYPE);
                            checkWishList();
                        } else {
                            final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText(addToWishlistResponse.getMessage());
                            alertDialog.setConfirmText("Verify Now");
                            alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    alertDialog.dismissWithAnimation();
                                    Config.moveTo(getActivity(), AccountVerification.class);

                                }
                            });
                            alertDialog.show();
                            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

//                        Log.e("error", error.toString());
                    }
                });
    }

    private void addToCart() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().addToCart(productList.get(position).getProductId(),
                MainActivity.userId, "1",
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();

                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            addToCart.setText("Go to Cart");
                            Config.getCartList(getActivity(), true);

                            Config.showCustomAlertDialog(getActivity(),
                                    addToWishlistResponse.getMessage(),
                                    "",
                                    SweetAlertDialog.SUCCESS_TYPE);
                        }
                        else {
                            final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText(addToWishlistResponse.getMessage());
                            alertDialog.setConfirmText("Verify Now");
                            alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    alertDialog.dismissWithAnimation();
                                    Config.moveTo(getActivity(), AccountVerification.class);

                                }
                            });
                            alertDialog.show();
                            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

//                        Log.e("error", error.toString());
                    }
                });
    }

    private void checkWishList() {
        progressBar.setVisibility(View.VISIBLE);
        addToWishList.setVisibility(View.GONE);
        Api.getClient().checkWishList(productList.get(position).getProductId(),
                MainActivity.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {

                        progressBar.setVisibility(View.GONE);
                        addToWishList.setVisibility(View.VISIBLE);
//                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            addToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorited_icon, 0, 0, 0);
                        } else
                            addToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unfavorite_icon, 0, 0, 0);


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressBar.setVisibility(View.GONE);
                        addToWishList.setVisibility(View.VISIBLE);
//                        Log.e("error", error.toString());
                    }
                });
    }

    private void getProductDetails() {
        progressBar1.setVisibility(View.VISIBLE);
        addToCart.setVisibility(View.GONE);
        Api.getClient().getProductDetails(productList.get(position).getProductId(),
                new Callback<Product>() {
                    @Override
                    public void success(Product product, Response response) {

                        progressBar1.setVisibility(View.GONE);
                        addToCart.setVisibility(View.VISIBLE);

                        String strRate = product.getAvg_rating();
                        if (strRate != null && !strRate.isEmpty() && !strRate.equals("null")) {
                            rb_rateProdDetail.setRating(Float.parseFloat(strRate));
                            total_rating.setText(strRate);
                        } else {
                            rb_rateProdDetail.setVisibility(View.GONE);
                            total_rating.setVisibility(View.GONE);
                        }
                        if (product.getRate_count().equals("0")){
                            total_rating.setVisibility(View.GONE);
                        }
                        total_rating_txt.setText(product.getRate_count() +" ratings");
//                        Log.d("productDetailsResponse", product.getProductId() + "" + product.toString());
                        if (Integer.parseInt(product.getQuantity()) < 1) {
                            textViews.get(4).setText("Out of Stock");
                            addToCart.setBackgroundColor(Color.parseColor("#80148cbf"));
                            addToCart.setText("Out of Stock");
                        } else if (Integer.parseInt(product.getQuantity()) > 0 && Integer.parseInt(product.getQuantity()) < 10) {
                            textViews.get(4).setText("Hurry, only " + product.getQuantity() + " left");
                        } else textViews.get(4).setVisibility(View.GONE);
                        textViews.get(5).setText(product.getStatus());

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressBar1.setVisibility(View.GONE);
                        addToCart.setVisibility(View.VISIBLE);
//                        Log.e("error", error.toString());
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.search.setVisibility(View.VISIBLE);
        MainActivity.cart.setVisibility(View.VISIBLE);
        MainActivity.title.setText("");
        Config.getCartList(getActivity(), true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.search.setVisibility(View.GONE);
    }


    private void setData() {
        sliderImages = new ArrayList<>();
        try {
            sliderImages.addAll(productList.get(position).getImages());
            if (sliderImages.size() > 0) {
                init();
                noImageAdded.setVisibility(View.GONE);
            } else {
                noImageAdded.setVisibility(View.VISIBLE);
            }
        }catch (Exception e)
        {
            noImageAdded.setVisibility(View.VISIBLE);
        }
        setDots(0);
        productQuantity = productList.get(position).getQuantity();
        productDescWebView.loadDataWithBaseURL(null, productList.get(position).getDescription(), "text/html", "utf-8", null);
        textViews.get(0).setText(productList.get(position).getProductName());
        textViews.get(1).setText(MainActivity.currency + " " + productList.get(position).getSellprice());
        try {
            double discountPercentage = Integer.parseInt(productList.get(position).getMrpprice()) - Integer.parseInt(productList.get(position).getSellprice());
//            Log.d("percentage", discountPercentage + "");
            discountPercentage = (discountPercentage / Integer.parseInt(productList.get(position).getMrpprice())) * 100;
            if ((int) Math.round(discountPercentage) > 0) {
                textViews.get(3).setText(((int) Math.round(discountPercentage) + "% Off"));
            }
            textViews.get(2).setText(MainActivity.currency + " " + productList.get(position).getMrpprice());
            textViews.get(2).setPaintFlags(textViews.get(2).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } catch (Exception e) {
        }

    }

    private void init() {
        mPager = (ViewPager) view.findViewById(R.id.pager);
        DetailPageSliderPagerAdapter mAdapter = new DetailPageSliderPagerAdapter(getChildFragmentManager(), sliderImages);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(mPager.getChildCount() * MyPagerAdapter.LOOPS_COUNT / 2, false); // set current item in the adapter to middle
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % sliderImages.size();
//                Log.d("onPageSelected", position + "");
                setDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setDots(int selectedPos) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        dotsRecyclerView.setLayoutManager(linearLayoutManager);
        dotsAdapter = new DotsAdapter(activity, sliderImages.size(), selectedPos);
        dotsRecyclerView.setAdapter(dotsAdapter);

    }
}
