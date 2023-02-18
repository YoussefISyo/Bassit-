package com.optim.bassit.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.florent37.diagonallayout.DiagonalLayout;
import com.optim.bassit.App;
import com.optim.bassit.PagedScrollView;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.base.BaseFragment;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Ads;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Apidata;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.HomeadsFeed;
import com.optim.bassit.ui.activities.AdsAddEditActivity;
import com.optim.bassit.ui.activities.ChatActivity;
import com.optim.bassit.ui.activities.MainActivity;
import com.optim.bassit.ui.activities.MapActivity;
import com.optim.bassit.ui.activities.MapFragActivity;
import com.optim.bassit.ui.activities.ProfileActivity;
import com.optim.bassit.ui.activities.RechercheActivity;
import com.optim.bassit.ui.activities.ServiceActivity;
import com.optim.bassit.ui.activities.myofferActivity;
import com.optim.bassit.ui.activities.offerActivity;
import com.optim.bassit.ui.adapters.CatsAdapter;
import com.optim.bassit.ui.adapters.HomeAdapter;
import com.optim.bassit.ui.adapters.HomeImagePagerAdapter;
import com.optim.bassit.ui.adapters.HomeadsAdapter;
import com.optim.bassit.ui.adapters.HomeadsImagePagerAdapter;
import com.optim.bassit.utils.LockableViewPager;
import com.optim.bassit.utils.OptimTools;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsFragment extends BaseFragment implements HomeadsAdapter.ItemClickListener, CatsAdapter.ItemClickListener, MainActivity.gotPositionListener {

    HomeadsAdapter adapter;
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
    public AdsFragment() {
        // Required empty public constructor
    }

    public static AdsFragment newInstance() {
        AdsFragment fragment = new AdsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }

    boolean bb = false;
String srch="";
    View mvw;
    EditText searchView;
    Button btnpro,btnclient;
    DiagonalLayout cntr,dpro,dclient;
    ConstraintLayout clpro,clclient;
    String side="pro";
  //  @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_ads, container, false);
        mvw = vw;
        btnclient=(Button) vw.findViewById(R.id.button4);
        btnpro=(Button) vw.findViewById(R.id.button5);

        cntr=(DiagonalLayout) vw.findViewById(R.id.dcntr);
        dpro=(DiagonalLayout) vw.findViewById(R.id.dpro);
        dclient=(DiagonalLayout) vw.findViewById(R.id.dclient);

        clpro=(ConstraintLayout) vw.findViewById(R.id.clpro);
        clclient=(ConstraintLayout) vw.findViewById(R.id.clclient);
        if(Locale.getDefault().getLanguage().equals("ar"))
        {
            cntr.setDiagonalDirection(DiagonalLayout.DIRECTION_LEFT);
            cntr.setDiagonalPosition(DiagonalLayout.POSITION_LEFT);
        }
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this.getActivity());

        scroll = vw.findViewById(R.id.scroll);

            btnclient.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 clclient.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                 clpro.setBackgroundColor(getResources().getColor(R.color.white));
                 if(Locale.getDefault().getLanguage().equals("ar"))
                 {
                     cntr.setDiagonalDirection(DiagonalLayout.DIRECTION_LEFT);
                     cntr.setDiagonalPosition(DiagonalLayout.POSITION_RIGHT);
                 }else {
                     cntr.setDiagonalDirection(DiagonalLayout.DIRECTION_RIGHT);
                     cntr.setDiagonalPosition(DiagonalLayout.POSITION_LEFT);
                 }
                 getFeeds(1,srch,side);
                 side="client";
           }
         });
        btnpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clclient.setBackgroundColor(getResources().getColor(R.color.white));
                clpro.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                if(Locale.getDefault().getLanguage().equals("ar"))
                {
                    cntr.setDiagonalDirection(DiagonalLayout.DIRECTION_LEFT);
                    cntr.setDiagonalPosition(DiagonalLayout.POSITION_LEFT);
                }else {
                    cntr.setDiagonalDirection(DiagonalLayout.DIRECTION_RIGHT);
                    cntr.setDiagonalPosition(DiagonalLayout.POSITION_RIGHT);
                }
                side="pro";
                getFeeds(1,srch,side);
            }
        });
        vw.findViewById(R.id.searchview).setOnClickListener((a) -> {
         //   Intent intent = new Intent(AdsFragment.this.getActivity(), RechercheActivity.class);
          //  AdsFragment.this.getActivity().startActivity(intent);
        });
         searchView=(EditText) vw.findViewById(R.id.searchview);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                srch=s.toString();
                if(s.toString().trim().isEmpty())srch="";
                getFeeds(1,srch,side);
            }
        });

        vw.findViewById(R.id.floatingActionButton).setOnClickListener((a) -> {
            Intent intent = new Intent(AdsFragment.this.getActivity(), AdsAddEditActivity.class);
            AdsFragment.this.getActivity().startActivity(intent);
        });
        vw.findViewById(R.id.b_menu).setOnClickListener((b) -> {
            Intent intent = new Intent(AdsFragment.this.getActivity(), myofferActivity.class);
            AdsFragment.this.getActivity().startActivity(intent);
        });
        vw.findViewById(R.id.b_map).setOnClickListener((a) -> {
            vw.findViewById(R.id.searchview).setVisibility(View.VISIBLE);
            vw.findViewById(R.id.b_map2).setVisibility(View.VISIBLE);
            vw.findViewById(R.id.textView12).setVisibility(View.GONE);
            vw.findViewById(R.id.b_map).setVisibility(View.GONE);
        });
        vw.findViewById(R.id.b_map2).setOnClickListener((a) -> {
            if(!searchView.getText().toString().trim().isEmpty())
            {
                searchView.setText("");
                srch="";
                getFeeds(1,srch,side);
            }

            vw.findViewById(R.id.searchview).setVisibility(View.GONE);
            vw.findViewById(R.id.b_map2).setVisibility(View.GONE);
            vw.findViewById(R.id.textView12).setVisibility(View.VISIBLE);
            vw.findViewById(R.id.b_map).setVisibility(View.VISIBLE);
        });
        pager = vw.findViewById(R.id.pager);
        dotsIndicator = vw.findViewById(R.id.dots_indicator);


        RecyclerView recyclerView = vw.findViewById(R.id.rvHome);

        recyclerView.setLayoutManager(new LinearLayoutManager(vw.getContext()));
      /*  int height= vw.findViewById(R.id.floatingActionButton).getHeight().;
        recyclerView.setPadding(0,0,0,height);
        Toast.makeText(getContext(),height+"",Toast.LENGTH_LONG).show();*/
        adapter = new HomeadsAdapter(this, getActivity());
        recyclerView.setAdapter(adapter);

        paged = OptimTools.injectPaginationHavingLayoutManager(scroll, recyclerView.getLayoutManager(), (p, t) -> {
            getFeeds(p,srch,side);
        });

        RecyclerView rvcats = vw.findViewById(R.id.lst_cats);
        rvcats.setLayoutManager(new LinearLayoutManager(vw.getContext(), LinearLayoutManager.HORIZONTAL, false));
        catadapter = new CatsAdapter(this);
        rvcats.setAdapter(catadapter);

        swipeView = vw.findViewById(R.id.swipe_layout);
        swipeView.setOnRefreshListener(() -> {
            paged.resetState();
            getFeeds(1,srch,side);
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
        SharedPreferences prefs = getContext().getSharedPreferences("offers",0);
        String Voffers = prefs.getString("Voffers","0");
        String Vrep = prefs.getString("Vrep","0");
        Customer me = CurrentUser.getInstance().getmCustomer();
        me.getId();
       if(!CurrentUser.getInstance().getmCustomer().getVoffers().equals("1") && Voffers.equals("0"))
        showdialogAds();
        if(!CurrentUser.getInstance().getmCustomer().getVoffers().equals("1") && Voffers.equals("1")) updateVoffer();
        if(!CurrentUser.getInstance().getmCustomer().getVrep().equals("1") && Vrep.equals("1")) updateVrep();
        getAds();
        return vw;
    }

    private void updateVoffer() {
        Customer me = CurrentUser.getInstance().getmCustomer();
        me.getId();
        appApi.Voffers(me.getId())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (!response.isSuccessful() || response.body().isError()) {
                            Alert(response.body().getMessage());
                        } else {
                            CurrentUser.getInstance().getmCustomer().setVoffers("1");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                        Alert(t.getMessage());

                    }
                });
    }
    private void updateVrep() {
        Customer me = CurrentUser.getInstance().getmCustomer();
        me.getId();
        appApi.Vreq(me.getId())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (!response.isSuccessful() || response.body().isError()) {
                            Alert(response.body().getMessage());
                        } else {
                            CurrentUser.getInstance().getmCustomer().setVrep("1");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                        Alert(t.getMessage());

                    }
                });
    }
    private void showdialogAds() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_rimand_ads);
        Button dialogButtonok = (Button) dialog.findViewById(R.id.b_confirmer);
        dialogButtonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getContext().getSharedPreferences("offers",0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Voffers", "1");
                editor.apply();
                Customer me = CurrentUser.getInstance().getmCustomer();
                me.getId();
                appApi.Voffers(me.getId())
                        .enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                                if (!response.isSuccessful() || response.body().isError()) {
                                    Alert(response.body().getMessage());
                                } else {
                                    CurrentUser.getInstance().getmCustomer().setVoffers("1");
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {

                                Alert(t.getMessage());

                            }
                        });
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public static List<Ads> listAds;


    static boolean alreadyPosition;
