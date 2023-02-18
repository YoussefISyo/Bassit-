package com.optim.bassit.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.optim.bassit.App;
import com.optim.bassit.PagedScrollView;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.base.BaseFragment;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Ads;
import com.optim.bassit.models.Apidata;
import com.optim.bassit.models.Apidatahome;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.ui.activities.AddDemandServiceActivity;
import com.optim.bassit.ui.activities.ChatActivity;
import com.optim.bassit.ui.activities.EditInformationActivity;
import com.optim.bassit.ui.activities.FilterActivity;
import com.optim.bassit.ui.activities.MainActivity;
import com.optim.bassit.ui.activities.MapActivity;
import com.optim.bassit.ui.activities.MapFragActivity;
import com.optim.bassit.ui.activities.ProfileActivity;
import com.optim.bassit.ui.activities.RechercheActivity;
import com.optim.bassit.ui.activities.ServiceActivity;
import com.optim.bassit.ui.activities.mynotifiActivity;
import com.optim.bassit.ui.adapters.CatsAdapter;
import com.optim.bassit.ui.adapters.HomeAdapter;
import com.optim.bassit.ui.adapters.HomeImagePagerAdapter;
import com.optim.bassit.utils.LockableViewPager;
import com.optim.bassit.utils.MapHelper;
import com.optim.bassit.utils.OptimTools;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements HomeAdapter.ItemClickListener, CatsAdapter.ItemClickListener, MainActivity.gotPositionListener {

    HomeAdapter adapter;
    Dialog myDialog;
    SwipeRefreshLayout swipeView;

    ScrollView scroll;

    @Inject
    AppApi appApi;
    private CatsAdapter catadapter;
    private HomeImagePagerAdapter adapterpager;
    private LockableViewPager pager;
    private DotsIndicator dotsIndicator;
    private PagedScrollView paged;
    Handler mHandler=new Handler();

    Boolean run=true;
    public static Boolean runing=true;
    public  void timer2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    try {
                        Thread.sleep(4000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                mvw.findViewById(R.id.remb).setVisibility(View.GONE);
                                run=false;
                            }
                        });

                    } catch(Exception e){
                    }}
            }

        }).start();
    }
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }

    boolean bb = false;

    View mvw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_home, container, false);
        mvw = vw;
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this.getActivity());

        scroll = vw.findViewById(R.id.scroll);


        vw.findViewById(R.id.searchview).setOnClickListener((a) -> {
            Intent intent = new Intent(HomeFragment.this.getActivity(), FilterActivity.class);
            HomeFragment.this.getActivity().startActivity(intent);
        });
        vw.findViewById(R.id.b_notifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_profile = new Intent(HomeFragment.this.getActivity(), mynotifiActivity.class);
                startActivity(intent_profile);
                ((TextView)  vw.findViewById(R.id.notifitext)).setText("");
                vw.findViewById(R.id.notifitext).setVisibility(View.GONE);
            }
        });
        vw.findViewById(R.id.b_menu).setOnClickListener((b) -> {
            drawer = getActivity().findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
        });
        vw.findViewById(R.id.b_map).setOnClickListener((a) -> {
            Intent intent = new Intent(HomeFragment.this.getActivity(), MapFragActivity.class);
            HomeFragment.this.getActivity().startActivity(intent);
        });
        pager = vw.findViewById(R.id.pager);
        dotsIndicator = vw.findViewById(R.id.dots_indicator);


        RecyclerView recyclerView = vw.findViewById(R.id.rvHome);

        recyclerView.setLayoutManager(new LinearLayoutManager(vw.getContext()));


        adapter = new HomeAdapter(this, getActivity());
        recyclerView.setAdapter(adapter);

        paged = OptimTools.injectPaginationHavingLayoutManager(scroll, recyclerView.getLayoutManager(), (p, t) -> {
            getFeeds(p);
        });

        RecyclerView rvcats = vw.findViewById(R.id.lst_cats);
        rvcats.setLayoutManager(new LinearLayoutManager(vw.getContext(), LinearLayoutManager.HORIZONTAL, false));
        catadapter = new CatsAdapter(this);
        rvcats.setAdapter(catadapter);

        swipeView = vw.findViewById(R.id.swipe_layout);
        swipeView.setOnRefreshListener(() -> {
            paged.resetState();
            getFeeds(1);
        });

        CardView card = vw.findViewById(R.id.card_slider);

        ViewTreeObserver vto = card.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    card.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    card.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = card.getMeasuredWidth();

                int az = (int) (width * 0.4);

                card.getLayoutParams().height = az;
            }
        });

        card.getLayoutParams().height = (int) OptimTools.dpToPixels(140, getContext());
        fillCats();


        t_address = vw.findViewById(R.id.t_address);

        t_address.setOnClickListener((a) -> {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            intent.putExtra("user", true);
            startActivityForResult(intent, 104);

        });

        if (alreadyPosition)
            onGotPosition();
        return vw;
    }

    public static List<Ads> listAds;


    static boolean alreadyPosition;
