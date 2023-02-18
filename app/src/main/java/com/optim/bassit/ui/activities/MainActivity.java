package com.optim.bassit.ui.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.Customer;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.models.SousCategorie;
import com.optim.bassit.models.Tache;
import com.optim.bassit.ui.dialogs.ChooseDialogFragment;
import com.optim.bassit.ui.fragments.AdsFragment;
import com.optim.bassit.ui.fragments.CompteFragment;
import com.optim.bassit.ui.fragments.DemandsListFragment;
import com.optim.bassit.ui.fragments.HomeFragment;
import com.optim.bassit.ui.fragments.MyProfileFragment;
import com.optim.bassit.ui.fragments.ProFragment;
import com.optim.bassit.ui.fragments.TachesFragment;
import com.optim.bassit.utils.LocaleHelper;
import com.optim.bassit.utils.MapHelper;
import com.optim.bassit.utils.OptimTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    /*@Inject
    AppApi appApi;*/

    Dialog myDialog;
    @BindView(R.id.navigation)
    BottomNavigationView bottomNav;

    public static  BottomNavigationView bottomNav2;
    @BindView(R.id.t_title)
    TextView Title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    HomeFragment home_fragment;
    DemandsListFragment taches_fragment;
    MyProfileFragment profile_fragment;
    ProFragment pro_fragment;
    CompteFragment compte_fragment;
    AdsFragment ads_fragment;
 //  public static LinearLayout linearLayout;
    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
      /*  BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.notification_badge, bottomNavigationView, false);*/
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(itemId);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        View badge = LayoutInflater.from(context).inflate(R.layout.notification_badge, bottomNavigationView, false);
        //   View badge = LayoutInflater.from(this).inflate(R.layout.notification_badge, itemView, true);



        TextView text = badge.findViewById(R.id.notificationstext);
        text.setText(value);
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, int itemId) {
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(itemId);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

    public void setnumchat(String num) {
        SharedPreferences settings = getSharedPreferences("compte", 0);
        SharedPreferences.Editor editor = settings.edit();
        //  int s=Integer.valueOf(num)+Integer.valueOf(getnumchat());
        editor.putString("numchat",num);
        editor.apply();
    }

    public  String getnumchat() {
        SharedPreferences settings = getSharedPreferences("compte", 0);
        return settings.getString("numchat","0");
    }
     Handler mHandler=new Handler();

     Boolean run=true;
    public static Boolean runing=true;
    public  void timer2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {

                                if(!getnumchat().equals("0"))
                                    showBadge(MainActivity.this,bottomNav,2,getnumchat());
                            }
                        });
                        Thread.sleep(3000);
                    } catch(Exception e){
                    }}
            }

        }).start();
    }
    public static Context context;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //run=false;
        runing=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  run=false;
        runing=false;
    }

    @Override
    protected void onStart() {
        super.onStart();
      //  run=true;
        runing=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    //   timer2();
        runing=true;
        status(1);

    }
   // Boolean runb=true;
    public  void timer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        Thread.sleep(4000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                              //  linearLayout.setVisibility(View.GONE);
                                //runb=false;
                            }
                        });

                    } catch(Exception e){
                    }
            }

        }).start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        status(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        } catch (Exception ex) {
            setContentView(R.layout.activity_main);
            //super.onCreate(savedInstanceState);
        }


        ButterKnife.bind(this);
        App.getApp(getApplication()).getComponent().inject(this);

        SplashActivity.doFCM(appApi, this);
        myDialog = new Dialog(this);
     //   linearLayout=(LinearLayout)  findViewById(R.id.remb);
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        context=this;

        if(!getnumchat().equals("0"))
            showBadge(this,bottomNav,2,getnumchat());
        timer2();
        Customer me = CurrentUser.getInstance().getmCustomer();

        status(1);
        getSousCategories();
        getCats();

        int from_service = getIntent().getIntExtra("from_service", 0);

        if (from_service != 0){
            toolbar.setVisibility(View.GONE);
            setStatusBarColor(R.color.white, false);
            Title.setText("Bassit Pro");
            if (!CurrentUser.getInstance().isPro()) {
                if (pro_fragment == null)
                    pro_fragment = new ProFragment();
                loadFragment(pro_fragment);

            }else{
                if (compte_fragment == null)
                    compte_fragment = new CompteFragment();
                loadFragment(compte_fragment);
            }



        }

        if (home_fragment == null)
            home_fragment = new HomeFragment();
        mGotPositionListener = home_fragment;
        loadFragment(home_fragment);
        bottomNav.setSelectedItemId(R.id.navigation_home);

        toolbar.setVisibility(View.GONE);
        //setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_information, R.id.nav_interest, R.id.nav_language)
                .setDrawerLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        Customer cus2 = CurrentUser.getInstance().getmCustomer();



        if (cus2 == null) {
            AlertWait("Veuillez vous connecter d'abord", () -> {
                finish();
            });
        }
        if (cus2.getTherole() == 0) {
            FragmentManager fm = getSupportFragmentManager();
            ChooseDialogFragment editNameDialogFragment = ChooseDialogFragment.newInstance(0, null,MainActivity.this);
            editNameDialogFragment.show(fm, "fragment_edit_name");
        }

        Menu menu = navigationView.getMenu();

        if (!CurrentUser.getInstance().isPro()) {
//            menu.findItem(R.id.nav_stats).setVisible(false);
//            menu.findItem(R.id.nav_finance).setVisible(false);

        }else   menu.findItem(R.id.nav_share).setTitle(getString(R.string.invitefiendandbenfit));
        HandleMenu(navigationView);

        Thread thread = new Thread(() -> {
            MapHelper.getInstance().getGPS(MainActivity.this, (gps) -> {

                if (gps == null || (gps.latitude == 0 && gps.longitude == 0)) {
                    Alert(R.string.impossible_de_determiner_votrez_position_ressayer);
                    return;
                }
                writeDownTheAddress(gps);

            });
        });

        appApi.getOldPosition().enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer cus = response.body();
                    if (cus.getCountry() == null || cus.getCountry().matches("")) {


                        thread.start();
                        return;
                    }
                    CurrentUser.getInstance().setCountry(cus.getCountry());
                    CurrentUser.getInstance().setWilaya(cus.getWilaya());
                    CurrentUser.getInstance().setCity(cus.getCity());
                    CurrentUser.getInstance().setLat(cus.getMylat());
                    CurrentUser.getInstance().setLon(cus.getMylon());
                    mGotPositionListener.onGotPosition();
                } else {

                    AlertWait(R.string.veuillez_autoriser_bassit_a_gps, () -> restartApplication());
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {

            }
        });

        if (getIntent().hasExtra("notification")) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("notification", getIntent().getStringExtra("notification"));
           startActivity(intent);

        }else  if (getIntent().hasExtra("service")) {
            if(getIntent().getStringExtra("service").equals("offer")){
                if (ads_fragment == null)
                    ads_fragment = new AdsFragment();
                loadFragment(ads_fragment);
                Title.setText("Publicité");
                bottomNav.setSelectedItemId(R.id.navigation_offer);
            }else if (getIntent().getStringExtra("service").equals("demand service")){
                if (taches_fragment == null)
                    taches_fragment = new DemandsListFragment();
                loadFragment(taches_fragment);
                bottomNav.setSelectedItemId(R.id.navigation_tache);
            }else if (getIntent().getStringExtra("service").equals("check")){
                if (taches_fragment == null)
                    taches_fragment = new DemandsListFragment();
                loadFragment(taches_fragment);
                bottomNav.setSelectedItemId(R.id.navigation_tache);
            }


        }