int siz=0;
    private void getAds() {
        appApi.getofferAds().enqueue(new Callback<List<Ads>>() {
            @Override
            public void onResponse(Call<List<Ads>> call, Response<List<Ads>> response) {
                if (response.isSuccessful()) {

                    if (response.body().size() > 0) {
                        siz=response.body().size();
                        adapterpager = new HomeadsImagePagerAdapter(getChildFragmentManager());
                        adapterpager.fillData(response.body());

                        listAds = new ArrayList<>();
                    //    RechercheActivity.listSearchAds = new ArrayList<>();
                        for (Ads a : response.body()) {
                            if (a.getPlace() == 3)
                                listAds.add(a);
                          //   else if (a.getPlace() == 2)
                     //        RechercheActivity.listSearchAds.add(a);
                        }

                        pager.setAdapter(adapterpager);

                        dotsIndicator.setViewPager(pager);

                        //loopAds();

                    }

                } else {

                }

                paged.resetState();
                getFeeds(1,srch,side);


            }

            @Override
            public void onFailure(Call<List<Ads>> call, Throwable t) {
                paged.resetState();
                getFeeds(1,srch,side);
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
             //   System.out.println("pager.getAdapter().getCount()");
              //  Toast.makeText(getContext(),"pager.getAdapter().getCount()",Toast.LENGTH_LONG).show();
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

    String trailer="";
    String dif="";
    String date_now="";
    private void getFeeds(int page,String srch,String side) {
        show(mvw);

        appApi.getHomeadsFeed(page,srch,side).enqueue(new Callback<Apidata>() {
            @Override
            public void onResponse(Call<Apidata> call, Response<Apidata> response) {
              //  response.message();
             //   Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();

                if (response.isSuccessful()) {
                    adapter.fill(page, response.body().getData(), listAds);
                    trailer=response.body().getMessage();
                    dif=response.body().getDif();
                    date_now=response.body().getDate_now();
                }
                swipeView.setRefreshing(false);
                hide(mvw);
            }

            @Override
            public void onFailure(Call<Apidata> call, Throwable t) {
                swipeView.setRefreshing(false);
                hide(mvw);
            }
        });
    }

    @Override
    public void onMessageClick(HomeadsFeed hmeadsFeed) {
       /* Intent myintent = new Intent(getContext(),ChatActivity.class);
        myintent.putExtra("notification",hmeadsFeed.getUser_id());
        startActivity(myintent);*/
       Intent myintent = new Intent(getContext(), offerActivity.class);
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
        myintent.putExtra("trailer",  trailer);
        myintent.putExtra("dif",  dif);
        myintent.putExtra("date_now",  date_now);
        myintent.putExtra("urgence",  hmeadsFeed.getUrgence());
        myintent.putExtra("unity",  hmeadsFeed.getUnity());
        myintent.putExtra("quantity",  hmeadsFeed.getQuantity());
        myintent.putExtra("date_start",  hmeadsFeed.getDate_start());
        myintent.putExtra("official_account",  hmeadsFeed.getOfficial_account());
        myintent.putExtra("status",  hmeadsFeed.getStatus());
        myintent.putExtra("price",  hmeadsFeed.getPrix());
        startActivity(myintent);
    }


    @Override
    public void onremoveClick(HomeadsFeed hmeadsFeed) {
        AlertYesNo(R.string.confirm_delete_offer, getString(R.string.text_delete), getString(R.string.text_cancel), () -> {


            show(mvw);
            appApi.deleteoffer(hmeadsFeed.getId())
                    .enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            hide(mvw);
                            if (!response.isSuccessful() || response.body().isError()) {
                                Alert(response.body().getMessage());
                            } else {
                                getFeeds(1,srch,side);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            hide(mvw);
                            Alert(t.getMessage());

                        }
                    });

        });


    }
    @Override
    public void onAdsClick(HomeadsFeed homefeed) {
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
            getFeeds(1,srch,side);
            onGotPosition();
        }
    }

}