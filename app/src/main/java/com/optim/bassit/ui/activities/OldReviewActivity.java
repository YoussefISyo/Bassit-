package com.optim.bassit.ui.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.Avis;
import com.optim.bassit.ui.adapters.OldReviewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OldReviewActivity extends BaseActivity implements OldReviewAdapter.ItemClickListener {

    OldReviewAdapter adapter;


    @Inject
    AppApi appApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_review);
        ButterKnife.bind(this);
        App.getApp(this.getApplication()).getComponent().inject(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OldReviewAdapter(this);
        recyclerView.setAdapter(adapter);

        getSimilars();
    }

    @OnClick(R.id.b_back)
    public void onClickBack() {
        finish();
    }

    private void getSimilars() {
        appApi.getOldReview().enqueue(new Callback<List<Avis>>() {
            @Override
            public void onResponse(Call<List<Avis>> call, Response<List<Avis>> response) {
                if (response.isSuccessful()) {
                    adapter.fill(1, response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Avis>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onItemClick(Avis avis) {

        AlertYesNo(getString(R.string.sur_to_delete_old_review) + " " + avis.getFullName() + " - " + avis.getClient_comment(), getString(R.string.text_delete)
                , getString(R.string.text_cancel), () -> {
                    fullyHandleResponseSuccessOnly(appApi.deleteOldReview(avis.getId()), () -> {
                        AlertWait(R.string.avis_supprime_avec_success, OldReviewActivity.this::finish);
                    });
                });
    }
}