package com.optim.bassit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.optim.bassit.App;
import com.optim.bassit.BuildConfig;
import com.optim.bassit.MyFirebaseNotification;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.Word;
import com.optim.bassit.utils.LocaleHelper;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    @Inject
    AppApi appApi;
    private String thelink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        thelink = appLinkIntent.getDataString();

        App.getApp(getApplication()).getComponent().inject(this);

        appApi.getVersion().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                if (!response.isSuccessful()) {
                    new android.app.AlertDialog.Builder(SplashActivity.this)
                            .setTitle(R.string.app_name)
                            .setMessage(getString(R.string.noconnection))
                            .setPositiveButton(R.string.text_ok, (a, b) -> {
                                finish();
                                a.dismiss();
                            }).show();
                    return;
                }

                int version = Integer.parseInt(response.body());

                int my_version = BuildConfig.VERSION_CODE;

                if (my_version < version) {
                    new android.app.AlertDialog.Builder(SplashActivity.this)
                            .setTitle(R.string.app_name)
                            .setMessage("Vous devez mettre bassit à jour")
                            .setPositiveButton(R.string.text_ok, (a, b) -> {
                                a.dismiss();
                                final String appPackageName = SplashActivity.this.getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }).show();

                    return;
                }

                setContentView(R.layout.activity_splash);

                ImageView logo = findViewById(R.id.logo);
                Animation pulseAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.pulse);
                logo.startAnimation(pulseAnim);
                String token = getSharedPreferences("_", MODE_PRIVATE).getString("apitoken", "");
                Log.d("lrvTokenID", token);
                if (!token.matches("")) {
                    CurrentUser.getInstance().setApitoken(token);
                } else
                    CurrentUser.getInstance().setApitoken("");

                //  Log.d("OPTIM_LOG", CurrentUser.getInstance().getApitoken());

                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashActivity.this, instanceIdResult -> {
                    String newToken = instanceIdResult.getToken();
                    Log.d("TokenID", newToken);
                    getSharedPreferences("_", MODE_PRIVATE).edit().putString("fbtoken", newToken).apply();
                });


                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.WHITE);
                final int lFlags = window.getDecorView().getSystemUiVisibility();
                window.getDecorView().setSystemUiVisibility(lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                if (token != null && !token.matches(""))
                    CheckLogin();
                else
                    redoLogin();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                new android.app.AlertDialog.Builder(SplashActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Veuillez vérifier votre connexion internet")
                        .setPositiveButton(R.string.text_ok, (a, b) -> {
                            finish();
                            a.dismiss();
                        }).show();
                return;
            }
        });

    }

    private void CheckLogin() {
        appApi.customerProfile(0).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer customer = response.body();
                    CurrentUser.getInstance().setmCustomer(customer);
                    customer.setToken(CurrentUser.getInstance().getApitoken());
                    doFCM(appApi, SplashActivity.this);


                    appApi.getDict().enqueue(new Callback<List<Word>>() {
                        @Override
                        public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                            if (response.isSuccessful())
                                AppData.getInstance().setDict(response.body(), LocaleHelper.getPersistedData(SplashActivity.this, Locale.getDefault().getLanguage()));

                            startMainActivity();
                        }

                        @Override
                        public void onFailure(Call<List<Word>> call, Throwable t) {
                            startMainActivity();
                        }
                    });


                } else {
                    doUnFCM(appApi, SplashActivity.this);
                    redoLogin();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                doUnFCM(appApi, SplashActivity.this);
                redoLogin();
            }
        });
    }

    private void redoLogin() {
        Intent intent = new Intent(SplashActivity.this, OnboardActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        if (thelink != null && !thelink.matches(""))
            intent.putExtra("link", thelink);
        if (getIntent().hasExtra("notification")) {
            intent.putExtra("notification", getIntent().getStringExtra("notification"));

        }
        if (getIntent().hasExtra("service")) {
            intent.putExtra("service", getIntent().getStringExtra("service"));

        }
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    public static void doUnFCM(AppApi appApi, Context ctx) {
        appApi.unfcm(MyFirebaseNotification.getToken(ctx)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public static void doFCM(AppApi appApi, Context ctx) {
        appApi.fcm(MyFirebaseNotification.getToken(ctx)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}

