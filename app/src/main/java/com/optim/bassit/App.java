package com.optim.bassit;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
/*
import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;*/

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.optim.bassit.di.AppComponent;
import com.optim.bassit.di.DaggerAppComponent;
import com.optim.bassit.di.modules.ContextModule;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.utils.LocaleHelper;

import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

import static com.optim.bassit.ui.activities.MainActivity.showBadge;


public class App extends Application {

    @Inject
    AppComponent component;

    public static String getCurrency() {
        return Currency.getInstance(Locale.getDefault()).getSymbol();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        LocaleHelper.onAttach(base);
        ctx = base;
    }

    public static void chng(String numchat, BottomNavigationView bottomNavigationView, int num){
        if(!numchat.equals("0"))
            showBadge(ctx,bottomNavigationView,num,numchat);
    }
    public static Context getAppContext() {
        return ctx;
    }

    static Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();


//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.optim.bassit",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }


        //FirebaseAnalytics.getInstance(this);

        initDependencies();
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
     /*   CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("jf.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
/*
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .disableGmsMissingPrompt(true)
                .init();*/

    }

    private void initDependencies() {
        component = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .networkModule(new NetworkModule())
                //    .firebaseModule(new FirebaseModule())
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }

    public static App getApp(Application application) {
        return (App) application;

    }


}
