package com.teocfish.teoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Login extends AppCompatActivity {


    @BindViews({R.id.email, R.id.password})
    List<EditText> editTexts;
    @BindView(R.id.loginLinearLayout)
    LinearLayout loginLinearLayout;
    @BindView(R.id.appIcon)
    ImageView appIcon;
    GoogleApiClient mGoogleApiClient;
    Button sign;
    CallbackManager callbackManager;
    // GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 007;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(Login.this)
                .enableAutoManage(Login.this /* FragmentActivity */, null /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        //  mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        sign = (Button) findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_btn);
        loginButton.setReadPermissions("email");

        System.out.println("Key hash---   " + printKeyHash(Login.this));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }


            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

//            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
//                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
//            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
//            Log.e("Exception", e.toString());
        }

        return key;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        try {
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                if (requestCode == RC_SIGN_IN) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                     //   Toast.makeText(getApplicationContext(), "user's FB Info---  " + object.toString(), Toast.LENGTH_SHORT).show();
//                        System.out.println("user's FB Info---  " + object.toString());

                            signUpViaFBAndGmail(object.getString("name"),object.getString("email"),"","fb");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email, picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    private void handleSignInResult(GoogleSignInResult result) {
        //  Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfolly, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
           // Toast.makeText(getApplicationContext(), "User name---   " + acct.getDisplayName(), Toast.LENGTH_SHORT).show();
//            System.out.println("User name---   " + acct.getDisplayName());

            signUpViaFBAndGmail(acct.getDisplayName(),acct.getEmail(),"","gmail");

        }
    }

    /*   private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
           try {
               GoogleSignInAccount account = completedTask.getResult(ApiException.class);

               // Signed in successfully, show authenticated UI.
             //  updateUI(account);
           } catch (ApiException e) {
               // The ApiException status code indicates the detailed failure reason.
               // Please refer to the GoogleSignInStatusCodes class reference for more information.
              // Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
               System.out.println("signInResult:failed code=" + e.getStatusCode());
              // updateUI(null);
           }
       }*/
    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnClick({R.id.txtSignUp, R.id.txtForgotPassword, R.id.skipLoginLayout, R.id.signIn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignUp:
                Config.moveTo(Login.this, SignUp.class);
                break;
            case R.id.txtForgotPassword:
                Config.moveTo(Login.this, ForgotPassword.class);
                break;
            case R.id.skipLoginLayout:
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("from", "skip");
                startActivity(intent);
                finishAffinity();
                break;

            case R.id.signIn:
                if (Config.validateEmail(editTexts.get(0), Login.this) && validatePassword(editTexts.get(1))) {
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

    private void login() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        // sending gcm token to server
        Api.getClient().login(editTexts.get(0).getText().toString().trim(),
                editTexts.get(1).getText().toString().trim(),
                "email",
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("signUpResponse", signUpResponse.getUserid() + "");
                        Toast.makeText(Login.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            Common.saveUserData(Login.this, "email", editTexts.get(1).getText().toString());
                            Common.saveUserData(Login.this, "userId", signUpResponse.getUserid() + "");
                            Config.moveTo(Login.this, MainActivity.class);
                            finishAffinity();
                        } else if (signUpResponse.getSuccess().equalsIgnoreCase("notactive")) {
                            Config.moveTo(Login.this, AccountVerification.class);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();
//                        Log.e("error", error.toString());
                    }
                });
    }


    private void signUpViaFBAndGmail(String name, final String email, String password, String login_type) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        // sending gcm token to server
        //System.out.println("data....   "+name+"   "+email+"   "+login_type);
        Api.getClient().registration(name, email,"123456g","email",
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        try {
                            pDialog.dismiss();
                            Log.d("signUpResponse", signUpResponse.getMessage());
                          //  Toast.makeText(Login.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {

                                Common.saveUserData(Login.this, "email", email);
                                Common.saveUserData(Login.this, "userId", signUpResponse.getUserid() + "");
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("from", "signUp");
                                startActivity(intent);
//                            Config.moveTo(SignUp.this, MainActivity.class);
                                finishAffinity();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
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
