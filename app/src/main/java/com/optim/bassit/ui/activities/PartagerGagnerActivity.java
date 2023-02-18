package com.optim.bassit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.HomeFeed;

import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartagerGagnerActivity extends BaseActivity {


    @Inject
    AppApi appApi;
    @BindView(R.id.t_text)
    TextView text;

    HomeFeed mHomeFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partager_gagner);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        mHomeFeed = getIntent().getParcelableExtra("service");
        text.setText(text.getText().toString().replace("{0}", new DecimalFormat("#").format(Math.floor(mHomeFeed.getPure()))));
    }


    @OnClick({R.id.b_confirmer})
    public void onConfirmerClick() {
        appApi.generateLink(mHomeFeed.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!response.isSuccessful())
                    Alert(response.errorBody().toString());
                else if (response.body().isError())
                    Alert(response.body().getMessage());
                else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.partager_et_gagner);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, response.body().getMessage());
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.partager_sur)));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(t.getMessage());
            }
        });


    }


}
