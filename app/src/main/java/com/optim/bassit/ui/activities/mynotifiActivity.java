package com.optim.bassit.ui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
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
import com.optim.bassit.models.DemandService;
import com.optim.bassit.models.HomeadsFeed;
import com.optim.bassit.models.Tache;
import com.optim.bassit.models.notifi;
import com.optim.bassit.ui.adapters.CatsAdapter;
import com.optim.bassit.ui.adapters.HomeadsImagePagerAdapter;
import com.optim.bassit.ui.adapters.mynotifiAdapter;
import com.optim.bassit.ui.adapters.myofferAdapter;
import com.optim.bassit.ui.dialogs.EvaluationDialogFragment;
import com.optim.bassit.utils.LockableViewPager;
import com.optim.bassit.utils.OptimTools;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class mynotifiActivity extends BaseActivity implements mynotifiAdapter.ItemClickListener{




    mynotifiAdapter adapter;
    Dialog myDialog;
    SwipeRefreshLayout swipeView;

    ScrollView scroll;

    @Inject
    AppApi appApi;
    private LockableViewPager pager;
    private DotsIndicator dotsIndicator;
    private PagedScrollView paged;
    boolean bb = false;
    View mvw;
    String srch="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mynotifi);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);
        scroll = findViewById(R.id.scroll);

        pager = findViewById(R.id.pager);
        dotsIndicator = findViewById(R.id.dots_indicator);


        RecyclerView recyclerView = findViewById(R.id.rvHome);

        recyclerView.setLayoutManager(new LinearLayoutManager(mynotifiActivity.this));
       /* int height= findViewById(R.id.floatingActionButton).getHeight();
        recyclerView.setPadding(0,0,0,100);*/

        adapter = new mynotifiAdapter(this);
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

        appApi.getmynotifi(page,srch,CurrentUser.getInstance().getmCustomer().getId()+"").enqueue(new Callback<List<notifi>>() {
            @Override
            public void onResponse(Call<List<notifi>> call, Response<List<notifi>> response) {

                //   Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();
                if (response.isSuccessful()) {
                    //  Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();
                    adapter.fill(page, response.body());
                }
                swipeView.setRefreshing(false);
                hide();
            }

            @Override
            public void onFailure(Call<List<notifi>> call, Throwable t) {
                swipeView.setRefreshing(false);
                hide();
            }
        });
    }
    @Override
    public void onMessageClick(notifi notifi) {
        //LocaleHelper.getPersistedData(SplashActivity.this, Locale.getDefault().getLanguage())



        if(notifi.getOpr_type().equals("chat"))openchat(notifi);
        else if(notifi.getOpr_type().equals("repreq"))openoffer(notifi);
        else if(notifi.getOpr_type().equals("req"))openoffer(notifi);
        else  if(notifi.getOpr_type().equals("service")){
           // Toast.makeText(this,notifi.getUser_id(),Toast.LENGTH_LONG).show();
            Intent myintent = new Intent(mynotifiActivity.this, ProfileActivity.class);
            int id=0;
            try {
                id=Integer.parseInt(notifi.getUser_id());
            }catch (Exception e){
                id=0;
            }

            myintent.putExtra("user_id", id);
            startActivity(myintent);
        } else  if(notifi.getOpr_type().equals("bonus")){
            Intent myintent = new Intent(this, ConsommationActivity.class);
            myintent.putExtra("consommation_type", 101);
            startActivity(myintent);
        }else if (notifi.getOpr_type().equals("demande service")){
            openDemandeService(notifi);
        }
        else Toast.makeText(this,"Aucan action",Toast.LENGTH_LONG).show();
       // else if(notifi.getOpr_type().equals("service"))type.setText(itemView.getContext().getString(R.string.new_service));
       // else if(notifi.getOpr_type().equals("bonus"))type.setText(itemView.getContext().getString(R.string.new_bonus));


       /* Intent myintent = new Intent(mynotifiActivity.this, offerActivity.class);
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
        startActivity(myintent);*/


    }

    private void openDemandeService(notifi notifi) {
        appApi.getDemandService(Integer.parseInt(notifi.getOpr_id())).enqueue(new Callback<DemandService>() {
            @Override
            public void onResponse(Call<DemandService> call, Response<DemandService> response) {
                Intent mIntent= new Intent(mynotifiActivity.this, DemandDetailsActivity.class);
                mIntent.putExtra("demand", response.body());
                startActivity(mIntent);
            }

            @Override
            public void onFailure(Call<DemandService> call, Throwable t) {

            }
        });

    }

    private void openoffer(notifi notifi) {
        appApi.getOneoffer(notifi.getOpr_id()).enqueue(new Callback<HomeadsFeed>() {
            @Override
            public void onResponse(Call<HomeadsFeed> call, Response<HomeadsFeed> response) {

                if (response.isSuccessful()) {
                    HomeadsFeed  hmeadsFeed = response.body();
                    Intent myintent = new Intent(mynotifiActivity.this, offerActivity.class);
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
                    myintent.putExtra("status",  hmeadsFeed.getStatus());
                    myintent.putExtra("price",  hmeadsFeed.getPrix());
                    startActivity(myintent);
                } else {
                    AlertWait(R.string.chat_nexiste_plus, () -> {
                        finish();
                    });
                }
            }

            @Override
            public void onFailure(Call<HomeadsFeed> call, Throwable t) {
                AlertWait(R.string.chat_nexiste_plus, () -> {
                    finish();
                });
            }
        });
    }

    private void openchat(notifi notifi) {
        appApi.getOneTaches(notifi.getOpr_id()).enqueue(new Callback<Tache>() {
            @Override
            public void onResponse(Call<Tache> call, Response<Tache> response) {

                if (response.isSuccessful()) {
                  Tache  tache = response.body();
                    Intent intent = new Intent(mynotifiActivity.this, ChatActivity.class);
                    intent.putExtra("typec", tache.getTypec());
                    intent.putExtra("offer_id", tache.getOffer_id());
                    intent.putExtra("tache", tache);
                    startActivityForResult(intent, 0);
                } else {
                    AlertWait(R.string.chat_nexiste_plus, () -> {
                        finish();
                    });
                }
            }

            @Override
            public void onFailure(Call<Tache> call, Throwable t) {
                AlertWait(R.string.chat_nexiste_plus, () -> {
                    finish();
                });
            }
        });
    }


    void  opendialog() {

    }




}
