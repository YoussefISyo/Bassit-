package com.optim.bassit.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.models.Ads;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.ui.activities.ProfileActivity;
import com.optim.bassit.ui.activities.ServiceActivity;
import com.optim.bassit.utils.OptimTools;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeImageFragment extends Fragment {


    public Ads getAds() {
        return ads;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }

    Ads ads;

    public static HomeImageFragment newInstance(Ads ads) {
        HomeImageFragment fragment = new HomeImageFragment();
        fragment.setAds(ads);
        return fragment;
    }

    public HomeImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getApp(getActivity().getApplication()).getComponent().inject(this);
    }

    @Inject
    AppApi appApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_home_image, container, false);
        ButterKnife.bind(this, vw);
        String image = ads.getFullImage();
        OptimTools.getPicasso(image).into((ImageView) vw.findViewById(R.id.img));

        vw.setOnClickListener((a) -> {
            try {
                switch (ads.getType()) {
                    case 0:


                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ads.getTarget()));
                        startActivity(browserIntent);

                        break;
                    case 1:
                        Integer u_id = Integer.valueOf(ads.getTarget());
                        Intent myintent = new Intent(getActivity(), ProfileActivity.class);
                        myintent.putExtra("user_id", u_id);
                        startActivity(myintent);
                        break;
                    case 2:
                        Integer s_id = Integer.valueOf(ads.getTarget());
                        appApi.getOneService(s_id).enqueue(new Callback<HomeFeed>() {
                            @Override
                            public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
                                if (response.isSuccessful()) {
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
        });
        return vw;
    }
}
