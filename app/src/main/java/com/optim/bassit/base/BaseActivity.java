package com.optim.bassit.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.optim.bassit.R;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.utils.LocaleHelper;
import com.optim.bassit.utils.OptimTools;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        updateBaseContextLocale(this);
        Locale loc = new Locale(LocaleHelper.getCurrentLanguage());
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(loc);

        super.onCreate(savedInstanceState);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    protected void attachBaseContext(Context base) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(new ContextWrapper(LocaleHelper.onAttach(base)));
        } else {
            super.attachBaseContext(updateBaseContextLocale(base));
        }
    }


    private Context updateBaseContextLocale(Context context) {
        String language = LocaleHelper.getCurrentLanguage();// SharedPrefUtils.getSavedLanguage(); // Helper method to get saved language from SharedPreferences
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResourcesLocale(context, locale);
        }

        return updateResourcesLocaleLegacy(context, locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }


    public void Alert(int message) {
        OptimTools.Alert(this, message);
    }

    public void Alert(String message) {
        OptimTools.Alert(this, message);
    }


    public void AlertWait(int message, OptimTools.AlertOkListener handler) {
        OptimTools.AlertWait(this, message, handler);
    }

    public void AlertWait(String message, OptimTools.AlertOkListener handler) {
        OptimTools.AlertWait(this, message, handler);
    }

    public void InputText(Activity activity, int title, int ok_button, int cancel_button, int type, OptimTools.InputPromptListener handler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title);
        final EditText input = new EditText(this);
        builder.setView(input);
        input.setInputType(type);
        builder.setPositiveButton(ok_button, (dialog, which) -> {
            String s = input.getText().toString();

            //OptimTools.hideKeyboard(this);
            KeyboardUtils.hideKeyboard(input);
            handler.onSuccess(s);
            dialog.dismiss();

        });
        builder.setOnCancelListener((a) -> {
            KeyboardUtils.hideKeyboard(input);
        });
        builder.setNegativeButton(cancel_button, (a, b) -> {

            KeyboardUtils.hideKeyboard(input);
            a.dismiss();
        });

        builder.show();
        input.requestFocus();
        KeyboardUtils.showKeyboard(activity);

//        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void InputText(Activity activity, String title, String ok_button, String cancel_button, int type, OptimTools.InputPromptListener handler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title);
        final EditText input = new EditText(this);
        builder.setView(input);
        input.setInputType(type);
        builder.setPositiveButton(ok_button, (dialog, which) -> {
            String s = input.getText().toString();

            //OptimTools.hideKeyboard(this);
            KeyboardUtils.hideKeyboard(input);
            handler.onSuccess(s);
            dialog.dismiss();

        });
        builder.setOnCancelListener((a) -> {
            KeyboardUtils.hideKeyboard(input);
        });
        builder.setNegativeButton(cancel_button, (a, b) -> {

            KeyboardUtils.hideKeyboard(input);
            a.dismiss();
        });

        builder.show();
        input.requestFocus();
        KeyboardUtils.showKeyboard(activity);

//        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void Alert(Context ctx, String message) {
        new AlertDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(R.string.text_ok, null).show();
    }


    public void AlertYesNo(int message, String ok_button, String cancel_button, OptimTools.AlertOkListener handler) {
        AlertYesNo(this.getResources().getString(message), ok_button, cancel_button, handler);
    }


    public void AlertYesNo(int message, OptimTools.AlertOkListener handler) {
        AlertYesNo(this.getResources().getString(message), getString(R.string.oui), getString(R.string.non), handler);
    }

    public void AlertConfirmCancel(int message, OptimTools.AlertOkListener handler) {
        AlertYesNo(this.getResources().getString(message), getString(R.string.text_confirm) , getString(R.string.text_cancel) , handler);
    }

    public void AlertYesNo(String message, OptimTools.AlertOkListener handler) {
        AlertYesNo(message, getString(R.string.oui), getString(R.string.non), handler);
    }

    public void AlertConfirmCancel(String message, OptimTools.AlertOkListener handler) {
        AlertYesNo(message, getString(R.string.text_confirm) , getString(R.string.text_cancel) , handler);

    }


    public void AlertYesNo(String message, String ok_button, String cancel_button, OptimTools.AlertOkListener handler) {
        OptimTools.AlertYesNo(this, message, ok_button, cancel_button, handler);
    }

    public void show() {
        View vw = findViewById(R.id.loading);
        OptimTools.animateView(vw, View.VISIBLE, 1, 200);
    }

    public void hide() {
        View vw = findViewById(R.id.loading);
        OptimTools.animateView(vw, View.GONE, 0, 200);
    }

    public boolean isempty(TextView vw) {
        try {

            if (vw.getText() == null || vw.getText().toString().matches(""))
                return true;
        } catch (Exception ex) {
            return true;
        }
        return false;
    }


    public void setStatusBarColor(int status_bg, boolean bdark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(status_bg));
            final int lFlags = window.getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(!bdark ? (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));


        }
    }

    public void fullyHandleResponse(Call<ApiResponse> callback, handleResponse handler) {
        callback.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Alert(response.errorBody().toString());
                    handler.onFail(response.errorBody().toString());
                } else if (response.body().isError()) {

                    Alert(response.body().getMessage());
                    handler.onFail(response.body().getMessage());
                } else {
                    handler.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(t.getMessage());
                handler.onFail(t.getMessage());
            }
        });
    }

    public void fullyHandleResponseSuccessOnly(Call<ApiResponse> callback, handleResponseWithSuccessOnly handler) {
        callback.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful()) {
                    Alert(response.errorBody().toString());

                } else if (response.body().isError()) {

                    Alert(response.body().getMessage());

                } else {
                    handler.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(t.getMessage());

            }
        });
    }

    public interface handleResponseWithSuccessOnly {

        void onSuccess();

    }

    public interface handleBooleanResponse {

        void onFinish(boolean b);

    }

    public interface handleResponse {

        void onSuccess();

        void onFail(String error);
    }

    public void selectPhoto(int maxcount) {
        FishBun.with(this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(maxcount)
                .setMinCount(1)
                .setPickerSpanCount(5)
                .setActionBarColor(this.getResources().getColor(R.color.colorPrimaryDark), this.getResources().getColor(R.color.colorPrimaryDark), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
//                .setArrayPaths(path)
                .setAlbumSpanCount(1, 1)
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(false)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back))
                .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check))
                .setAllDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check))
                .setAllViewTitle(this.getString(R.string.tous))
                .setMenuAllDoneText(this.getString(R.string.fini))
                .setActionBarTitle(this.getString(R.string.photos))
                .textOnNothingSelected(this.getString(R.string.veuillez_selectionner_au_moins_une_photo))
                .startAlbum();
    }
}


