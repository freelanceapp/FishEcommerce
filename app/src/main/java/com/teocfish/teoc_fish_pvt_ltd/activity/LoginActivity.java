package com.teocfish.teoc_fish_pvt_ltd.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.teocfish.teoc_fish_pvt_ltd.AccountVerification;
import com.teocfish.teoc_fish_pvt_ltd.MainActivity;
import com.teocfish.teoc_fish_pvt_ltd.Api;
import com.teocfish.teoc_fish_pvt_ltd.ForgotPassword;
import com.teocfish.teoc_fish_pvt_ltd.R;
import com.teocfish.teoc_fish_pvt_ltd.SharedPrefManager;
import com.teocfish.teoc_fish_pvt_ltd.SignUpResponse;
import com.teocfish.teoc_fish_pvt_ltd.utills.Config;
import com.teocfish.teoc_fish_pvt_ltd.utills.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;

    @BindViews({R.id.etEmail, R.id.etPassword})
    protected List<EditText> editText;
//    @BindViews(R.id.etPassword)
//    protected EditText etPassword;
    @BindView(R.id.appIcon)
    ImageView appIcon;
    @BindView(R.id.btn_loginFacebook)
    LoginButton loginButton;
    @BindView(R.id.btn_loginGoogle)
    SignInButton btn_loginGoogle;
    GoogleApiClient mGoogleApiClient;
    Button sign;
    CallbackManager callbackManager;
    // GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 101;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getUserDetails();
        getGoogleLogin();
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        @SuppressLint("RestrictedApi")
//        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (alreadyloggedAccount != null) {
////            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
//            onLoggedIn(alreadyloggedAccount);
//        } else {
//            Log.d(Constant.TAG, "Not logged in");
//        }
//    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, SignUp.class);
        intent.putExtra(Constant.GOOGLE_ACCOUNT, googleSignInAccount);
        startActivity(intent);
        finish();
    }

    @SuppressLint("RestrictedApi")
    private void getGoogleLogin(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
//        Log.d(TAG,"AuthToken : "+ getResources().getString(R.string.web_client_id));
    }

    @OnClick(R.id.btn_loginGoogle)
    public void btn_loginGoogleClicked(View view){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void getUserDetails() {
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v(Constant.TAG, response.toString());

                                // Application code
                                try {
                                    String strId = object.getString("id");
                                    String strEmail = object.getString("email");
                                    String strName = object.getString("name"); // 01/31/1980 format
//                                    Log.d(Constant.TAG, "Email : "+strEmail);
//                                    Log.d(Constant.TAG, "Name : "+strName);
//                                    SharedPrefManager.saveUserData(LoginActivity.this, "email", strEmail);
//                                    SharedPrefManager.saveUserData(LoginActivity.this, "userId", strId + "");

//                                    AddToWishlistResponse addToWishlistResponse = new AddToWishlistResponse();
//                                    addToWishlistResponse.setSuccess("true");
//                                    String strResponse = addToWishlistResponse.getSuccess();
//                                    Log.e(Constant.TAG, "WishListResponse"+ strResponse);

//                                    Config.moveTo(LoginActivity.this, MainActivity.class);
//                                    Intent intent = new Intent(LoginActivity.this, SignUp.class);
//                                    intent.putExtra(Constant.USER_EMAIL, strEmail);
//                                    intent.putExtra(Constant.USER_NAME, strName);
//                                    intent.putExtra(Constant.LOGIN_TYPE, "facebook");
//                                    startActivity(intent);
//                                    finishAffinity();
                                    signUp(strEmail, strName, strId, "facebook");


                                } catch (JSONException e) {
                                    Log.d(Constant.TAG, "Email : "+e);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
                Log.v(Constant.TAG, "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v(Constant.TAG, "Error : "+exception);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == RC_SIGN_IN) {
                try {
                    Log.e(Constant.TAG, "Activity result");
                    @SuppressLint("RestrictedApi")
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    String idToken = account.getIdToken();
                    Log.e(Constant.TAG, "Token : " + idToken);
                    String strEmail = account.getEmail();
                    String stringName = account.getDisplayName();
                    String strId = account.getId();
//                    SharedPrefManager.saveUserData(LoginActivity.this, "email", account.getEmail());
//                    SharedPrefManager.saveUserData(LoginActivity.this, "userId", account.getId() + "");
//                    Intent intent = new Intent(this, SignUp.class);
//                    intent.putExtra(Constant.USER_EMAIL, account.getEmail());
//                    intent.putExtra(Constant.USER_NAME, account.getDisplayName());
//                    intent.putExtra(Constant.LOGIN_TYPE, "google");
//                    startActivity(intent);
//                    finish();
                    signUp(strEmail, stringName, strId, "google");
                    /*
                     Write to the logic send this id token to server using HTTPS
                     */
                } catch (ApiException e) {
                    Log.d(Constant.TAG, "signInResult:failed code=" + e.getStatusCode());
                }
            }



    }




    @OnClick({R.id.txtSignUp, R.id.txtForgotPassword, R.id.skipLoginLayout, R.id.signIn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignUp:
                Intent intentSignUp = new Intent(this, SignUp.class);
                intentSignUp.putExtra(Constant.LOGIN_TYPE, "email");
                startActivity(intentSignUp);
                break;
            case R.id.txtForgotPassword:
                Config.moveTo(LoginActivity.this, ForgotPassword.class);
                break;
            case R.id.skipLoginLayout:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("from", "skip");
                startActivity(intent);
                finishAffinity();
                break;

            case R.id.signIn:
                if (Config.validateEmail(editText.get(0), LoginActivity.this) && validatePassword(editText.get(1))) {
                    login();
                }
                break;
        }
    }

    private boolean validatePassword(EditText editText) {
        if (editText.getText().toString().trim().length() > 5) {
            return true;
        } else if (editText.getText().toString().trim().length() > 0) {
            editText.setError("Password must be of 6 characters");
            editText.requestFocus();
            return false;
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }
    private void signUp(final String strEmail, String strName, final String strId, String strLoginType) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        // sending gcm token to server
        Api.getClient().registration(strName,
               strEmail,
               strId,
                strLoginType,
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
//                        Log.d("signUpResponse", signUpResponse.getMessage());
//                        Toast.makeText(LoginActivity.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            SharedPrefManager.saveUserData(LoginActivity.this, "email",strEmail );
                            SharedPrefManager.saveUserData(LoginActivity.this, "userId", strId + "");
                            Config.moveTo(LoginActivity.this, MainActivity.class);
                            finishAffinity();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

//                        Log.e("error", error.toString());
                    }
                });
    }

    private void login() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        // sending gcm token to server
        Api.getClient().login(editText.get(0).getText().toString().trim(),
                editText.get(1).getText().toString().trim(),
                "email",
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("signUpResponse", signUpResponse.getUserid() + "");
                        Toast.makeText(LoginActivity.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            SharedPrefManager.saveUserData(LoginActivity.this, "email", editText.get(0).getText().toString());
                            SharedPrefManager.saveUserData(LoginActivity.this, "userId", signUpResponse.getUserid() + "");
                            Config.moveTo(LoginActivity.this, MainActivity.class);
                            finishAffinity();
                        } else if (signUpResponse.getSuccess().equalsIgnoreCase("notactive")) {
                            Config.moveTo(LoginActivity.this, AccountVerification.class);
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