else
        if (getIntent().hasExtra("link")) {


            String mylink = getIntent().getStringExtra("link");

            appApi.activateLink(mylink).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isError())
                            Alert(response.body().getMessage());
                        else {
                            appApi.getOneTaches(response.body().getMessage()).enqueue(new Callback<Tache>() {
                                @Override
                                public void onResponse(Call<Tache> call, Response<Tache> rr) {
                                    if (rr.isSuccessful()) {
                                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                                        intent.putExtra("tache", rr.body());
                                        startActivityForResult(intent, 0);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Tache> call, Throwable t) {

                                }
                            });
                        }
                    } else
                        Alert(response.errorBody().toString());
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Alert(t.getMessage());
                }
            });
        }else if (getIntent().hasExtra("frgToLoad")){
            Boolean intentFragment = getIntent().getExtras().getBoolean("frgToLoad", false);
            if (intentFragment){
                if (compte_fragment == null)
                    compte_fragment = new CompteFragment();
                loadFragment(compte_fragment);
            }
        }
      /* if (CurrentUser.getInstance()!=null && CurrentUser.getInstance().isPro()){
            linearLayout=(LinearLayout)  findViewById(R.id.remb);
            linearLayout.setVisibility(View.GONE);
            if(CurrentUser.getInstance().getmCustomer().getPhone()==null || CurrentUser.getInstance().getmCustomer().getPhone().isEmpty())
            {
                linearLayout.setVisibility(View.VISIBLE);
                timer();}
        }
       */
        //  if (CurrentUser.getInstance().isPro()) {
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_profile = new Intent(MainActivity.this, EditInformationActivity.class);
                startActivity(intent_profile);
            }
        });
        SharedPreferences prefs = getSharedPreferences("bassitpro",0);
        String ch = prefs.getString("choose","true");
         if(ch.equals("false") && cus2.getTherole() == 2){
             if (Double.valueOf(CurrentUser.getInstance().getmCustomer().getLat()) == 0 && Double.valueOf(CurrentUser.getInstance().getmCustomer().getLon()) == 0) {
              /*   new android.app.AlertDialog.Builder(this)
                         .setTitle(R.string.app_name)
                         .setMessage(R.string.message_personal_information_required)
                         .setPositiveButton(R.string.text_ok, (d, i) -> {
                             d.dismiss();*/
                             Intent myintent = new Intent(this, UpdateMyProfileActivity.class);
                             startActivityForResult(myintent, 10008);
                       //  }).show();


             }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("choose", "true");
        editor.apply();

        //    return;
         }
    try {
         FirebaseApp.initializeApp(this);

        topics = getSharedPreferences("pushMessagingSubscription", 0);
        unsubscribeAllTopics();
     //   subscribeTopic("taster");
        if (CurrentUser.getInstance().isPro()) {
            subscribeTopic("Pro");
        }
        if (cus2.getTherole() == 2) {
         //   getServices();
            subscribeTopic("ProMax");
        }
        if (cus2.getTherole() == 33) {
            subscribeTopic("ProMin");
        }
        if (cus2.getTherole() == 99) {
            subscribeTopic("Client");
        } }catch (Exception e){

    } if (cus2.getTherole() == 2) {

            getServices();

        }
        bottomNav2= (BottomNavigationView) findViewById((R.id.navigation));
       // MobileAds.initialize(this, "ca-app-pub-5903730561297285~3743395548");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void showdialog() {
        String date="2020-05-22 23:21:29.766";
        SharedPreferences prefs = getSharedPreferences("bassitpro",0);
        date = prefs.getString("dates","2020-05-22 22:21:29.766");
        if(date.equals("2020-05-22 22:21:29.766")){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("dates", getCurrentTimeStamp());
            editor.apply();
            return;
        }
        long diff = new Date().getTime() - strtodate(date).getTime();

        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        //  System.out.println("difference between days: " + diffDays);

        if(diffDays>2){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("dates", getCurrentTimeStamp());
            editor.apply();


        }else return;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_bassitpro);
        Button dialogButtonok = (Button) dialog.findViewById(R.id.b_confirmer);
        dialogButtonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,rewardActivity.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.b_confirmer2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });
        Button info = (Button) dialog.findViewById(R.id.b_confirmer3);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bassit-app.com/inforeward"));
                startActivity(browserIntent3);
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    private void getServices() {
        int user_id =0; //getIntent() != null ? getIntent().getInt("user_id", 0) : 0;
        appApi.getServicesByUser(user_id == 0 ? CurrentUser.getInstance().getmCustomer().getId() : user_id).enqueue(new Callback<List<HomeFeed>>() {
            @Override
            public void onResponse(Call<List<HomeFeed>> call, Response<List<HomeFeed>> response) {

                if (response.isSuccessful()) {
                    if ( response.body().size() == 0) {
                        //System.out.println(getCurrentTimeStamp());
                        String date="2020-05-22 23:21:29.766";
                        SharedPreferences prefs = getSharedPreferences("service",0);
                        date = prefs.getString("dates","2020-05-22 22:21:29.766");
                        if(date.equals("2020-05-22 22:21:29.766")){
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("dates", getCurrentTimeStamp());
                            editor.apply();
                            return;
                        }
                        long diff = new Date().getTime() - strtodate(date).getTime();

                        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                      //  System.out.println("difference between days: " + diffDays);

                        if(diffDays>0){
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("dates", getCurrentTimeStamp());
                            editor.apply();
                            final Dialog dialog = new Dialog(MainActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.dialog_rimand_service);


                            Button dialogButtonc = (Button) dialog.findViewById(R.id.b_cancel);
                            dialogButtonc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            Button dialogButtonok = (Button) dialog.findViewById(R.id.b_confirmer);
                            dialogButtonok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onAddService();
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
							Window window = dialog.getWindow();
                            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							
							
							}
                    }else {
                        if(CurrentUser.getInstance().getmCustomer().getPlan()==101 || CurrentUser.getInstance().getmCustomer().getPlan()==102 ||
                                CurrentUser.getInstance().getmCustomer().getPlan()==103 ||CurrentUser.getInstance().getmCustomer().getPlan()==104){

                            }else showdialog();
                        if(CurrentUser.getInstance().getmCustomer().getTherole() == 2){
                            List<HomeFeed> services=response.body();
                            for(int i=0;i<services.size();i++){
                                //System.out.println(services.get(i).getTags());
                             //   services.getType_en()
                                try {
                               subscribeTopic(services.get(i).getCategorien());
                                }catch (Exception e){

                                }

                            }}

                    }


                    // adapter.fill(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<HomeFeed>> call, Throwable t) {

            }
        });
    }

    private  SharedPreferences topics;

    public  void subscribeTopic(String topic) {
        if (topics.contains(topic)) return;
        topics.edit().putBoolean(topic, true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "succes";
                        System.out.println(msg);
                        if (!task.isSuccessful()) {
                            msg = "failed";
                            System.out.println(msg);
                        }
                    }
                });
    }


    public  void unsubscribeAllTopics() {
        for (String topic : topics.getAll().keySet()) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        }
        topics.edit().clear().apply();
        // FirebaseInstanceId.getInstance().deleteInstanceId();
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    public  Date strtodate(String date){
        Date date1= null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public void onAddService() {
        if (Double.valueOf(CurrentUser.getInstance().getmCustomer().getLat()) == 0 && Double.valueOf(CurrentUser.getInstance().getmCustomer().getLon()) == 0) {
            new android.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.message_personal_information_required)
                    .setPositiveButton(R.string.text_ok, (d, i) -> {
                        d.dismiss();
                        Intent myintent = new Intent(this, UpdateMyProfileActivity.class);
                        startActivityForResult(myintent, 10008);
                    }).show();


        } else {
            Intent myintent = new Intent(this, ServiceAddEditActivity.class);
            myintent.putExtra("isnew", true);
            startActivityForResult(myintent, 10007);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MapHelper.LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MapHelper.getInstance().getGPS(MainActivity.this, (gps) -> {

                    if (gps == null || (gps.latitude == 0 && gps.longitude == 0)) {
                        Alert(R.string.impossible_de_determiner_votrez_position_ressayer);
                        return;
                    }
                    writeDownTheAddress(gps);

                });
            }
        }

    }

    private void writeDownTheAddress(LatLng gps) {
        Address addr = MapHelper.getInstance().getAddressFromLocation(MainActivity.this, gps);


        if (addr == null) {


        } else {

            MainActivity.this.runOnUiThread(() -> {

                CurrentUser.getInstance().setCountry(addr.getCountryName());
                CurrentUser.getInstance().setWilaya(addr.getAdminArea());
                CurrentUser.getInstance().setCity(addr.getLocality());
                CurrentUser.getInstance().setLat(OptimTools.toApiString(gps.latitude));
                CurrentUser.getInstance().setLon(OptimTools.toApiString(gps.longitude));

                Customer cus2 = new Customer();
                cus2.setCountry(addr.getCountryName());
                cus2.setWilaya(addr.getAdminArea());
                cus2.setCity(addr.getLocality());
                cus2.setMylat(OptimTools.toApiString(gps.latitude));
                cus2.setMylon(OptimTools.toApiString(gps.longitude));

                appApi.updatePosition(cus2).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });
                if (mGotPositionListener != null)
                    mGotPositionListener.onGotPosition();
            });
        }
    }

    public void restartApplication() {

        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }


    gotPositionListener mGotPositionListener;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            new android.app.AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.quitter_bassit)
                    .setPositiveButton(R.string.text_ok, (d, z) -> {
                        Process.killProcess(Process.myPid());
                    }).setNegativeButton(R.string.text_cancel, null).show();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            setStatusBarColor(R.color.colorPrimary, false);


            toolbar.setVisibility(View.GONE);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setVisibility(View.GONE);
                    if (home_fragment == null)
                        home_fragment = new HomeFragment();
                    mGotPositionListener = home_fragment;
                    loadFragment(home_fragment);

                    return true;
                case R.id.navigation_tache:
                  //  setStatusBarColor(R.color.activity_bg, true);
                    if (taches_fragment == null)
                        taches_fragment = new DemandsListFragment();
                 /*   if(!getnumchat().equals("0")) {
                        setnumchat("0");
                        removeBadge(bottomNav, 2);
                        ShortcutBadger.removeCount(getApplicationContext());
                    }*/
                    loadFragment(taches_fragment);
                    Title.setText("Tâches");
                    return true;
                case R.id.navigation_offer:
                    if (ads_fragment == null)
                        ads_fragment = new AdsFragment();
                    loadFragment(ads_fragment);
                    Title.setText("Publicité");
                    return true;
                case R.id.navigation_pro:

                    toolbar.setVisibility(View.GONE);
                    setStatusBarColor(R.color.white, false);
                    Title.setText("Bassit Pro");
                    if (!CurrentUser.getInstance().isPro()) {
                        if (pro_fragment == null)
                            pro_fragment = new ProFragment();
                        loadFragment(pro_fragment);

                        return true;
                    }
                    if (compte_fragment == null)
                        compte_fragment = new CompteFragment();
                    loadFragment(compte_fragment);

                    return true;
                case R.id.navigation_profile:
                    if (profile_fragment == null)
                        profile_fragment = new MyProfileFragment();
                    loadFragment(profile_fragment);
                    toolbar.setVisibility(View.GONE);
                    return true;

            }

            return false;
        }
    };

    public void loadFragment(Fragment fragment) {

        if (getIntent().hasExtra("service")){
            if (getIntent().getStringExtra("service").equals("check")){
                Bundle bundle = new Bundle();
                bundle.putString("check","true");
                bundle.putString("id",getIntent().getStringExtra("id"));
                bundle.putString("date",getIntent().getStringExtra("date"));
                fragment.setArguments(bundle);
            }
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @OnClick(R.id.b_menu)
    public void menuClick() {
        drawer.openDrawer(GravityCompat.START);
    }

    @Inject
    AppApi appApi;

    private void getCats() {
        appApi.getCats().enqueue(new Callback<List<Categorie>>() {
            @Override
            public void onResponse(Call<List<Categorie>> call, Response<List<Categorie>> response) {
                if (response.isSuccessful()) {
                    AppData.getInstance().setCategories(response.body());
                    if (home_fragment != null)
                        home_fragment.fillCats();
                }
            }

            @Override
            public void onFailure(Call<List<Categorie>> call, Throwable t) {

            }
        });
    }

    private void getSousCategories() {
        appApi.getSousCategories().enqueue(new Callback<List<SousCategorie>>() {
            @Override
            public void onResponse(Call<List<SousCategorie>> call, Response<List<SousCategorie>> response) {
                if (response.isSuccessful()) {
                    AppData.getInstance().setSousCategories(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<SousCategorie>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10008) {
            Intent myintent = new Intent(MainActivity.this, ServiceAddEditActivity.class);
            myintent.putExtra("isnew", true);
            startActivityForResult(myintent, 10007);
        }
    }

    private void HandleMenu(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            switch (id) {
                case R.id.nav_information:
                    Intent intent_profile = new Intent(MainActivity.this, EditInformationActivity.class);
                    startActivity(intent_profile);
                    break;
                case R.id.nav_partager_gagner:
                    Intent intent_solde = new Intent(MainActivity.this, ConsommationActivity.class);
                    intent_solde.putExtra("consommation_type", 101);
                    startActivityForResult(intent_solde, 10001);
                    break;
                case R.id.nav_commission:
                    Intent intent_commission = new Intent(MainActivity.this, CommissionActivity.class);
                    startActivity(intent_commission);
                    break;
                case R.id.nav_interest:
                    Intent intent_interest = new Intent(MainActivity.this, InterestActivity.class);
                    startActivity(intent_interest);
                    break;
                case R.id.nav_language:
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getString(R.string.text_choose_language))
                            .setItems(getResources().getStringArray(R.array.languages),
                                    (dialogInterface, i) -> {
                                        String lng = "fr";
                                        switch (i) {
                                            case 0:
                                                lng = "ar";
                                                break;
                                            case 1:
                                                lng = "fr";
                                                break;
                                            case 2:
                                                lng = "en";
                                                break;
                                        }
                                        LocaleHelper.setLocale(MainActivity.this.getApplicationContext(), lng);
                                        restartApplication();
                                    })
                            .create()
                            .show();
                    break;
                case R.id.nav_email:
                    Intent intent3 = new Intent(Intent.ACTION_SENDTO);
                    intent3.setData(Uri.parse("mailto:"));
                    intent3.putExtra(Intent.EXTRA_EMAIL, new String[]{MainActivity.this.getString(R.string.company_email)});
                    MainActivity.this.startActivity(intent3);
                    break;
                case R.id.nav_call:
                    Uri u = Uri.parse("tel:" + MainActivity.this.getString(R.string.company_phone));
                    Intent intent4 = new Intent(Intent.ACTION_DIAL, u);
                    MainActivity.this.startActivity(intent4);
                    break;
                case R.id.nav_facebook:


                    try {
                        MainActivity.this.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/BassitApp")));
                    } catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/BassitApp")));
                    }


                    break;
                case R.id.nav_insta:
                    Uri uri = Uri.parse("https://www.instagram.com/bassitapp");
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/bassitapp")));
                    }

                    break;
                case R.id.nav_share:

                    if (!CurrentUser.getInstance().isPro()){
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.partagebassit));
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.msgprtgbassit)+" https://bassit-app.com/download");
                        startActivity(Intent.createChooser(sharingIntent, getString(R.string.partager_sur)));
                    }else{
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle(getString(R.string.partagebassit));
                        builder1.setMessage(getString(R.string.inviteufriend));
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                getString(R.string.oui),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                        sharingIntent.setType("text/plain");
                                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.partagebassit));
                                        sharingIntent.putExtra(Intent.EXTRA_TEXT, CurrentUser.getInstance().getmCustomer().getFullName()+"\n "+
                                                getString(R.string.invitemsgA)+"\n"+
                                                getString(R.string.invitemsgB)+" "+getmyid()+" "+getString(R.string.invitemsgC)
                                                +"\n "+getString(R.string.lien_don)+" https://bassit-app.com/download");
                                        startActivity(Intent.createChooser(sharingIntent, getString(R.string.partager_sur)));


                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                getString(R.string.non),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                    }

                    break;
                case R.id.nav_rate:
                    final String appPackageName = MainActivity.this.getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    break;
                case R.id.nav_about:
                    Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bassit-app.com/about"));
                    startActivity(browserIntent3);
                    break;
                case R.id.nav_conditions:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bassit-app.com/conditions"));
                    startActivity(browserIntent);
                    break;
                case R.id.nav_privacy:
                    Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bassit-app.com/privacy"));
                    startActivity(browserIntent2);
                    break;
                case R.id.nav_logout:
                    SplashActivity.doUnFCM(appApi, this);

                    getSharedPreferences("_", MODE_PRIVATE).edit().putString("apitoken", "").apply();
                    getSharedPreferences("forgetP",0).edit().putString("dates","2020-05-22 22:21:29.766").apply();
                    getSharedPreferences("service",0).edit().putString("dates","2020-05-22 22:21:29.766").apply();
                    CurrentUser.getInstance().logout();
                    OptimTools.ClearPicassoCache(this);

                    Intent intent_logout = new Intent(MainActivity.this, SplashActivity.class);
                    startActivity(intent_logout);
                    finish();
                    break;
                default:
                    return true;
            }
            drawer.closeDrawer(GravityCompat.START);
            int size = navigationView.getMenu().size();
            for (int i = 0; i < size; i++) {
                navigationView.getMenu().getItem(i).setCheckable(false);
            }

            return true;

        });
    }

    private String getmyid() {
        Customer me = CurrentUser.getInstance().getmCustomer();
        me.getId();
        String num=Integer.toHexString(me.getId());
        num=num.toUpperCase();
        for(int i=0;i<6;i++)
            if(num.length()<6)num="0"+num;
        return num;
    }

    public interface gotPositionListener {
        public void onGotPosition();
    }

    public void status(int status){

        Customer customer = new Customer();

        customer.setStatus(status);

        appApi.updateStatus(customer).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

}