int siz=0;
    private void getAds() {
        appApi.getAds().enqueue(new Callback<List<Ads>>() {
            @Override
            public void onResponse(Call<List<Ads>> call, Response<List<Ads>> response) {
                if (response.isSuccessful()) {

                    if (response.body().size() > 0) {
                        siz=response.body().size();
                        adapterpager = new HomeImagePagerAdapter(getChildFragmentManager());
                        adapterpager.fillData(response.body());

                        listAds = new ArrayList<>();
                        RechercheActivity.listSearchAds = new ArrayList<>();
                        for (Ads a : response.body()) {
                            if (a.getPlace() == 1)
                                listAds.add(a);
                            else if (a.getPlace() == 2)
                                RechercheActivity.listSearchAds.add(a);
                        }

                        pager.setAdapter(adapterpager);

                        dotsIndicator.setViewPager(pager);

                        loopAds();

                    }

                } else {

                }

                paged.resetState();
                getFeeds(1);


            }

            @Override
            public void onFailure(Call<List<Ads>> call, Throwable t) {
                paged.resetState();
                getFeeds(1);
            }
        });
    }

    boolean bpassed = false;

    private void loopAds() {
        if (bpassed)
            return;
        bpassed = true;

        final Handler handler = new Handler();
        final Runnable Update = () -> {

            int a = pager.getCurrentItem() + 1;
           // pager.getAdapter().getCount();
            try{
                if (a >=  pager.getAdapter().getCount())//pager.getChildCount()+2)
                    a = 0;
            }catch (Exception e){
            //    System.out.println("pager.getAdapter().getCount()");
             //   Toast.makeText(getContext(),"pager.getAdapter().getCount()",Toast.LENGTH_LONG).show();
                if (a >=  pager.getChildCount())
                    a = 0;
            }

            pager.setCurrentItem(a, true);
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 5000);


    }


    public TextView t_address;


    private void getFeeds(int page) {
        show(mvw);
        appApi.getHomeFeed(page,"getnotifi").enqueue(new Callback<Apidatahome>() {
            @Override
            public void onResponse(Call<Apidatahome> call, Response<Apidatahome> response) {
               /* if (response.isSuccessful()) {
                    adapter.fill(page, response.body(), listAds);
                }*/
                if (response.isSuccessful()) {
                    adapter.fill(page, response.body().getData(), listAds);
                    String noti=response.body().getNotifi();
                    if(noti==null || noti.isEmpty() || noti.equals("0")) mvw.findViewById(R.id.notifitext).setVisibility(View.GONE);
                    else {
                        ((TextView)  mvw.findViewById(R.id.notifitext)).setText(noti);
                        mvw.findViewById(R.id.notifitext).setVisibility(View.VISIBLE);
                    }

                }
                swipeView.setRefreshing(false);
                hide(mvw);
            }

            @Override
            public void onFailure(Call<Apidatahome> call, Throwable t) {
                swipeView.setRefreshing(false);
                hide(mvw);
            }
        });
    }

    @Override
    public void onMessageClick(HomeFeed homefeed) {

        Intent myintent = new Intent(getContext(), AddDemandServiceActivity.class);
        myintent.putExtra("service", homefeed);
       // startActivityForResult(myintent, 0);
        startActivity(myintent);
    }

    @Override
    public void onShowProfile(HomeFeed homefeed) {
        Intent myintent = new Intent(getActivity(), ProfileActivity.class);
        myintent.putExtra("user_id", homefeed.getUser_id());
        startActivity(myintent);
    }

    @Override
    public void onCardClick(HomeFeed homefeed) {
        Intent myintent = new Intent(getActivity(), ServiceActivity.class);
        boolean isme = false;
        if (CurrentUser.getInstance().isLogin() && CurrentUser.getInstance().getmCustomer().getId() == homefeed.getUser_id())
            isme = true;

        myintent.putExtra("isme", false);
        myintent.putExtra("parcel_service", homefeed);
        startActivity(myintent);
    }

    @Override
    public void onAdsClick(HomeFeed homefeed) {
        try {
            switch (homefeed.getIsg()) {
                case 0:


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homefeed.getTitle()));
                    startActivity(browserIntent);
                    break;
                case 1:
                    Integer u_id = Integer.valueOf(homefeed.getTitle());
                    if (u_id == 0)
                        return;
                    Intent myintent = new Intent(getActivity(), ProfileActivity.class);
                    myintent.putExtra("user_id", u_id);
                    startActivity(myintent);
                    break;
                case 2:
                    Integer s_id = Integer.valueOf(homefeed.getTitle());
                    if (s_id == 0)
                        return;
                    appApi.getOneService(s_id).enqueue(new Callback<HomeFeed>() {
                        @Override
                        public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
                            if (response.isSuccessful()) {
                                if (response.body() == null || response.body().getName() == null)
                                    return;
                                Intent myintent = new Intent(getActivity(), ServiceActivity.class);
                                myintent.putExtra("isme", false);
                                myintent.putExtra("parcel_service", response.body());
                                startActivity(myintent);
                            }

                        }

                        @Override
                        public void onFailure(Call<HomeFeed> call, Throwable t) {

                        }
                    });

                    break;
            }
        } catch (Exception ex) {

        }

    }

    @Override
    public void onItemClick(Categorie cat) {
        if (AppData.getInstance().getCategories() == null || AppData.getInstance().getSousCategories() == null) {
            ((BaseActivity) getActivity()).Alert(R.string.chargement_patientez);
            return;
        }
        Intent intent = new Intent(getActivity(), RechercheActivity.class);
        intent.putExtra("CAT", cat.getId());
        getActivity().startActivity(intent);
    }

    public void fillCats() {
        catadapter.fill(AppData.getInstance().getCategories());
    }

    @Override
    public void onGotPosition() {
        alreadyPosition = true;
        t_address.setVisibility(View.VISIBLE);
        if (t_address != null) {
            if (CurrentUser.getInstance().getFullAddress() == null || CurrentUser.getInstance().getFullAddress().matches(""))
                t_address.setText(R.string.position);
            else {
                t_address.setText(CurrentUser.getInstance().getFullAddress());
            }
        }
        getAds();
    }

    DrawerLayout drawer;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 104) {
            paged.resetState();
            getFeeds(1);
            onGotPosition();
        }
    }

}