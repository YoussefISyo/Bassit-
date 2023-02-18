package com.optim.bassit.di.modules;


import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    static final String IP_ADDRESS = "https://bassit-app.com";
    static final String API_URL = IP_ADDRESS + "/public/api/";
    public static final String PIN_URL = API_URL + "pin/";
    public static final String CHAT_URL = API_URL + "chat/image/";
    public static final String COMMISSION_URL = API_URL + "commissions/";
    public static final String SERVICE_IMAGE_URL = API_URL + "service/photo/";
    public static final String ALBUM_IMAGE_URL = API_URL + "service/albumphoto/";
    public static final String ICONS_URL = IP_ADDRESS + "/images/categories/";
    public static final String ICONS_SOUS_URL = IP_ADDRESS + "/images/sous/";
    public static final String ADS_URL = API_URL+ "slider/image/";

    @Singleton
    @Provides
    public HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Singleton
    @Provides
    public OkHttpClient getHttpClient(HttpLoggingInterceptor interceptor) {


        OkHttpClient.Builder bld = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(interceptor);

        bld = bld.addInterceptor(chain -> {
            Request original = chain.request();

            String token = CurrentUser.getInstance().getApitoken();

            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

        return bld.build();
    }

    @Singleton
    @Provides
    public Retrofit getRetrofit(OkHttpClient client) {

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    public AppApi getApi(Retrofit retrofit) {
        return retrofit.create(AppApi.class);
    }

}

