package com.optim.bassit.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Commission;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.ui.adapters.CommissionAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommissionActivity extends BaseActivity {

    @Inject
    AppApi appApi;
    @BindView(R.id.recycler_commission)
    RecyclerView recycler_commission;
    
    CommissionAdapter commissionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);

        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        recycler_commission.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        
        commissionAdapter = new CommissionAdapter(this, appApi);
        recycler_commission.setAdapter(commissionAdapter);
        
        getCommissions();
    }

    private void getCommissions() {
        show();
        appApi.getCommissions(CurrentUser.getInstance().getmCustomer().getId()).enqueue(new Callback<List<Commission>>() {
            @Override
            public void onResponse(Call<List<Commission>> call, Response<List<Commission>> response) {
                if (response.isSuccessful()){
                    commissionAdapter.fill(response.body());
                }
                hide();
            }

            @Override
            public void onFailure(Call<List<Commission>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        commissionAdapter.onActivityResult(requestCode, resultCode, data);
    }
}