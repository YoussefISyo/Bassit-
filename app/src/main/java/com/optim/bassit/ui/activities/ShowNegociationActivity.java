package com.optim.bassit.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.optim.bassit.App;
import com.optim.bassit.PagedScrollView;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.HomeadsFeed;
import com.optim.bassit.models.repreq;
import com.optim.bassit.ui.adapters.offerAdapter;
import com.optim.bassit.utils.OptimTools;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowNegociationActivity extends BaseActivity implements offerAdapter.ItemClickListener {

    offerAdapter adapter;
    private PagedScrollView paged;

    @Inject
    AppApi appApi;

    @BindView(R.id.mToolbarNegociations)
    Toolbar mToolbarNegociations;

    @BindView(R.id.recycler_negociation)
    RecyclerView recycler_negociation;

    @BindView(R.id.empty_container)
    LinearLayout empty_container;


    HomeadsFeed req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_negociation);

        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        setSupportActionBar(mToolbarNegociations);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);

        req = new HomeadsFeed();

        req.setId(Integer.parseInt(getIntent().getStringExtra("offer_id")));
        req.setAdrs(getIntent().getStringExtra("city"));
        req.setDate_start(getIntent().getStringExtra("date_start"));
        req.setDurationreq(getIntent().getStringExtra("date_end"));
        req.setUser_id(getIntent().getStringExtra("user"));
        req.setQuantity(getIntent().getIntExtra("quantity", 0));
        req.setUnity(getIntent().getStringExtra("unity"));
        req.setUrgence(getIntent().getIntExtra("urgence", 0));
        req.setPrix(getIntent().getStringExtra("price"));
        req.setDes(getIntent().getStringExtra("description"));



        recycler_negociation.setLayoutManager(new LinearLayoutManager(ShowNegociationActivity.this));

        adapter = new offerAdapter(this);
        recycler_negociation.setAdapter(adapter);

//        paged = OptimTools.injectPaginationHavingLayoutManager(scroll, recyclerView.getLayoutManager(), (p, t) -> {
//            getFeeds(p,srch);
//        });



        init();
    }

    private void init() {

        show();

        appApi.getoffer(1,String.valueOf(req.getId())).enqueue(new Callback<List<repreq>>() {
            @Override
            public void onResponse(Call<List<repreq>> call, Response<List<repreq>> response) {

                //   Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();
                if (response.isSuccessful()) {
                    //  Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();
                    adapter.fill(1, response.body(),true);
                   if (response.body().size() == 0){
                       empty_container.setVisibility(View.VISIBLE);
                   }
                }
                //swipeView.setRefreshing(false);
                hide();
            }

            @Override
            public void onFailure(Call<List<repreq>> call, Throwable t) {
                //swipeView.setRefreshing(false);
                hide();
            }
        });
    }

    @Override
    public void onMessageClick(repreq HomeadsFeed) {

    }

    @Override
    public void onModifiClick(repreq HomeadsFeed) {

    }

    @Override
    public void onremoveClick(repreq HomeadsFeed) {

    }

    @Override
    public void onAcceptClick(repreq HomeadsFeed) {
        show();
        appApi.addDemandService(Integer.parseInt(req.getUser_id()), Integer.parseInt(HomeadsFeed.getService_id()), req.getDate_start(), req.getDurationreq(),
                HomeadsFeed.getDesignation(), req.getAdrs(), req.getUrgence(), null, req.getUnity(),
                req.getQuantity(), HomeadsFeed.getPrix(), req.getDes(), Integer.parseInt(HomeadsFeed.getUser_id()),
                2, req.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Toast.makeText(ShowNegociationActivity.this, "La demande est accepté", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShowNegociationActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("dsd", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onDeclineClick(repreq HomeadsFeed) {
        show();
        appApi.declineNegociation(HomeadsFeed.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    if (!response.body().isError()){
                        Toast.makeText(ShowNegociationActivity.this, getString(R.string.negociation_decline), Toast.LENGTH_SHORT).show();
                        init();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}