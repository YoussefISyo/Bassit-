package com.optim.bassit.ui.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.optim.bassit.App;
import com.optim.bassit.PagedScrollView;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.HomeadsFeed;
import com.optim.bassit.models.repreq;
import com.optim.bassit.ui.adapters.CatsAdapter;
import com.optim.bassit.ui.adapters.HomeadsAdapter;
import com.optim.bassit.ui.adapters.HomeadsImagePagerAdapter;
import com.optim.bassit.ui.adapters.myofferAdapter;
import com.optim.bassit.ui.dialogs.CloturerDialogFragment;
import com.optim.bassit.ui.dialogs.EvaluationDialogFragment;
import com.optim.bassit.ui.fragments.AdsFragment;
import com.optim.bassit.utils.LockableViewPager;
import com.optim.bassit.utils.OptimTools;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class myofferActivity extends BaseActivity implements myofferAdapter.ItemClickListener{




    myofferAdapter adapter;
    Dialog myDialog;
    SwipeRefreshLayout swipeView;

    ScrollView scroll;

    @Inject
    AppApi appApi;
    private CatsAdapter catadapter;
    private HomeadsImagePagerAdapter adapterpager;
    private LockableViewPager pager;
    private DotsIndicator dotsIndicator;
    private PagedScrollView paged;
    boolean bb = false;
    View mvw;
    String srch="";
    EditText searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ads);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);
        scroll = findViewById(R.id.scroll);


        findViewById(R.id.searchview).setOnClickListener((a) -> {
            //   Intent intent = new Intent(AdsFragment.this.getActivity(), RechercheActivity.class);
            //  AdsFragment.this.getActivity().startActivity(intent);
        });
        searchView=(EditText) findViewById(R.id.searchview);


        findViewById(R.id.floatingActionButton).setOnClickListener((a) -> {
            Intent intent = new Intent(myofferActivity.this, AdsAddEditActivity.class);
           startActivity(intent);
        });


        findViewById(R.id.b_menu).setVisibility(View.GONE);
        findViewById(R.id.b_map).setVisibility(View.GONE);
        findViewById(R.id.searchview).setVisibility(View.GONE);
        findViewById(R.id.b_map2).setVisibility(View.GONE);
        findViewById(R.id.textView12).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textView12)).setText(getString(R.string.myoffer));
        pager = findViewById(R.id.pager);
        dotsIndicator = findViewById(R.id.dots_indicator);


        RecyclerView recyclerView = findViewById(R.id.rvHome);

        recyclerView.setLayoutManager(new LinearLayoutManager(myofferActivity.this));
       /* int height= findViewById(R.id.floatingActionButton).getHeight();
        recyclerView.setPadding(0,0,0,100);*/

        adapter = new myofferAdapter(this);
        recyclerView.setAdapter(adapter);

        paged = OptimTools.injectPaginationHavingLayoutManager(scroll, recyclerView.getLayoutManager(), (p, t) -> {
            getFeeds(p,srch);
        });
        swipeView = findViewById(R.id.swipe_layout);
        swipeView.setOnRefreshListener(() -> {
            paged.resetState();
            getFeeds(1,srch);
        });

        getFeeds(1,srch);
    }




    private void getFeeds(int page,String srch) {
        show();

        appApi.getmyoffer(page,srch,CurrentUser.getInstance().getmCustomer().getId()+"").enqueue(new Callback<List<HomeadsFeed>>() {
            @Override
            public void onResponse(Call<List<HomeadsFeed>> call, Response<List<HomeadsFeed>> response) {

                //   Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();
                if (response.isSuccessful()) {
                    //  Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();
                    adapter.fill(page, response.body());
                }
                swipeView.setRefreshing(false);
                hide();
            }

            @Override
            public void onFailure(Call<List<HomeadsFeed>> call, Throwable t) {
                swipeView.setRefreshing(false);
                hide();
            }
        });
    }
    @Override
    public void onMessageClick(HomeadsFeed hmeadsFeed) {
        //LocaleHelper.getPersistedData(SplashActivity.this, Locale.getDefault().getLanguage())
        Intent myintent = new Intent(myofferActivity.this, offerActivity.class);
        myintent.putExtra("user",  hmeadsFeed.getUser_id());
        myintent.putExtra("adrs",  hmeadsFeed.getAdrs());
        myintent.putExtra("datereq",  hmeadsFeed.getDatereq());
        myintent.putExtra("des",  hmeadsFeed.getDes());
        myintent.putExtra("dur",  hmeadsFeed.getDurationreq());
        myintent.putExtra("fullname",  hmeadsFeed.getFullName());
        myintent.putExtra("id",  hmeadsFeed.getId()+"");
        if(!Locale.getDefault().getLanguage().equals("en"))
        myintent.putExtra("type",  hmeadsFeed.getTypereq());
        else   myintent.putExtra("type",  hmeadsFeed.getType_en());
        myintent.putExtra("title",  hmeadsFeed.getTitle());
        myintent.putExtra("st",  hmeadsFeed.getStatereq());
        myintent.putExtra("image",  hmeadsFeed.getPinLink());
        myintent.putExtra("trailer",  "trailer");
        myintent.putExtra("urgence",  hmeadsFeed.getUrgence());
        myintent.putExtra("unity",  hmeadsFeed.getUnity());
        myintent.putExtra("quantity",  hmeadsFeed.getQuantity());
        myintent.putExtra("date_start",  hmeadsFeed.getDate_start());
        myintent.putExtra("official_account",  hmeadsFeed.getOfficial_account());
        myintent.putExtra("price",  hmeadsFeed.getPrix());
        startActivity(myintent);


    }
    @Override
    public void onrmoveClick(HomeadsFeed homefeed) {
        AlertYesNo(R.string.confirm_delete_offer, getString(R.string.text_delete), getString(R.string.text_cancel), () -> {


            show();
            appApi.deleteoffer(homefeed.getId())
                    .enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            hide();
                            if (!response.isSuccessful() || response.body().isError()) {
                                Alert(response.body().getMessage());
                            } else {
                                getFeeds(1,"");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            hide();
                            Alert(t.getMessage());

                        }
                    });

        });


    }

    @Override
    public void onstClick(HomeadsFeed homefeed) {
// setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(myofferActivity.this);
        builder.setTitle(getString(R.string.choose_st));
// add a radio button list
        String[] animals = {getString(R.string.fini_suc),getString(R.string.fini_witout_suc)};
       int checkedItem = 0; // cow
        final int[] checked = {0};
        builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //    Toast.makeText(myofferActivity.this,which+"",Toast.LENGTH_LONG).show();
               checked[0] =which;
            }
        });
// add OK and Cancel buttons
        builder.setPositiveButton(getString(R.string.text_ok), new DialogInterface.OnClickListener() {



            @Override
            public void onClick(DialogInterface dialog, int which) {
            //    Toast.makeText(myofferActivity.this, checked[0] +"",Toast.LENGTH_LONG).show();
if(checked[0]==0) {
    FragmentManager fm = getSupportFragmentManager();
    EvaluationDialogFragment editNameDialogFragment = EvaluationDialogFragment.newInstance(0, "offer", homefeed.getId() + "", "myoffer");
    editNameDialogFragment.show(fm, "fragment_cloturer");
}else {
    // user clicked OK
    show();
    appApi.changestoffer(homefeed.getId(),  "2")
            .enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    hide();
                    if (!response.isSuccessful() || response.body().isError()) {
                        Alert(response.body().getMessage());
                    } else {
                        getFeeds(1, "");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    hide();
                    Alert(t.getMessage());

                }
            });

}
            }
        });
        builder.setNegativeButton(getString(R.string.text_cancel), null);
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();



    }





}
