package com.optim.bassit.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;

import com.optim.bassit.App;

import java.util.Locale;


public class LocaleHelper {
    private static final String SELECTED_LANGUAGE = "language";

    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());

        return setLocale(context, lang);
    }


    public static Context setLocale(Context context, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        persist(context, language);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        }

        return updateResourcesLegacy(context, locale);
    }

    public static String getCurrentLanguage() {
        return getPersistedData(App.getAppContext(), Locale.getDefault().getLanguage());
    }

    public static boolean isRTL() {
        String lang = getCurrentLanguage();

        return lang.equals("ar");
    }

    public static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language).apply();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        return context.createConfigurationContext(configuration);
    }


    private static Context updateResourcesLegacy(Context context, Locale locale) {

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);

        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }

        return new ContextWrapper(context);
    }
}
