package com.optim.bassit.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseFragment;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.ApiResponse;
import com.optim.bassit.ui.dialogs.ChooseDialogFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProFragment extends BaseFragment implements ChooseDialogFragment.doRefreshListener {

    @Inject
    AppApi appapi;

    @Inject
    AppApi appApi;

    public ProFragment() {
        // Required empty public constructor
    }

    public static ProFragment newInstance() {
        ProFragment fragment = new ProFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_pro, container, false);

        ButterKnife.bind(this, vw);


        return vw;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);

    }

    @OnClick(R.id.demandepro)
    public void demandePro() {

//        FragmentManager fm = getChildFragmentManager();
//        ChooseDialogFragment editNameDialogFragment = ChooseDialogFragment.newInstance(0, this,getContext());
//        editNameDialogFragment.show(fm, "fragment_edit_name");

        appApi.doPro().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {

                        try{
                            SharedPreferences prefs = getActivity().getSharedPreferences("bassitpro",0);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("choose", "false");
                            editor.apply();}
                        catch (Exception e){}

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, new CompteFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();

                    }



            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });



    }

    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public void doRefresh() {
        Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        getActivity().finish();
    }
}
