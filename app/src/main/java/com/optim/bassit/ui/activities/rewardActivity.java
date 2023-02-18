package com.optim.bassit.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.Reward;
import com.optim.bassit.ui.adapters.RewardAdapter;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.ui.fragments.CompteFragment;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class rewardActivity extends BaseActivity implements RewardAdapter.ItemClickListener{
    @Inject
    AppApi appApi;
    private TextView t_points;
    private TextView t_credit;
    private TextView t_gcount;
    private TextView t_gagner;
    SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_compte);
        App.getApp(getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        swipe = findViewById(R.id.swipe);
        t_points = findViewById(R.id.t_points);
        t_credit = findViewById(R.id.t_credit);
        t_gcount = findViewById(R.id.t_gcount);
        t_gagner = findViewById(R.id.t_gagner);

        refreshApi();


        swipe.setOnRefreshListener(() -> refreshApi());
      /*  CompteFragment compte_fragment = new CompteFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, compte_fragment);
        transaction.addToBackStack(null);
        transaction.commit();*/
    }
    RewardAdapter adapter;

    private void fillData() {


        t_points.setText(CurrentUser.getInstance().getmCustomer().getFullPoints(this));
        t_gagner.setText(CurrentUser.getInstance().getmCustomer().getGagner() + " " + getString(R.string.text_da));
        t_credit.setText(new DecimalFormat("#").format(CurrentUser.getInstance().getmCustomer().getCredit()) + " " + getString(R.string.text_da));
        int gcount = CurrentUser.getInstance().getmCustomer().getGcount();

        if (gcount == 1)
            t_gcount.setText(gcount + " " + getString(R.string.text_service));
        else
            t_gcount.setText(gcount + " " + getString(R.string.text_services));

        swipe.setRefreshing(true);
        RecyclerView lst = findViewById(R.id.lst);
        lst.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new RewardAdapter(this);
        lst.setAdapter(adapter);

        appApi.getRewards().enqueue(new Callback<List<Reward>>() {
            @Override
            public void onResponse(Call<List<Reward>> call, Response<List<Reward>> response) {
                if (response.isSuccessful()) {
                    adapter.fillPoints(response.body());
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Reward>> call, Throwable t) {
                swipe.setRefreshing(false);
            }
        });
    }

    private void refreshApi() {
        swipe.setRefreshing(true);
        appApi.customerProfile(0).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer customer = response.body();
                    CurrentUser.getInstance().setmCustomer(customer);
                    customer.setToken(CurrentUser.getInstance().getApitoken());
                    fillData();
                }
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                swipe.setRefreshing(false);
            }
        });
    }
    @Override
    public void onItemClick(Reward reward) {

    }

    @Override
    public void doBuy(Reward reward) {

        appApi.doBuy(reward.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful())
                    if (response.body().isError())
                        Alert(response.body().getMessage());
                    else
                        refreshApi();
                else
                    Alert(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(t.getMessage());
            }
        });
    }

    @Override
    public void doPlan(Reward reward) {

        if (reward.getValeur() == 1000) {
            Intent intent = new Intent(rewardActivity.this, OldReviewActivity.class);
            startActivityForResult(intent, 10001);
            return;
        }


        appApi.doPlan(reward.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful())
                    if (response.body().isError())
                        Alert(response.body().getMessage());
                    else {

                        if (reward.getValeur() >= 1000) {
                            Alert(R.string.demande_success_we_will_contact_you);
                            refreshApi();
                            return;
                        }

                        new AlertDialog.Builder(rewardActivity.this)
                                .setTitle(R.string.app_name)
                                .setMessage(getString(R.string.text_plan) + " " + reward.getDesignation() + " " + getString(R.string.text_activeee))
                                .setPositiveButton(R.string.text_ok, (a, b) -> {
                                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                   finish();
                                }).show();
                    }
                else
                    Alert(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Alert(t.getMessage());
            }
        });
    }


    @OnClick(R.id.b_info)
    public void doInfo() {
        Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bassit-app.com/info"));
        startActivity(browserIntent3);
    }

    @OnClick(R.id.b_cons_montant)
    public void doConsMontant() {
        Intent intent = new Intent(this, ConsommationActivity.class);
        intent.putExtra("consommation_type", 1);
        startActivityForResult(intent, 10001);
    }

    @OnClick(R.id.b_gagner)
    public void doShowGagner() {
        Intent intent = new Intent(this, ConsommationActivity.class);
        intent.putExtra("consommation_type", 2);
        startActivityForResult(intent, 10001);
    }

    @OnClick(R.id.b_cons_points)
    public void doConsPoints() {
        Intent intent = new Intent(this, ConsommationActivity.class);
        intent.putExtra("consommation_type", 0);
        startActivityForResult(intent, 10001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001)
            refreshApi();
    }
    @Override
    public void onBackPressed() {
     finish();
    }
}
