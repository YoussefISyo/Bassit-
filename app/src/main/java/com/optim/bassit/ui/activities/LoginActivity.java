package com.optim.bassit.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Customer;
import com.optim.bassit.utils.LocaleHelper;
import com.optim.bassit.utils.OptimTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @Inject
    AppApi appApi;
    private static final int RC_SIGN_IN = 1037;

    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;

    @BindView(R.id.t_email)
    EditText tEmail;
    @BindView(R.id.t_pwd)
    EditText tPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        //************* GOOGLE **********************
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(getString(R.string.server_client_id))
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //************* FB **********************

        if (LocaleHelper.isRTL())
            tPwd.setGravity(Gravity.RIGHT);
        callbackManager = CallbackManager.Factory.create();
        try {
            LoginManager.getInstance().logOut();

        } catch (Exception ex) {
        }
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                (object, response) -> {
                                    try {
                                        String email = "";
                                        if (!object.has("email"))
                                            email = OptimTools.generateRandomStringUnique() + "@bassit-app.com";
                                        else
                                            email = object.getString("email");

                                        String first_name = object.getString("first_name");
                                        String last_name = object.getString("last_name");
                                        Customer customer = new Customer();
                                        customer.setPrenom(last_name);
                                        customer.setName(first_name);
                                        customer.setEmail(email);
                                        customer.setAccess_token(loginResult.getAccessToken().getToken());
                                        FacebookSignIn(customer);
                                    } catch (JSONException e) {


                                    }

                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "first_name,last_name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Alert("Canceled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Alert(exception.getMessage());
                    }
                });


        findViewById(R.id.b_resetpwd).setOnClickListener(v -> {

            InputText(this, R.string.email, R.string.text_send, R.string.text_cancel, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, (s) -> {
                if (!OptimTools.isEmailValid(s)) {
                    Alert("Adresse email invalide !");
                    return;
                }
                show();
                fullyHandleResponse(appApi.resetPWD(s), new handleResponse() {
                    @Override
                    public void onSuccess() {
                        Alert("Votre nouveau mot de passe a été envoyé par email !");
                        hide();
                    }

                    @Override
                    public void onFail(String error) {
                        hide();
                    }
                });
            });
        });

        findViewById(R.id.textView29).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bassit-app.com/privacy"));
                startActivity(browserIntent2);
            }
        });
    }

    @OnClick(R.id.b_google)
    public void onGoogle() {
        try {
            mGoogleSignInClient.signOut();
        } catch (Exception ex) {

        }

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void FacebookSignIn(Customer customer) {
        appApi.facebookLogin(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && !response.body().isError())
                    doLogin(response, LoginActivity.this, appApi);
                else if (response.isSuccessful())
                    Alert(response.body().getMessage());
                else
                    Alert(response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(R.string.messages_erreur_connexion);
            }
        });
    }

    private void GoogleSignIn(Customer customer) {
        appApi.googleLogin(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && !response.body().isError())
                    doLogin(response, LoginActivity.this, appApi);
                else if (response.isSuccessful())
                    Alert(response.body().getMessage());
                else
                    Alert(response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(R.string.messages_erreur_connexion);
            }
        });
    }

    @OnClick(R.id.b_inscrire)
    public void InscrireClick() {
        Intent myintent = new Intent(this, PhoneActivity.class);
        startActivity(myintent);
    }

    @OnClick(R.id.b_connexion)
    public void ConnexionClick() {

        if (tEmail.getText().toString().matches("") || tPwd.getText().toString().matches("")) {
            Alert(R.string.message_fill_required_fields);
            return;
        } else if (!OptimTools.isEmailValid(tEmail.getText().toString())) {
            Alert(R.string.message_email_invalid);
            return;
        } else {
            Customer customer = new Customer(tEmail.getText().toString(), tPwd.getText().toString());
            show();
            appApi.loginCustomer(customer).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    hide();
                    if (!response.isSuccessful() || response.body().isError())
                        Alert(response.body().getMessage());
                    else {

                        doLogin(response, LoginActivity.this, appApi);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    hide();
                    Alert(t.getMessage());
                }
            });
        }
    }

    public static void doLogin(Response<ApiResponse> response, BaseActivity ctx, AppApi appapi) {
        String token = response.body().getData();
        ctx.getSharedPreferences("_", MODE_PRIVATE).edit().putString("apitoken", token).apply();
        CurrentUser.getInstance().setApitoken(token);

        appapi.customerProfile(0).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (!response.isSuccessful())
                    ctx.Alert(R.string.error_cant_get_profile);
                else {
                    CurrentUser.getInstance().setmCustomer(response.body());
                    Intent intent = new Intent(ctx, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ctx.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                ctx.Alert(t.getMessage());
            }
        });
    }


    @OnClick(R.id.b_facebook)
    public void onFacebookClick() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    String mAccessToken = null;
    long mTokenExpired = 0;

    private String requestAccessToken(GoogleSignInAccount googleAccount) {


        if (mAccessToken != null && SystemClock.elapsedRealtime() < mTokenExpired)
            return mAccessToken;

        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            final URL url = new URL("https://www.googleapis.com/oauth2/v4/token");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            final StringBuilder b = new StringBuilder();
            b.append("code=").append(googleAccount.getServerAuthCode()).append('&')
                    .append("client_id=").append(getString(R.string.server_client_id)).append('&')
                    .append("client_secret=").append(getString(R.string.server_client_secret)).append('&')
                    .append("redirect_uri=").append("").append('&')
                    .append("grant_type=").append("authorization_code");

            final byte[] postData = b.toString().getBytes("UTF-8");

            os = conn.getOutputStream();
            os.write(postData);

            final int responseCode = conn.getResponseCode();
            if (200 <= responseCode && responseCode <= 299) {
                is = conn.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
            } else {
                Log.d("Error:", conn.getResponseMessage());
                return null;
            }

            b.setLength(0);
            String output;
            while ((output = br.readLine()) != null) {
                b.append(output);
            }

            final JSONObject jsonResponse = new JSONObject(b.toString());
            mAccessToken = jsonResponse.getString("access_token");
            mTokenExpired = SystemClock.elapsedRealtime() + jsonResponse.getLong("expires_in") * 1000;
            return mAccessToken;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Thread thread = new Thread(() -> {
                    try {
                        String token = requestAccessToken(account);

                        String first_name = account.getGivenName();
                        String last_name = account.getFamilyName();
                        String email = account.getEmail();
                        Customer customer = new Customer();
                        customer.setPrenom(last_name);
                        customer.setName(first_name);
                        customer.setEmail(email);
                        customer.setAccess_token(token);
                        GoogleSignIn(customer);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                thread.start();


                // Signed in successfully, show authenticated UI.
                //updateUI(account);
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("optim_ERROR", "signInResult:failed code=" + e.getStatusCode());
                Alert("Error sign in with google");
                //updateUI(null);
            }
        } else {
            //If not request code is RC_SIGN_IN it must be facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}
