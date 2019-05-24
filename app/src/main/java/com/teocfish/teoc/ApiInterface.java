package com.teocfish.teoc;


import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiInterface {

    // API's endpoints
    @GET("/JSON/allproducts.php")
    public void getAllProducts(
            Callback<List<Product>> callback);

    @GET("/JSON/pbyc.php")
    public void getCategoryList(Callback<List<CategoryListResponse>> callback);

    @GET("/JSON/slider.php")
    public void getSliderList(Callback<List<SliderListResponse>> callback);

    @GET("/JSON/faq.php")
    public void getFAQ(Callback<FAQResponse> callback);

    @GET("/JSON/terms.php")
    public void getTerms(Callback<TermsResponse> callback);

    @FormUrlEncoded
    @POST("/JSON/pushadd.php")
    public void sendAccessToken(@Field("accesstoken") String accesstoken, Callback<RegistrationResponse> callback);

    @FormUrlEncoded
    @POST("/JSON/addwishlist.php")
    public void addToWishList(@Field("product_id") String product_id, @Field("user_id") String user_id, Callback<AddToWishlistResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/add_cart.php")
    public void addToCart(@Field("product_id") String product_id, @Field("userid") String user_id,
                          @Field("cartquantity") String cartquantity,
                          Callback<AddToWishlistResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/updatecart.php")
    public void updateCart(@Field("cartquantity") String cartquantity, @Field("iteamid") String iteamid, Callback<AddToWishlistResponse> callback);

    @FormUrlEncoded
    @POST("/JSON/wishcheck.php")
    public void checkWishList(@Field("product_id") String product_id, @Field("user_id") String user_id, Callback<AddToWishlistResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/wishlist.php")
    public void getWishList(@Field("user_id") String user_id, Callback<WishlistResponse> callback);



    @FormUrlEncoded
    @POST("/JSON/product.php")
    public void getProductDetails(@Field("product_id") String product_id, Callback<Product> callback);


    @FormUrlEncoded
    @POST("/JSON/vieworders.php")
    public void getMyOrders(@Field("user_id") String user_id, Callback<MyOrdersResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/viewcart.php")
    public void getCartList(@Field("user_id") String user_id, Callback<CartistResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/userprofile.php")
    public void getUserProfile(@Field("user_id") String user_id, Callback<UserProfileResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/updateprofile.php")
    public void updateProfile(@Field("user_id") String user_id,
                              @Field("name") String name,
                              @Field("city") String city,
                              @Field("state") String state,
                              @Field("pincode") String pincode,
                              @Field("local") String local,
                              @Field("flat") String flat,
                              @Field("gender") String gender,
                              @Field("phone") String phone,
                              @Field("landmark") String landmark,
                              Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/resentmail.php")
    public void resentEmail(@Field("email") String email, Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/login.php")
    public void
    login(@Field("email") String email,
          @Field("password") String password,
          @Field("logintype") String logintype,
          Callback<SignUpResponse> callback);
    
    @FormUrlEncoded
    @POST("/JSON/addorders.php")
    public void addOrder(@Field("user_id") String user_id,
                         @Field("cart_id") String cart_id,
                         @Field("address") String address,
                         @Field("phone") String phone,
                         @Field("paymentref") String paymentref,
                         @Field("paystatus") String paystatus,
                         @Field("total") String total,
                         @Field("paymentmode") String paymentmode,
                         Callback<SignUpResponse> callback);

@FormUrlEncoded
@GET("/JSON/order-cancel.php")
public void ordercancel(@Field("id") String id,
                        @Field("order_case") String order_case,
                        Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/forgot.php")
    public void forgotPassword(@Field("email") String email, Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/register.php")
    public void registration(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("logintype") String logintype, Callback<SignUpResponse> callback);


}
