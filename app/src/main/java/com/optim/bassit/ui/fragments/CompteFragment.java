package com.optim.bassit.ui.fragments;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.optim.bassit.App;
import com.optim.bassit.BuildConfig;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseFragment;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.Reward;
import com.optim.bassit.ui.activities.ConsommationActivity;
import com.optim.bassit.ui.activities.MainActivity;
import com.optim.bassit.ui.activities.OldReviewActivity;
import com.optim.bassit.ui.adapters.RewardAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteFragment extends BaseFragment implements RewardAdapter.ItemClickListener {

    @Inject
    AppApi appApi;
    private TextView t_points;
    private TextView t_credit;
    private TextView t_gcount;
    private TextView t_gagner;
    private TextView txt_user_points;
    private TextView txt_user_points_silver;
    private TextView txt_user_points_gold;
    private TextView txt_user_points_diamond;
    private TextView txt_reward_points;
    private TextView txt_reward_points_silver;
    private TextView txt_reward_points_gold;
    private TextView txt_reward_points_diamond;
    private TextView txt_designation_reward;
    private TextView txt_designation_reward_silver;
    private TextView txt_designation_reward_gold;
    private TextView txt_designation_reward_diamond;


    private Button btn_active_bronze;
    private Button txt_encours_bronze;

    private Button btn_active_silver;
    private Button txt_encours_silver;

    private Button btn_active_gold;
    private Button txt_encours_gold;

    private Button btn_active_diamond;
    private Button txt_encours_diamond;


    private Button btn_rateus;
    private Button btn_shareapp;
    private Button btn_likefb;
    private Button btn_likeinsta;



    List<Reward> initial_rewards, other_rewards;

    public CompteFragment() {
    }

    public static CompteFragment newInstance() {
        CompteFragment fragment = new CompteFragment();
        return fragment;
    }

    SwipeRefreshLayout swipe;
    View vw;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vw = inflater.inflate(R.layout.fragment_compte, container, false);
        ButterKnife.bind(this, vw);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);

        swipe = vw.findViewById(R.id.swipe);
        t_points = vw.findViewById(R.id.t_points);
        t_credit = vw.findViewById(R.id.t_credit);
        t_gcount = vw.findViewById(R.id.t_gcount);
        t_gagner = vw.findViewById(R.id.t_gagner);
        txt_user_points = vw.findViewById(R.id.txt_user_points);
        txt_user_points_silver = vw.findViewById(R.id.txt_user_points_silver);
        txt_user_points_gold = vw.findViewById(R.id.txt_user_points_gold);
        txt_user_points_diamond = vw.findViewById(R.id.txt_user_points_diamond);
        btn_rateus = vw.findViewById(R.id.btn_rateus);
        btn_shareapp = vw.findViewById(R.id.btn_shareapp);
        btn_likefb = vw.findViewById(R.id.btn_likefb);
        btn_likeinsta = vw.findViewById(R.id.btn_likeinsta);

        initial_rewards = new ArrayList<>();
        other_rewards = new ArrayList<>();

        refreshApi();


        swipe.setOnRefreshListener(() -> refreshApi());
        return vw;
    }

    RewardAdapter adapter;

    private void fillData(View vw) {


        t_points.setText(CurrentUser.getInstance().getmCustomer().getFullPoints(this.getActivity()));
        txt_user_points.setText(String.valueOf(CurrentUser.getInstance().getmCustomer().getPoints()));
        txt_user_points_silver.setText(String.valueOf(CurrentUser.getInstance().getmCustomer().getPoints()));
        txt_user_points_gold.setText(String.valueOf(CurrentUser.getInstance().getmCustomer().getPoints()));
        txt_user_points_diamond.setText(String.valueOf(CurrentUser.getInstance().getmCustomer().getPoints()));
        t_gagner.setText(CurrentUser.getInstance().getmCustomer().getGagner() + " " + getString(R.string.text_da));
        t_credit.setText(new DecimalFormat("#").format(CurrentUser.getInstance().getmCustomer().getCredit()) + " " + getString(R.string.text_da));
        int gcount = CurrentUser.getInstance().getmCustomer().getGcount();
        
        if (CurrentUser.getInstance().getmCustomer().getRate_us() == 0 && CurrentUser.getInstance().getmCustomer().getPoints() >= 5){
            btn_rateus.setEnabled(true);
            btn_rateus.setBackground(getContext().getDrawable(R.drawable.bg_card_points));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_rateus.setTextColor(getContext().getColor(R.color.white));
            }
        }else{
            btn_rateus.setEnabled(false);
            btn_rateus.setBackground(getContext().getDrawable(R.drawable.bg_btn_reward_gray));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_rateus.setTextColor(getContext().getColor(R.color.black));
            }
        }

        if (CurrentUser.getInstance().getmCustomer().getShare_app() == 0 && CurrentUser.getInstance().getmCustomer().getPoints() >= 5){
            btn_shareapp.setEnabled(true);
            btn_shareapp.setBackground(getContext().getDrawable(R.drawable.bg_card_points));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_shareapp.setTextColor(getContext().getColor(R.color.white));
            }
        }else{
            btn_shareapp.setEnabled(false);
            btn_shareapp.setBackground(getContext().getDrawable(R.drawable.bg_btn_reward_gray));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_shareapp.setTextColor(getContext().getColor(R.color.black));
            }
        }

        if (CurrentUser.getInstance().getmCustomer().getLike_fb() == 0 && CurrentUser.getInstance().getmCustomer().getPoints() >= 5){
            btn_likefb.setEnabled(true);
            btn_likefb.setBackground(getContext().getDrawable(R.drawable.bg_card_points));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_likefb.setTextColor(getContext().getColor(R.color.white));
            }
        }else{
            btn_likefb.setEnabled(false);
            btn_likefb.setBackground(getContext().getDrawable(R.drawable.bg_btn_reward_gray));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_likefb.setTextColor(getContext().getColor(R.color.black));
            }
        }

        if (CurrentUser.getInstance().getmCustomer().getLike_insta() == 0 && CurrentUser.getInstance().getmCustomer().getPoints() >= 5){
            btn_likeinsta.setEnabled(true);
            btn_likeinsta.setBackground(getContext().getDrawable(R.drawable.bg_card_points));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_likeinsta.setTextColor(getContext().getColor(R.color.white));
            }
        }else{
            btn_likeinsta.setEnabled(false);
            btn_likeinsta.setBackground(getContext().getDrawable(R.drawable.bg_btn_reward_gray));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_likeinsta.setTextColor(getContext().getColor(R.color.black));
            }
        }

        if (gcount == 1)
            t_gcount.setText(gcount + " " + getString(R.string.text_service));
        else
            t_gcount.setText(gcount + " " + getString(R.string.text_services));

        swipe.setRefreshing(true);
        RecyclerView lst = vw.findViewById(R.id.lst);
        lst.setLayoutManager(new LinearLayoutManager(vw.getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new RewardAdapter(this);
        lst.setAdapter(adapter);

        appApi.getRewards().enqueue(new Callback<List<Reward>>() {
            @Override
            public void onResponse(Call<List<Reward>> call, Response<List<Reward>> response) {
                if (response.isSuccessful()) {
                    other_rewards.clear();
                    initial_rewards.clear();
                    for(Reward reward: response.body()){
                        if (reward.getValeur() == 101 || reward.getValeur() == 102 || reward.getValeur() == 103 || reward.getValeur() == 104){
                            initial_rewards.add(reward);
                        }else{
                            other_rewards.add(reward);
                        }
                    }
                    fillInitialRewards();
                    adapter.fillPoints(other_rewards);
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Reward>> call, Throwable t) {
                swipe.setRefreshing(false);
            }
        });
    }


    private void fillInitialRewards() {
        txt_reward_points = vw.findViewById(R.id.txt_reward_points);
        txt_reward_points_silver = vw.findViewById(R.id.txt_reward_points_silver);
        txt_reward_points_gold = vw.findViewById(R.id.txt_reward_points_gold);
        txt_reward_points_diamond= vw.findViewById(R.id.txt_reward_points_diamond);
        txt_designation_reward= vw.findViewById(R.id.txt_designation_reward);
        txt_designation_reward_silver = vw.findViewById(R.id.txt_designation_reward_silver);
        txt_designation_reward_gold = vw.findViewById(R.id.txt_designation_reward_gold);
        txt_designation_reward_diamond = vw.findViewById(R.id.txt_designation_reward_diamond);

        btn_active_bronze = vw.findViewById(R.id.btn_active_bronze);
        txt_encours_bronze = vw.findViewById(R.id.txt_encours_bronze);

        btn_active_silver = vw.findViewById(R.id.btn_active_silver);
        txt_encours_silver = vw.findViewById(R.id.txt_encours_silver);

        btn_active_gold = vw.findViewById(R.id.btn_active_gold);
        txt_encours_gold = vw.findViewById(R.id.txt_encours_gold);

        btn_active_diamond = vw.findViewById(R.id.btn_active_diamond);
        txt_encours_diamond = vw.findViewById(R.id.txt_encours_diamond);



        for (Reward reward: initial_rewards){

            if (reward.getValeur() == 101){
                txt_reward_points.setText(reward.getPoints() + getString(R.string.text_pts));
                if(Locale.getDefault().getLanguage().equals("en"))
                    txt_designation_reward.setText(reward.getD_en());
                else  if(Locale.getDefault().getLanguage().equals("ar"))
                    txt_designation_reward.setText(reward.getD_ar());
                else
                    txt_designation_reward.setText(reward.getDesignation());

                if (CurrentUser.getInstance().getmCustomer().getPlan() == reward.getValeur()) {
                    txt_encours_bronze.setVisibility(View.VISIBLE);
                    btn_active_bronze.setVisibility(View.GONE);
                } else if (CurrentUser.getInstance().getmCustomer().getPlan() > reward.getValeur()) {
                    txt_encours_bronze.setVisibility(View.GONE);
                    btn_active_bronze.setVisibility(View.GONE);
                } else {
                    txt_encours_bronze.setVisibility(View.GONE);
                    btn_active_bronze.setVisibility(View.VISIBLE);
                }

                if (CurrentUser.getInstance().getmCustomer().getPoints() >= Integer.valueOf(reward.getPoints()) && btn_active_bronze.getVisibility() == View.VISIBLE) {
                    btn_active_bronze.setEnabled(true);
                    btn_active_bronze.setBackground(getContext().getDrawable(R.drawable.round_button));
                } else {
                    btn_active_bronze.setEnabled(false);
                    btn_active_bronze.setBackground(getContext().getDrawable(R.drawable.round_button_white));
                }

                btn_active_bronze.setOnClickListener(view -> new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(getContext().getString(R.string.text_confirm_activation_plan) + reward.getDesignation() + " "  + getContext().getString(R.string.text_a) + " " + reward.getPoints() + getContext().getString(R.string.text_pts))
                        .setPositiveButton(R.string.text_ok, (d, i) -> {
                            this.doPlan(reward);
                        })
                        .setNegativeButton(R.string.text_cancel, (d, i) -> {
                            d.dismiss();
                        }).show());
            }



            if (reward.getValeur() == 102) {
                txt_reward_points_silver.setText(reward.getPoints() + getString(R.string.text_pts));
                if (Locale.getDefault().getLanguage().equals("en"))
                    txt_designation_reward_silver.setText(reward.getD_en());
                else if (Locale.getDefault().getLanguage().equals("ar"))
                    txt_designation_reward_silver.setText(reward.getD_ar());
                else
                    txt_designation_reward_silver.setText(reward.getDesignation());

                if (CurrentUser.getInstance().getmCustomer().getPlan() == reward.getValeur()) {
                    txt_encours_silver.setVisibility(View.VISIBLE);
                    btn_active_silver.setVisibility(View.GONE);
                } else if (CurrentUser.getInstance().getmCustomer().getPlan() > reward.getValeur()) {
                    txt_encours_silver.setVisibility(View.GONE);
                    btn_active_silver.setVisibility(View.GONE);
                } else {
                    txt_encours_silver.setVisibility(View.GONE);
                    btn_active_silver.setVisibility(View.VISIBLE);
                }
                if (CurrentUser.getInstance().getmCustomer().getPoints() >= Integer.valueOf(reward.getPoints()) && btn_active_silver.getVisibility() == View.VISIBLE) {
                    btn_active_silver.setEnabled(true);
                    btn_active_silver.setBackground(getContext().getDrawable(R.drawable.round_button));

                } else {
                    btn_active_silver.setEnabled(false);
                    btn_active_silver.setBackground(getContext().getDrawable(R.drawable.round_button_white));

                }

                btn_active_silver.setOnClickListener(view -> new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(getContext().getString(R.string.text_confirm_activation_plan) + reward.getDesignation() + " "  + getContext().getString(R.string.text_a) + " " + reward.getPoints() + getContext().getString(R.string.text_pts))
                        .setPositiveButton(R.string.text_ok, (d, i) -> {
                            this.doPlan(reward);
                        })
                        .setNegativeButton(R.string.text_cancel, (d, i) -> {
                            d.dismiss();
                        }).show());
            }



            if (reward.getValeur() == 103){
                txt_reward_points_gold.setText(reward.getPoints() + getString(R.string.text_pts));
                if(Locale.getDefault().getLanguage().equals("en"))
                    txt_designation_reward_gold.setText(reward.getD_en());
                else  if(Locale.getDefault().getLanguage().equals("ar"))
                    txt_designation_reward_gold.setText(reward.getD_ar());
                else
                    txt_designation_reward_gold.setText(reward.getDesignation());

                if (CurrentUser.getInstance().getmCustomer().getPlan() == reward.getValeur()) {
                    txt_encours_gold.setVisibility(View.VISIBLE);
                    btn_active_gold.setVisibility(View.GONE);
                } else if (CurrentUser.getInstance().getmCustomer().getPlan() > reward.getValeur()) {
                    txt_encours_gold.setVisibility(View.GONE);
                    btn_active_gold.setVisibility(View.GONE);
                } else {
                    txt_encours_gold.setVisibility(View.GONE);
                    btn_active_gold.setVisibility(View.VISIBLE);
                }
                if (CurrentUser.getInstance().getmCustomer().getPoints() >= Integer.valueOf(reward.getPoints()) && btn_active_gold.getVisibility() == View.VISIBLE) {
                    btn_active_gold.setEnabled(true);
                    btn_active_gold.setBackground(getContext().getDrawable(R.drawable.round_button));

                } else {
                    btn_active_gold.setEnabled(false);
                    btn_active_gold.setBackground(getContext().getDrawable(R.drawable.round_button_white));

                }

                btn_active_gold.setOnClickListener(view -> new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(getContext().getString(R.string.text_confirm_activation_plan) + reward.getDesignation() + " "  + getContext().getString(R.string.text_a) + " " + reward.getPoints() + getContext().getString(R.string.text_pts))
                        .setPositiveButton(R.string.text_ok, (d, i) -> {
                            this.doPlan(reward);
                        })
                        .setNegativeButton(R.string.text_cancel, (d, i) -> {
                            d.dismiss();
                        }).show());
            }



            if (reward.getValeur() == 104){
                txt_reward_points_diamond.setText(reward.getPoints() + getString(R.string.text_pts));
                if(Locale.getDefault().getLanguage().equals("en"))
                    txt_designation_reward_diamond.setText(reward.getD_en());
                else  if(Locale.getDefault().getLanguage().equals("ar"))
                    txt_designation_reward_diamond.setText(reward.getD_ar());
                else
                    txt_designation_reward_diamond.setText(reward.getDesignation());

                if (CurrentUser.getInstance().getmCustomer().getPlan() == reward.getValeur()) {
                    txt_encours_diamond.setVisibility(View.VISIBLE);
                    btn_active_diamond.setVisibility(View.GONE);
                } else if (CurrentUser.getInstance().getmCustomer().getPlan() > reward.getValeur()) {
                    txt_encours_diamond.setVisibility(View.GONE);
                    btn_active_diamond.setVisibility(View.GONE);
                } else {
                    txt_encours_diamond.setVisibility(View.GONE);
                    btn_active_diamond.setVisibility(View.VISIBLE);
                }
                if (CurrentUser.getInstance().getmCustomer().getPoints() >= Integer.valueOf(reward.getPoints()) && btn_active_diamond.getVisibility() == View.VISIBLE) {
                    btn_active_diamond.setEnabled(true);
                    btn_active_diamond.setBackground(getContext().getDrawable(R.drawable.round_button));

                } else {
                    btn_active_diamond.setEnabled(false);
                    btn_active_diamond.setBackground(getContext().getDrawable(R.drawable.round_button_white));

                }

                btn_active_diamond.setOnClickListener(view -> new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(getContext().getString(R.string.text_confirm_activation_plan) + reward.getDesignation() + " "  + getContext().getString(R.string.text_a) + " " + reward.getPoints() + getContext().getString(R.string.text_pts))
                        .setPositiveButton(R.string.text_ok, (d, i) -> {
                            this.doPlan(reward);
                        })
                        .setNegativeButton(R.string.text_cancel, (d, i) -> {
                            d.dismiss();
                        }).show());
            }


        }
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
                    fillData(vw);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Intent intent = new Intent(getActivity(), OldReviewActivity.class);
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

                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.app_name)
                                .setMessage(getString(R.string.text_plan) + " " + reward.getDesignation() + " " + getString(R.string.text_activeee))
                                .setPositiveButton(R.string.text_ok, (a, b) -> {
                                    Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    getActivity().finish();
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
        Intent intent = new Intent(getActivity(), ConsommationActivity.class);
        intent.putExtra("consommation_type", 1);
        startActivityForResult(intent, 10001);
    }

    @OnClick(R.id.b_gagner)
    public void doShowGagner() {
        Intent intent = new Intent(getActivity(), ConsommationActivity.class);
        intent.putExtra("consommation_type", 2);
        startActivityForResult(intent, 10001);
    }

    @OnClick(R.id.b_cons_points)
    public void doConsPoints() {
        Intent intent = new Intent(getActivity(), ConsommationActivity.class);
        intent.putExtra("consommation_type", 0);
        startActivityForResult(intent, 10001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10001)
            refreshApi();
    }
    
    @OnClick(R.id.btn_rateus)
    public void rate_us(){
        Customer customer = new Customer();

        customer.setPoints(CurrentUser.getInstance().getmCustomer().getPoints() + 5);
        customer.setRate_us(1);
        customer.setShare_app(CurrentUser.getInstance().getmCustomer().getShare_app());
        customer.setLike_fb(CurrentUser.getInstance().getmCustomer().getLike_fb());
        customer.setLike_insta(CurrentUser.getInstance().getmCustomer().getLike_insta());

        appApi.updatePointsUser(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    final String appPackageName = getActivity().getPackageName();
                    try{
                        btn_rateus.setEnabled(false);
                        btn_rateus.setBackground(getContext().getDrawable(R.drawable.bg_btn_reward_gray));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            btn_rateus.setTextColor(getContext().getColor(R.color.black));
                        }
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    }catch(android.content.ActivityNotFoundException anfe){
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.btn_shareapp)
    public void share_app(){
        Customer customer = new Customer();

        customer.setPoints(CurrentUser.getInstance().getmCustomer().getPoints() + 5);
        customer.setRate_us(CurrentUser.getInstance().getmCustomer().getRate_us());
        customer.setShare_app(1);
        customer.setLike_fb(CurrentUser.getInstance().getmCustomer().getLike_fb());
        customer.setLike_insta(CurrentUser.getInstance().getmCustomer().getLike_insta());


        appApi.updatePointsUser(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    try {
                        btn_shareapp.setEnabled(false);
                        btn_shareapp.setBackground(getContext().getDrawable(R.drawable.bg_btn_reward_gray));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            btn_shareapp.setTextColor(getContext().getColor(R.color.black));
                        }
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Bassit");
                        String shareMessage = "\n let me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "Choose one"));
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.btn_likefb)
    public void like_fb(){
        Customer customer = new Customer();

        customer.setPoints(CurrentUser.getInstance().getmCustomer().getPoints() + 5);
        customer.setRate_us(CurrentUser.getInstance().getmCustomer().getRate_us());
        customer.setShare_app(CurrentUser.getInstance().getmCustomer().getShare_app());
        customer.setLike_fb(1);
        customer.setLike_insta(CurrentUser.getInstance().getmCustomer().getLike_insta());

        appApi.updatePointsUser(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    btn_likefb.setEnabled(false);
                    btn_likefb.setBackground(getContext().getDrawable(R.drawable.bg_btn_reward_gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        btn_likefb.setTextColor(getContext().getColor(R.color.black));
                    }
                    try {
                        getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/BassitApp")));
                    } catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/BassitApp")));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.btn_likeinsta)
    public void like_insta(){
        Customer customer = new Customer();

        customer.setPoints(CurrentUser.getInstance().getmCustomer().getPoints() + 5);
        customer.setRate_us(CurrentUser.getInstance().getmCustomer().getRate_us());
        customer.setShare_app(CurrentUser.getInstance().getmCustomer().getShare_app());
        customer.setLike_fb(CurrentUser.getInstance().getmCustomer().getLike_fb());
        customer.setLike_insta(1);

        appApi.updatePointsUser(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    btn_likeinsta.setEnabled(false);
                    btn_likeinsta.setBackground(getContext().getDrawable(R.drawable.bg_btn_reward_gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        btn_likeinsta.setTextColor(getContext().getColor(R.color.black));
                    }
                    Uri uri = Uri.parse("https://www.instagram.com/bassitapp");
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/bassitapp")));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}