package com.optim.bassit.ui.activities;

import android.os.Bundle;
import android.widget.GridView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.ui.adapters.InterestAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterestActivity extends BaseActivity implements InterestAdapter.InterestClick {

    InterestAdapter adapter;

    @Inject
    AppApi appApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        ButterKnife.bind(this);
        App.getApp(this.getApplication()).getComponent().inject(this);


        GridView grid_view = findViewById(R.id.grid_view);
        adapter = new InterestAdapter(this, this);

        grid_view.setAdapter(adapter);

        getInterest();
    }

    private void getInterest() {

        appApi.getInterest().enqueue(new Callback<List<Categorie>>() {
            @Override
            public void onResponse(Call<List<Categorie>> call, Response<List<Categorie>> response) {
                if (response.isSuccessful()) {
                    adapter.fill(response.body());
                } else {
                    Alert("Erreur de chargement de catégories");
                }
            }

            @Override
            public void onFailure(Call<List<Categorie>> call, Throwable t) {
                Alert("Erreur de chargement de catégories");
            }
        });
    }

    @OnClick(R.id.b_back)
    public void onBack() {
        finish();
    }

    @Override
    public void doChange(int cat_id, int has, handleBooleanResponse hand) {
        fullyHandleResponseSuccessOnly(appApi.saveInterests(cat_id, has), () -> {
            hand.onFinish(true);
        });
    }
}
