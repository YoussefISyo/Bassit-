package com.optim.bassit.ui.activities;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.ui.adapters.RechercheResultAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransfererActivity extends BaseActivity implements RechercheResultAdapter.ItemClickListener {

    RechercheResultAdapter adapter;


    @Inject
    AppApi appApi;

    private int bon_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferer);
        ButterKnife.bind(this);
        App.getApp(this.getApplication()).getComponent().inject(this);

        bon_id = getIntent().getExtras().getInt("bon_id", 0);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RechercheResultAdapter(this);
        recyclerView.setAdapter(adapter);

        getSimilars();
    }

    @OnClick(R.id.b_back)
    public void onClickBack() {
        finish();
    }

    private void getSimilars() {
        appApi.getSame(bon_id).enqueue(new Callback<List<HomeFeed>>() {
            @Override
            public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
                if (response.isSuccessful()) {
                    adapter.fill(1, response.body());
                }
            }

            @Override
            public void onFailure(Call<List<HomeFeed>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onItemClick(HomeFeed resul) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage("Etes vous sur de vouloir transférer ce service vers " + resul.getFullName())
                .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                    appApi.doTransfert(bon_id, resul.getId()).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                if (!response.body().isError()) {
                                    setResult(10033);
                                    finish();
                                } else {
                                    Alert(response.body().getMessage());
                                }
                            } else
                                Alert(response.errorBody().toString());
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Alert(t.getMessage());
                        }
                    });
                })
                .setNegativeButton(R.string.text_cancel, null).show();

    }

    @Override
    public void onAdsClick(HomeFeed homefeed) {
        // nothing to do
    }
}
