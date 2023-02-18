package com.optim.bassit.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.ui.adapters.RechercheResultAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity implements RechercheResultAdapter.ItemClickListener {

    ArrayList<HomeFeed> list_result;
    RechercheResultAdapter adapterresult;

    @Inject
    AppApi appApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        list_result = getIntent().getParcelableArrayListExtra("list_search");

        RecyclerView recyclerViewVr = findViewById(R.id.lst_result);
        recyclerViewVr.setLayoutManager(new LinearLayoutManager(this));
        adapterresult = new RechercheResultAdapter(this);
        recyclerViewVr.setAdapter(adapterresult);

        adapterresult.fill(1, list_result);
    }

    @Override
    public void onItemClick(HomeFeed homefeed) {
        Intent myintent = new Intent(this, ServiceActivity.class);
        boolean isme = false;
        if (CurrentUser.getInstance().isLogin() && CurrentUser.getInstance().getmCustomer().getId() == homefeed.getUser_id())
            isme = true;

        myintent.putExtra("isme", isme);
        myintent.putExtra("parcel_service", homefeed);
        startActivity(myintent);
    }

    @Override
    public void onAdsClick(HomeFeed homefeed) {
        try {
            switch (homefeed.getIsg()) {
                case 0:


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homefeed.getTitle()));
                    startActivity(browserIntent);
                    break;
                case 1:
                    Integer u_id = Integer.valueOf(homefeed.getTitle());
                    Intent myintent = new Intent(this, ProfileActivity.class);
                    myintent.putExtra("user_id", u_id);
                    startActivity(myintent);
                    break;
                case 2:
                    Integer s_id = Integer.valueOf(homefeed.getTitle());
                    appApi.getOneService(s_id).enqueue(new Callback<HomeFeed>() {
                        @Override
                        public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
                            if (response.isSuccessful()) {
                                Intent myintent = new Intent(SearchResultActivity.this, ServiceActivity.class);
                                myintent.putExtra("isme", false);
                                myintent.putExtra("parcel_service", response.body());
                                startActivity(myintent);
                            }
                        }

                        @Override
                        public void onFailure(Call<HomeFeed> call, Throwable t) {

                        }
                    });

                    break;
            }
        } catch (Exception ex) {

        }
    }
}