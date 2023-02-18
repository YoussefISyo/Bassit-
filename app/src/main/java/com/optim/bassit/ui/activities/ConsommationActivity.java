package com.optim.bassit.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.Journal;
import com.optim.bassit.ui.adapters.ConsommationAdapter;
import com.optim.bassit.ui.dialogs.BuyPointsDialogFragment;
import com.optim.bassit.ui.dialogs.ChooseDialogFragment;
import com.optim.bassit.utils.OptimTools;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsommationActivity extends BaseActivity implements ConsommationAdapter.ItemClickListener, BuyPointsDialogFragment.RefreshListener, ChooseDialogFragment.doRefreshListener {

    ConsommationAdapter adapter;

    @BindView(R.id.b_transfer_back)
    ImageButton Back;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @Inject
    AppApi appApi;
    int consommation_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consommation_credit);
        ButterKnife.bind(this);
        App.getApp(this.getApplication()).getComponent().inject(this);
        TextView t_total = findViewById(R.id.t_total);
        TextView t_title = findViewById(R.id.t_title);

        CardView card = findViewById(R.id.bottom_card);


        consommation_type = getIntent().getIntExtra("consommation_type", 0);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConsommationAdapter(this, consommation_type);
        recyclerView.setAdapter(adapter);

        Back.setOnClickListener(v -> finish());


        if (consommation_type == 0) {
            t_title.setText(R.string.text_details_points);
            t_total.setText(CurrentUser.getInstance().getmCustomer().getPoints() + " " + getString(R.string.text_pts));
        } else if (consommation_type == 1) {
            t_title.setText(R.string.text_detail_paiement);
            t_total.setText(new DecimalFormat("#").format(CurrentUser.getInstance().getmCustomer().getCredit()) + " " + getString(R.string.text_da));
        } else if (consommation_type == 2) {
            t_title.setText(R.string.text_details_navigui);
            t_total.setText(new DecimalFormat("#").format(CurrentUser.getInstance().getmCustomer().getGagner()) + " " + getString(R.string.text_da));
        } else if (consommation_type == 101) {
            t_title.setText(R.string.text_bonus_navigui);
            t_total.setText(new DecimalFormat("#").format(CurrentUser.getInstance().getmCustomer().getBonus()) + " " + getString(R.string.text_da));

            setStatusBarColor(R.color.black, false);
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(getResources().getColor(R.color.black));
            Back.setColorFilter(getResources().getColor(R.color.yellow));
            t_title.setTextColor(getResources().getColor(R.color.yellow));
            card.setBackgroundColor(getResources().getColor(R.color.black));
            t_total.setTextColor(getResources().getColor(R.color.yellow));

            findViewById(R.id.b_add_montant).setVisibility(View.GONE);
            findViewById(R.id.b_withdraw).setVisibility(View.VISIBLE);

            TextView ref_id = findViewById(R.id.ref_id);
            ref_id.setVisibility(View.VISIBLE);
            ref_id.setText(getString(R.string.text_ref_client) + String.format("%04d", CurrentUser.getInstance().getmCustomer().getId() + 3986));
        }

        getConsommation();

        swipe.setOnRefreshListener(() -> {
            if (consommation_type == 101)
                doRefresh();
            else
                getConsommation();
        });
    }

    @OnClick(R.id.b_withdraw)
    public void withdrawAction() {
        if (consommation_type == 101) {
            FragmentManager fm = getSupportFragmentManager();
            ChooseDialogFragment chooseDialogFragment = ChooseDialogFragment.newInstance(consommation_type, this,ConsommationActivity.this);
            chooseDialogFragment.show(fm, "fragment_edit_name");
        }
    }

    private void getConsommation() {
        swipe.setRefreshing(true);
        String link = "points";
        if (consommation_type == 1)
            link = "credit";
        else if (consommation_type == 2)
            link = "gagner";
        else if (consommation_type == 101)
            link = "cgagner";
        appApi.getCreditConsommation("reward/consommation/" + link).enqueue(new Callback<List<Journal>>() {
            @Override
            public void onResponse(Call<List<Journal>> call, Response<List<Journal>> response) {
                if (response.isSuccessful())
                    adapter.fill(response.body());

                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Journal>> call, Throwable t) {
                swipe.setRefreshing(false);
                Alert(t.getMessage());
            }
        });
    }


    @Override
    public void onItemClick(Journal journal) {

    }

    @Override
    public boolean onLongItemClick(Journal journal) {
        return false;
    }

    @OnClick(R.id.b_add_montant)
    public void doAddMontant() {
        if (consommation_type == 0) {
            FragmentManager fm = getSupportFragmentManager();
            BuyPointsDialogFragment buypointsfragment = BuyPointsDialogFragment.newInstance(this);
            buypointsfragment.show(fm, "fragment_edit_name");
        } else if (consommation_type == 1) {
            Intent intent = new Intent(this, AddMontantActivity.class);
            startActivityForResult(intent, 10001);
        } else if (consommation_type == 2) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.text_versement_montant);

            final EditText input = new EditText(this);

            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton(getString(R.string.valider), (dialog, which) -> {
                String s = input.getText().toString();
                if (s.matches("") && Integer.valueOf(s) < 100) {
                    Alert(R.string.message_less_100);
                }

                OptimTools.hideKeyboard(this);
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.message_etes_vous_sur_de_vouloir_verser) + " " + s + " " + getString(R.string.da_vers_partager_et_gnagner))
                        .setPositiveButton(R.string.text_ok, (d, z) -> {
                            d.dismiss();

                            appApi.addGagner(Integer.valueOf(s)).enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful() && !response.body().isError())
                                        doRefresh();
                                    else if (response.isSuccessful())
                                        Alert(response.body().getMessage());
                                    else
                                        Alert(response.errorBody().toString());
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    Alert(t.getMessage());
                                }
                            });
                        }).setNegativeButton(R.string.text_cancel, null).show();
                dialog.dismiss();

            });
            builder.show();

            input.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (consommation_type == 101 && resultCode == 10066) {
            doRefresh();
        } else if (consommation_type != 101)
            getConsommation();
    }

    @Override
    public void doRefresh() {
        appApi.customerProfile(0).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer customer = response.body();
                    CurrentUser.getInstance().setmCustomer(customer);
                    TextView t_total = findViewById(R.id.t_total);
                    if (consommation_type == 0) {
                        t_total.setText(CurrentUser.getInstance().getmCustomer().getPoints() + " " + getString(R.string.text_pts));
                    } else if (consommation_type == 1) {
                        t_total.setText(new DecimalFormat("#").format(CurrentUser.getInstance().getmCustomer().getCredit()) + " " + getString(R.string.text_da));
                    } else if (consommation_type == 2) {
                        t_total.setText(new DecimalFormat("#").format(CurrentUser.getInstance().getmCustomer().getGagner()) + " " + getString(R.string.text_da));
                    } else if (consommation_type == 101) {
                        t_total.setText(new DecimalFormat("#").format(CurrentUser.getInstance().getmCustomer().getBonus()) + " " + getString(R.string.text_da));
                    }
                    getConsommation();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                swipe.setRefreshing(false);
            }
        });

    }


}
