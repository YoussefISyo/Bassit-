package com.optim.bassit.ui.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.optim.bassit.App;
import com.optim.bassit.PagedScrollView;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.HomeadsFeed;
import com.optim.bassit.models.Service;
import com.optim.bassit.models.repreq;
import com.optim.bassit.ui.adapters.CatsAdapter;
import com.optim.bassit.ui.adapters.HomeadsImagePagerAdapter;
import com.optim.bassit.ui.adapters.myofferAdapter;
import com.optim.bassit.ui.adapters.offerAdapter;
import com.optim.bassit.utils.LockableViewPager;
import com.optim.bassit.utils.OptimTools;
import com.skyhope.showmoretextview.ShowMoreTextView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class offerActivity extends BaseActivity implements offerAdapter.ItemClickListener{

    offerAdapter adapter;
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
    String id="0";
    String user="0";
    boolean bb = false;
    View mvw;
    String trailer="";
    String srch="";
    int service_id = 0;
    boolean bme=false;
  //  HomeadsFeed homefeed;
    EditText searchView;
    Handler mHandler=new Handler();
    public  void timer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Thread.sleep(1000);


                   mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            cadrshow.setVisibility(View.VISIBLE);

                            //runb=false;
                        }
                    });
                    Thread.sleep(8000);
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            cadrshow.setVisibility(View.GONE);

                            //runb=false;
                        }
                    });
                } catch(Exception e){
                }
            }

        }).start();
    }
    TextView show;
    LinearLayout cadrshow;
    Toolbar mToolbarDetailsOffer;

    DatePickerDialog picker;
    List<String> unities;

    List<Service> all;
    List<String> cats = new ArrayList<>();

    @BindView(R.id.img_active_user)
    ImageView img_active_user;
    @BindView(R.id.txt_active)
    TextView txt_active;
    @BindView(R.id.btn_show_nego)
    Button btn_show_nego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);
        scroll = findViewById(R.id.scroll);
        mToolbarDetailsOffer = findViewById(R.id.mToolbarDetailsOffer);


        setSupportActionBar(mToolbarDetailsOffer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
     /*   findViewById(R.id.searchview).setOnClickListener((a) -> {
            //   Intent intent = new Intent(AdsFragment.this.getActivity(), RechercheActivity.class);
            //  AdsFragment.this.getActivity().startActivity(intent);
        });
        searchView=(EditText) findViewById(R.id.searchview);

        findViewById(R.id.b_menu).setVisibility(View.GONE);
        findViewById(R.id.b_map).setVisibility(View.GONE);
        findViewById(R.id.searchview).setVisibility(View.GONE);
        findViewById(R.id.b_map2).setVisibility(View.GONE);
        findViewById(R.id.textView12).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.textView12)).setText(getString(R.string.myoffer));*/

       // homefeed = getIntent().getParcelableExtra("service");
        findViewById(R.id.floatingActionButton).setOnClickListener((a) -> {
            Intent intent = new Intent(offerActivity.this, repreqAddEditActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        });
        findViewById(R.id.button3).setOnClickListener((a) -> {
            if(!CurrentUser.getInstance().isPro()){
                showdialogAdsclient();
                return;
            }
            if(CurrentUser.getInstance().isPro()){
                if(!trailer.equals("trailer")){
                    if(CurrentUser.getInstance().getmCustomer().getPlan()==101 || CurrentUser.getInstance().getmCustomer().getPlan()==102 ||
                            CurrentUser.getInstance().getmCustomer().getPlan()==103 ||CurrentUser.getInstance().getmCustomer().getPlan()==104){
                        try {
                            if(getIntent().hasExtra("dif")) {
                                double dif = Double.parseDouble(getIntent().getStringExtra("dif"));
                                Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(getIntent().getStringExtra("date_now"));
                                Date date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(getIntent().getStringExtra("datereq"));
                                //  long diffInMillies = Math.abs(date1.getTime() - new Date().getTime());
                                int diffInDays = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
                                int diffInmin = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60));

                                if (CurrentUser.getInstance().getmCustomer().getPlan() == 103 && diffInmin < dif) {
                                    int mdif=(int)dif-diffInmin;
                                   // findViewById(R.id.button3).setVisibility(View.GONE);
                                    //    Toast.makeText(offerActivity.this,getString(R.string.uhavetowait)+" "+mdif+" "+getString(R.string.minute),Toast.LENGTH_LONG).show();
                                    show.setText(getString(R.string.uhavetowait)+" "+mdif+" "+getString(R.string.minute));
                                    //  show.setVisibility(View.VISIBLE);
                                    timer();
                                    return;
                                }
                                if (CurrentUser.getInstance().getmCustomer().getPlan() == 102 && diffInmin < (dif * 2)) {
                                    int mdif=(int)dif*2-diffInmin;
                                   // findViewById(R.id.button3).setVisibility(View.GONE);
                                    //   Toast.makeText(offerActivity.this,getString(R.string.uhavetowait)+" "+(mdif * 2)+" "+getString(R.string.minute),Toast.LENGTH_LONG).show();
                                    show.setText(getString(R.string.uhavetowait)+" "+mdif +" "+getString(R.string.minute));
                                    //  show.setVisibility(View.VISIBLE);
                                    timer();
                                    return;
                                }
                                if (CurrentUser.getInstance().getmCustomer().getPlan() == 101 && diffInmin < (dif * 3)) {
                                    int mdif=(int)dif*3-diffInmin;
                                   // findViewById(R.id.button3).setVisibility(View.GONE);
                                    //  Toast.makeText(offerActivity.this,getString(R.string.uhavetowait)+" "+(mdif * 3)+" "+getString(R.string.minute),Toast.LENGTH_LONG).show();
                                    show.setText(getString(R.string.uhavetowait)+" "+mdif +" "+getString(R.string.minute));
                                    //   show.setVisibility(View.VISIBLE);
                                    timer();
                                    return;
                                }

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }







                    }else {
                        if(trailer.equals("trailerpro")){
                            try {
                                if(getIntent().hasExtra("dif")) {
                                    double dif = Double.parseDouble(getIntent().getStringExtra("dif"));
                                    Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(getIntent().getStringExtra("date_now"));
                                    Date date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(getIntent().getStringExtra("datereq"));
                                    //  long diffInMillies = Math.abs(date1.getTime() - new Date().getTime());
                                    int diffInDays = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
                                    int diffInmin = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60));
                                    int mdif=(int)5-diffInDays;
                                    if ( diffInDays < 5) {
                                     //   findViewById(R.id.button3).setVisibility(View.GONE);
                                        //     Toast.makeText(offerActivity.this,getString(R.string.uhavetowait)+" "+(mdif * 3)+" "+getString(R.string.minute),Toast.LENGTH_LONG).show();
                                        show.setText(getString(R.string.uhavetowait)+" "+(mdif)+" "+getString(R.string.day));
                                        //   show.setVisibility(View.VISIBLE);
                                        timer();
                                        return;
                                    }
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                       //     findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
                         //   findViewById(R.id.button3).setVisibility(View.GONE);
                           // show.setText(getString(R.string.dialog_rim_rep));
                            //  show.setVisibility(View.VISIBLE);
                            // timer();
                          //  isshowed = true;
                            showdialogAds("button");  return;
                        }
                    }}
            }else {
                showdialogAds("button");
              //  findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
               // findViewById(R.id.button3).setVisibility(View.GONE);
                return;
            }
            appApi.getServicesByUser(CurrentUser.getInstance().getmCustomer().getId()).enqueue(new Callback<List<HomeFeed>>() {
                @Override
                public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
                    if (response.isSuccessful() && response.body().size() > 0) {
                        show();
                        showBottomSheet();
//                       Intent intent = new Intent(offerActivity.this, repreqAddEditActivity.class);
//                       intent.putExtra("id",id);
//                       intent.putExtra("change","");
//                       startActivity(intent);
                    }else {
                        new android.app.AlertDialog.Builder(offerActivity.this)
                                .setTitle(R.string.app_name)
                                .setMessage(getString(R.string.udoesnthaveaservice))
                                .setPositiveButton(R.string.text_ok, (a, b) -> {
                                   // finish();
                                    a.dismiss();
                                }).show();
                    }
                    hide();}
                @Override
                public void onFailure(Call<List<HomeFeed>> call, Throwable t) {
                    swipeView.setRefreshing(false); hide();
                    new android.app.AlertDialog.Builder(offerActivity.this)
                            .setTitle(R.string.app_name)
                            .setMessage(getString(R.string.noconnection))
                            .setPositiveButton(R.string.text_ok, (a, b) -> {
                               // finish();
                                a.dismiss();
                            }).show();

                }
            });
        });

        TextView st = findViewById(R.id.st);
        cadrshow = findViewById(R.id.cardshow);
       show = findViewById(R.id.textView21);
        TextView datefin = findViewById(R.id.tdatefin);
        trailer=getIntent().getStringExtra("trailer");

        String tst="";
        if(getIntent().getStringExtra("st").equals("3")) tst=getString(R.string.non_finie);
        try {

            Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(getIntent().getStringExtra("dur"));
            //  long diffInMillies = Math.abs(date1.getTime() - new Date().getTime());
            int diffInDays = (int)( (date1.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24) );
            //   long diffInMillies = Math.abs(date1.getTime() - new Date().getTime());
            //long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            //  Toast.makeText(offerActivity.this,diffInDays+" ",Toast.LENGTH_LONG).show();
            if(diffInDays<0) {
                findViewById(R.id.button3).setVisibility(View.GONE);
                tst = getString(R.string.finish);
                st.setTextColor(Color.MAGENTA);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(getIntent().getStringExtra("st").equals("1"))
        {tst=getString(R.string.fini_suc);
        st.setTextColor(Color.GREEN);
        }
        if(getIntent().getStringExtra("st").equals("2"))
        {   tst=getString(R.string.fini_witout_suc);
            st.setTextColor(Color.RED);}

        if(getIntent().getStringExtra("st").equals("4")) tst=getString(R.string.en_cour_tratment);

        if(!getIntent().getStringExtra("st").equals("3") )findViewById(R.id.button3).setVisibility(View.GONE);



        st.setText(tst);
      //  st.setText(tst);
        datefin.setText(getIntent().getStringExtra("dur"));
        TextView username = findViewById(R.id.t_username);
        TextView categorie = findViewById(R.id.t_categorie);
        ShowMoreTextView content = findViewById(R.id.t_content);
        TextView title = findViewById(R.id.t_title);
        TextView t_city = findViewById(R.id.t_city);
        TextView txt_status = findViewById(R.id.txt_status);
        TextView txt_urgence = findViewById(R.id.txt_urgence);
        TextView edt_unity = findViewById(R.id.edt_unity);
        TextView edt_quantity = findViewById(R.id.edt_quantity);
        TextView tDateStart = findViewById(R.id.tDateStart);
        TextView msg_price = findViewById(R.id.msg_price);
        ImageView img_official_account = findViewById(R.id.img_official_account);
        //     ImageView plan_img = itemView.findViewById(R.id.plan_img);
        TextView type = findViewById(R.id.msg);
        //     ImageView plan_img = itemView.findViewById(R.id.plan_img);
        if(getIntent().getStringExtra("type").trim().isEmpty())
        {type.setVisibility(View.GONE);findViewById(R.id.button3).setVisibility(View.GONE);}
        type.setText(getIntent().getStringExtra("type"));
        title.setText(getIntent().getStringExtra("title"));
        t_city.setText(getIntent().getStringExtra("adrs"));
        username.setText(getIntent().getStringExtra("fullname"));
        msg_price.setText(getIntent().getStringExtra("price"));
        content.setText(getIntent().getStringExtra("des"));
        content.setShowingLine(4);

        if(getIntent().getIntExtra("des", 0) == 0){
            img_official_account.setVisibility(GONE);
        }else{
            img_official_account.setVisibility(View.VISIBLE);
        }

        if (getIntent().getIntExtra("status", 0) == 0){
            img_active_user.setColorFilter(ContextCompat.getColor(this, R.color.red));
            txt_active.setText(R.string.inactive);
        }else{
            img_active_user.setColorFilter(ContextCompat.getColor(this, R.color.greenSeafoam));
            txt_active.setText(R.string.active);
        }


        if (getIntent().getIntExtra("urgence", 0) == 0){
            txt_urgence.setVisibility(GONE);
            txt_status.setVisibility(GONE);
        }else{
            txt_urgence.setVisibility(View.VISIBLE);
            txt_status.setVisibility(View.VISIBLE);
        }

        if (getIntent().getStringExtra("date_start") == null){
            try {
                Date datestart = new SimpleDateFormat("yyyy-MM-dd").parse(getIntent().getStringExtra("datereq"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(datestart);
                tDateStart.setText(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " - ");
            }catch (ParseException e) {
                e.printStackTrace();
            }

        }else{
            tDateStart.setText(getIntent().getStringExtra("date_start") + " - ");
        }

        if (getIntent().getStringExtra("unity") == null){
            edt_unity.setText("Non spécifié");
        }else{
            edt_unity.setText(getIntent().getStringExtra("unity"));
        }

        edt_quantity.setText(String.valueOf(getIntent().getIntExtra("quantity", 0)));

        boolean isshowed=false;
        id=getIntent().getStringExtra("id");
        if (getIntent().hasExtra("user")){
            user=getIntent().getStringExtra("user");
            if(user.equals(CurrentUser.getInstance().getmCustomer().getId().toString())) {
                bme = true;
                findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
                findViewById(R.id.button3).setVisibility(View.GONE);
                findViewById(R.id.btn_show_nego).setVisibility(View.VISIBLE);
            }
        }

     /*   if(!CurrentUser.getInstance().isPro()){
            findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
            findViewById(R.id.button3).setVisibility(View.GONE);
        }*/
        content.addShowMoreText(getString(R.string.showmore));
        content.addShowLessText(getString(R.string.showless));
        content.setShowMoreColor(Color.RED); // or other color
        content.setShowLessTextColor(Color.RED);
        categorie.setText(getIntent().getStringExtra("datereq"));


        ImageView img = findViewById(R.id.profile_img);
        OptimTools.getPicasso(getIntent().getStringExtra("image")).into(img);


        btn_show_nego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent nego_intent = new Intent(offerActivity.this, ShowNegociationActivity.class);
                nego_intent.putExtra("offer_id", id);
                nego_intent.putExtra("user", getIntent().getStringExtra("user"));
                nego_intent.putExtra("description", getIntent().getStringExtra("des"));
                nego_intent.putExtra("date_start", getIntent().getStringExtra("datereq"));
                nego_intent.putExtra("date_end", getIntent().getStringExtra("dur"));
                nego_intent.putExtra("city", getIntent().getStringExtra("adrs"));
                nego_intent.putExtra("urgence", getIntent().getIntExtra("urgence", 0));
                nego_intent.putExtra("quantity", getIntent().getIntExtra("quantity", 0));
                nego_intent.putExtra("unity", getIntent().getStringExtra("unity"));
                nego_intent.putExtra("price", getIntent().getStringExtra("price"));
                startActivity(nego_intent);
                finish();
            }
        });



        pager = findViewById(R.id.pager);
        dotsIndicator = findViewById(R.id.dots_indicator);


       // RecyclerView recyclerView = findViewById(R.id.rvHome);

//        recyclerView.setLayoutManager(new LinearLayoutManager(offerActivity.this));
//
//
//        adapter = new offerAdapter(this);
//        recyclerView.setAdapter(adapter);
//
//        paged = OptimTools.injectPaginationHavingLayoutManager(scroll, recyclerView.getLayoutManager(), (p, t) -> {
//            getFeeds(p,srch);
//        });
        swipeView = findViewById(R.id.swipe_layout);
//        swipeView.setOnRefreshListener(() -> {
//            paged.resetState();
//            getFeeds(1,srch);
//        });
        SharedPreferences prefs = getSharedPreferences("offers",0);
        String Voffers = prefs.getString("Voffers","0");
        String Vrep = prefs.getString("Vrep","0");
        Customer me = CurrentUser.getInstance().getmCustomer();
        me.getId();
        if(!CurrentUser.getInstance().getmCustomer().getVrep().equals("1") && Vrep.equals("0") && isshowed==false)
            showdialogAds("");

//        getFeeds(1,srch);
    }

    private void showdialogAds(String from) {
        final Dialog dialog = new Dialog(offerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_rimand_rep);
        Button dialogButtonok = (Button) dialog.findViewById(R.id.b_confirmer);
        dialogButtonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("offers",0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Vrep", "1");
                editor.apply();
                Customer me = CurrentUser.getInstance().getmCustomer();
                me.getId();
                if(!from.isEmpty()) {
                    Intent intent=new Intent(offerActivity.this,rewardActivity.class);
                    startActivity(intent);
                }
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
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    private void showdialogAdsclient() {
        final Dialog dialog = new Dialog(offerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //   dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_rimand_client_ads);
        Button dialogButtonok = (Button) dialog.findViewById(R.id.b_confirmer);
        dialogButtonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

//    private void getFeeds(int page,String srch) {
//        show();
//
//        appApi.getoffer(page,id).enqueue(new Callback<List<repreq>>() {
//            @Override
//            public void onResponse(Call<List<repreq>> call, Response<List<repreq>> response) {
//
//                //   Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();
//                if (response.isSuccessful()) {
//                    //  Toast.makeText(getContext(),"su",Toast.LENGTH_LONG).show();
//                    adapter.fill(page, response.body(),bme);
//                }
//                swipeView.setRefreshing(false);
//                hide();
//            }
//
//            @Override
//            public void onFailure(Call<List<repreq>> call, Throwable t) {
//                swipeView.setRefreshing(false);
//                hide();
//            }
//        });
//    }

    @Override
    public void onMessageClick(repreq homefeed) {
        show();
        appApi.getServicesByUser(Integer.valueOf(homefeed.getUser_id())).enqueue(new Callback<List<HomeFeed>>() {
            @Override
            public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                  //  ;

                    Intent myintent = new Intent(offerActivity.this, ChatActivity.class);
                    myintent.putExtra("service", response.body().get(0));
                    myintent.putExtra("typec", "offer");
                    myintent.putExtra("offer_id", homefeed.getReq_id()+"");
                 startActivityForResult(myintent, 0);
                //    startActivity(myintent);
                }else {
                    new android.app.AlertDialog.Builder(offerActivity.this)
                            .setTitle(R.string.app_name)
                            .setMessage(getString(R.string.hedoesnthaveaservice))
                            .setPositiveButton(R.string.text_ok, (a, b) -> {
                                finish();
                                a.dismiss();
                            }).show();
                }
                swipeView.setRefreshing(false);
                hide();
            }

            @Override
            public void onFailure(Call<List<HomeFeed>> call, Throwable t) {
                swipeView.setRefreshing(false); hide();
                new android.app.AlertDialog.Builder(offerActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.noconnection))
                        .setPositiveButton(R.string.text_ok, (a, b) -> {
                            finish();
                            a.dismiss();
                        }).show();

            }
        });
      /*  Intent myintent = new Intent(offerActivity.this,ChatActivity.class);
        myintent.putExtra("notification",homefeed.getUser_id()+"");
        startActivity(myintent);*/

    }
    @Override
    public void onModifiClick(repreq homefeed) {
        Intent intent = new Intent(offerActivity.this, repreqAddEditActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("change",homefeed.getId()+"");
        intent.putExtra("dur",homefeed.getDuration());
        intent.putExtra("price",homefeed.getPrix());
        intent.putExtra("des",homefeed.getDes());
        startActivity(intent);


    }
    @Override
    public void onremoveClick(repreq homefeed) {
        AlertYesNo(R.string.confirm_delete_offer, getString(R.string.text_delete), getString(R.string.text_cancel), () -> {


            show();
            appApi.deleterepreq(homefeed.getId())
                    .enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            hide();
                            if (!response.isSuccessful() || response.body().isError()) {
                                Alert(response.body().getMessage());
                            } else {
                              //  getFeeds(1,"");
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
    public void onAcceptClick(repreq HomeadsFeed) {

    }

    @Override
    public void onDeclineClick(repreq HomeadsFeed) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showBottomSheet(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(offerActivity.this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_negociation);

        Button btn_cancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
        Button btn_add = bottomSheetDialog.findViewById(R.id.btn_add);
        EditText et_date_start = bottomSheetDialog.findViewById(R.id.et_date_start);
        EditText et_date_final = bottomSheetDialog.findViewById(R.id.et_date_final);
        Spinner et_unité = bottomSheetDialog.findViewById(R.id.et_unité);
        EditText et_quantity = bottomSheetDialog.findViewById(R.id.et_quantity);
        EditText et_price = bottomSheetDialog.findViewById(R.id.et_price);
        EditText et_description = bottomSheetDialog.findViewById(R.id.et_description);
        Spinner spinner_services = bottomSheetDialog.findViewById(R.id.spinner_services);

        ArrayAdapter<String> spinner_services_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        spinner_services_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_services.setAdapter(spinner_services_adapter);

        hide();
        appApi.availableServices(CurrentUser.getInstance().getmCustomer().getId()).enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {

                all = response.body();


                if (all.size() > 0){
                    for (Service cat : all) {
                        String cat_designation = cat.getTitle();
                        if (!cats.contains(cat_designation))
                            cats.add(cat_designation);
                    }
                }

                if (cats.size() > 0 ){
                    spinner_services_adapter.clear();
                    spinner_services_adapter.addAll(cats);
                    spinner_services_adapter.notifyDataSetChanged();

                    spinner_services.setSelection(0);
                    service_id = all.get(0).getId();
                }


//                spinner_services_adapter.addAll();
//                spinner_services_adapter.notifyDataSetChanged();
                hide();
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                t.getMessage();
                hide();
            }
        });

        et_date_start.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(offerActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> et_date_start.setText( year1 +"-" + (monthOfYear + 1) +"-" +dayOfMonth), year, month, day);
            long now = System.currentTimeMillis();
            picker.getDatePicker().setMinDate(now);
            //  picker.getDatePicker().setMaxDate(now+(1000*60*60*24*40));
            picker.show();

        });

        et_date_final.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(offerActivity.this,
                    (view, year12, monthOfYear, dayOfMonth) -> et_date_final.setText( year12 +"-" + (monthOfYear + 1) +"-" +dayOfMonth), year, month, day);
            long now = System.currentTimeMillis();
            picker.getDatePicker().setMinDate(now);
            //  picker.getDatePicker().setMaxDate(now+(1000*60*60*24*40));
            picker.show();

        });

        spinner_services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for (Service serv : all){
                    if (spinner_services_adapter.getItem(i).equals(serv.getTitle())){
                        service_id = serv.getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        unities = Arrays.asList("cm", "m", "m²", "g", "Kg", "unité");

        ArrayAdapter<String> adapter_unties = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter_unties.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_unité.setAdapter(adapter_unties);

        adapter_unties.addAll(unities);
        adapter_unties.notifyDataSetChanged();

        btn_add.setOnClickListener(v -> addNegociation(et_date_start.getText().toString(), et_date_final.getText().toString(), et_unité.getSelectedItem().toString(), et_quantity.getText().toString(), et_price.getText().toString(), et_description.getText().toString()));

        btn_cancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    private void addNegociation(String dateStart, String dateFinal, String unity, String quantity, String price, String description) {
        if (dateFinal.matches("") || dateStart.matches("") || price.trim().equals("")|| quantity.trim().equals("") || description.matches("")
                || service_id == 0) {
            Alert(R.string.message_fill_required_fields);
            return;
        }

        String change = "";

        show();
        appApi.addEditrepreq(CurrentUser.getInstance().getmCustomer().getId()+"",id,description,dateFinal,price,change,service_id,"", dateStart, unity, Integer.parseInt(quantity))
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        hide();
                        if (!response.isSuccessful() || response.body().isError()) {
                            //Alert(response.body().getMessage());
                            Alert("there is a problem");
                        } else {
                            Alert(response.body().getMessage());
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        hide();
                        Alert(t.getMessage());

                    }
                });

    }

    private void getAvailableServices(){
//        show();
//        appApi.availableServices(CurrentUser.getInstance().getmCustomer().getId()).enqueue(new Callback<ApiResponse>() {
//            @Override
//            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse> call, Throwable t) {
//
//            }
//        });
    }
}
