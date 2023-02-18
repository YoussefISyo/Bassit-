package com.optim.bassit.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.optim.bassit.App;
import com.optim.bassit.MyFirebaseNotification;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.models.Tache;
import com.optim.bassit.ui.activities.ChatActivity;
import com.optim.bassit.ui.activities.MainActivity;
import com.optim.bassit.ui.activities.myofferActivity;
import com.optim.bassit.ui.activities.rewardActivity;
import com.optim.bassit.ui.adapters.TachesAdapter;
import com.optim.bassit.ui.dialogs.CloturerDialogFragment;
import com.optim.bassit.ui.dialogs.EvaluationDialogFragment;
import com.optim.bassit.utils.OptimTools;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.optim.bassit.ui.activities.MainActivity.context;
import static com.optim.bassit.ui.activities.MainActivity.showBadge;

public class TachesFragment extends Fragment implements TachesAdapter.ItemClickListener, MyFirebaseNotification.NotificationListener {
    SwipeRefreshLayout swipeView;
    TachesAdapter adapter;

    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;

    String date, id;


    @Inject
    AppApi appApi;

    public TachesFragment() {
    }

    public static TachesFragment newInstance() {
        TachesFragment fragment = new TachesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyFirebaseNotification.registerNotification(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_taches, container, false);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this, vw);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.text_tache_active)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.text_clotures)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Bundle bundle = this.getArguments();

        if (bundle != null){
            date = bundle.getString("date");
            id = bundle.getString("id");

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
            final View customLayout = getLayoutInflater().inflate(R.layout.dialog_extend_time, null);
            builder.setView(customLayout);
            
            androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.show();

            customLayout.findViewById(R.id.b_cancel).setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            customLayout.findViewById(R.id.b_confirmer).setOnClickListener(v1 -> {
                updateDemand();
                dialog.dismiss();
            });
        }


        RecyclerView recyclerView = vw.findViewById(R.id.rvTaches);
        recyclerView.setLayoutManager(new LinearLayoutManager(vw.getContext()));
        adapter = new TachesAdapter(this,getContext());
        recyclerView.setAdapter(adapter);
        getChatList();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //pager.setCurrentItem(tab.getPosition());
                filterTab();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(0);
        swipeView = vw.findViewById(R.id.swipe_layout);
        swipeView.setOnRefreshListener(() -> getChatList());
       /* try {
            showBadge(context,MainActivity.bottomNav2,2,"2");
        }catch (Exception e){}*/
        return vw;
    }

    private void updateDemand() {
        appApi.updateDateDemandService(date, Integer.parseInt(id)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    if (!response.body().isError()){
                        Toast.makeText(getActivity(), "La date de livraison a été modifié", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    List<Tache> local_taches = new ArrayList<>();

    private void getChatList() {
        if (swipeView != null)
            swipeView.setRefreshing(true);

        local_taches = new ArrayList<>();
        appApi.getTaches().enqueue(new Callback<List<Tache>>() {
            @Override
            public void onResponse(Call<List<Tache>> call, Response<List<Tache>> response) {
                if (response.isSuccessful()) {
                    local_taches = response.body();
                    filterTab();
                    for (int i = local_taches.size() - 1 ; i >= 0 ; i--) {
                        Tache tache=local_taches.get(i);
                        int iclient;
                        if (CurrentUser.getInstance().getmCustomer().getId() == tache.getClient_id())
                            iclient = 1;
                        else
                            iclient = 0;

                        if(tache.getStatus() == 0){
                        if (iclient == 0 ){

                            if(!tache.getTypec().equals("offer")){
                                showdialog(tache.getTypec(),tache,tache.getOffer_id());
                                break;
                            }
                        }
                        else{

                            if(tache.getTypec().equals("offer")){
                                showdialog(tache.getTypec(),tache,tache.getOffer_id());
                                break;
                            }
                        }
                        }

                    }
                } else {
                    String s = null;
                    try {
                        s = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    OptimTools.Alert(getContext(), s);

                }
                swipeView.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Tache>> call, Throwable t) {
                swipeView.setRefreshing(false);
            }
        });

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

    private void showdialog(String typec,Tache tache,String offer_id) {
      String date="2020-05-22 23:21:29.766";
        SharedPreferences prefs = getContext().getSharedPreferences("taches",0);
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
        al=  new AlertDialog.Builder(getContext())
                .setTitle(R.string.app_name)
                .setMessage(getContext().getString(R.string.dialogchattoclose))
                .setPositiveButton(getContext().getString(R.string.finishnow), (a, b) -> {

               //     opendialog(typec, id, offer_id);
                    Intent intent = new Intent(this.getContext(), ChatActivity.class);
                    intent.putExtra("typec", typec);
                    intent.putExtra("offer_id", offer_id);
                    intent.putExtra("action", "close");
                    intent.putExtra("tache", tache);
                    startActivityForResult(intent, 0);

                    al.dismiss();
                })
                .setNegativeButton(getContext().getString(R.string.later), null).show();
    }

    void  opendialog(String typec,int id,String offer_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                    if(typec.equals("offer")){
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        EvaluationDialogFragment editNameDialogFragment = EvaluationDialogFragment.newInstance(id, "offer", offer_id + "", "");
                        editNameDialogFragment.show(fm, "fragment_cloturer");
                    }else{
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        CloturerDialogFragment editNameDialogFragment = CloturerDialogFragment.newInstance(id,"","","");
                        editNameDialogFragment.show(fm, "fragment_cloturer");
                    }
                }else {
                    // user clicked OK
                    appApi.failTache(id).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                            if (response.isSuccessful()) {
                                if (response.body().isError()) {
                               //     Alert(response.body().getMessage());
                                } else {
                                     lc= new AlertDialog.Builder(getContext())
                                            .setTitle(R.string.app_name)
                                            .setMessage(R.string.text_tache_cloture)
                                            .setPositiveButton(R.string.text_ok, (dialogInterface, i) -> {
                                                lc.dismiss();


                                            }).show();

                                }
                            } else {
                                //Alert(response.errorBody().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                           // Alert(t.getMessage());
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

    AlertDialog lc;
    AlertDialog  al;

    private void filterTab() {
        int pos = tabLayout.getSelectedTabPosition();
        if (pos < 0)
            return;
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        List<Tache> custom_taches = new ArrayList<>();
        switch (pos) {
            case 0:
                for (Tache tache : local_taches) {
                    if (tache.getStatus() == 0)
                        custom_taches.add(tache);

                }
                break;

            case 1:
                for (Tache tache : local_taches) {
                    if (tache.getStatus() != 0)
                        custom_taches.add(tache);
                }
                break;
        }

        adapter.fill(custom_taches);
    }

    @Override
    public void onItemClick(Tache tache) {

        Intent intent = new Intent(this.getContext(), ChatActivity.class);
        intent.putExtra("typec", tache.getTypec());
        intent.putExtra("offer_id", tache.getOffer_id());
        intent.putExtra("tache", tache);
        startActivityForResult(intent, 0);
    }

    public void setnumchat(String num) {
        SharedPreferences settings = getContext().getSharedPreferences("compte", 0);
        SharedPreferences.Editor editor = settings.edit();
        int s=0;
        if(!getnumchat().equals("0")) s=Integer.valueOf(getnumchat())-Integer.valueOf(num);
        editor.putString("numchat",s+"");
        editor.apply();
    }

    public String getnumchat() {
        SharedPreferences settings = getContext().getSharedPreferences("compte", 0);
        return settings.getString("numchat","0");
    }
    @Override
    public void doRefreshOnNotificationThread(Integer him, Integer badge) {
        if (badge == 1)
            getChatList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10077) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getFragmentManager().beginTransaction().detach(this).commitNow();
                getFragmentManager().beginTransaction().attach(this).commitNow();
            } else {
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }
        } else if (resultCode == 10033 || requestCode == 0) {
            getChatList();
        }
    }
}
