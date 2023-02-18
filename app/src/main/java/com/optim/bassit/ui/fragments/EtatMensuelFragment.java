package com.optim.bassit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.Dette;
import com.optim.bassit.ui.activities.StatistiquesActivity;
import com.optim.bassit.ui.adapters.DetteAdapter;
import com.optim.bassit.ui.adapters.EtatMensuelPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EtatMensuelFragment extends Fragment implements DetteAdapter.ItemClickListener, StatistiquesActivity.FilterListener {


    @Inject
    AppApi appApi;

    public EtatMensuelFragment() {
    }


    public static EtatMensuelFragment newInstance() {
        EtatMensuelFragment fragment = new EtatMensuelFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    EtatMensuelPagerAdapter adapterpager;
    ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etat_mensuel, container, false);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this, view);

        adapterpager = new EtatMensuelPagerAdapter
                (getChildFragmentManager());
        pager = view.findViewById(R.id.pager);
        pager.setAdapter(adapterpager);

        getEtatMensuel();

        DotsIndicator dotsIndicator = view.findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(pager);

        return view;
    }

    boolean bfirst = false;

    private void getEtatMensuel() {

        selectCurrentMonth();
        appApi.getEtatMensuel(AppData.getInstance().getStatsyear()).enqueue(new Callback<List<Dette>>() {
            @Override
            public void onResponse(Call<List<Dette>> call, Response<List<Dette>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        appApi.getEtatServiceMensuel(AppData.getInstance().getStatsyear()).enqueue(new Callback<List<Dette>>() {
                            @Override
                            public void onResponse(Call<List<Dette>> call2, Response<List<Dette>> response2) {

                                if (response2.isSuccessful())
                                    adapterpager.fill(response.body(), response2.body());
                                else
                                    adapterpager.fill(response.body(), new ArrayList<>());

                            }

                            @Override
                            public void onFailure(Call<List<Dette>> call2, Throwable t2) {
                                adapterpager.fill(response.body(), new ArrayList<>());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Dette>> call, Throwable t) {
            }
        });
    }

    private void selectCurrentMonth() {
        if (!bfirst) {
            bfirst = true;
            final Calendar cldr = Calendar.getInstance();
            pager.setCurrentItem(cldr.get(Calendar.MONTH));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(Dette dette) {

    }

    @Override
    public void doRefreshOnMonthChanged() {
        getEtatMensuel();
    }
}
