package com.optim.bassit.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.Dette;
import com.optim.bassit.models.Journal;
import com.optim.bassit.ui.activities.StatistiquesActivity;
import com.optim.bassit.ui.adapters.DetteAdapter;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EtatAnnuelFragment extends Fragment implements DetteAdapter.ItemClickListener, StatistiquesActivity.FilterListener {


    @Inject
    AppApi appApi;

    public EtatAnnuelFragment() {
    }

    public static EtatAnnuelFragment newInstance() {
        EtatAnnuelFragment fragment = new EtatAnnuelFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etat_annuel, container, false);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this, view);

        getEtatAnnuel();

        return view;
    }

    private void getEtatAnnuel() {
        appApi.getEtatAnnuel(AppData.getInstance().getStatsyear()).enqueue(new Callback<List<Journal>>() {
            @Override
            public void onResponse(Call<List<Journal>> call, Response<List<Journal>> response) {
                if (response.isSuccessful()) {
                    drawCharts(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<Journal>> call, Throwable t) {
            }
        });
    }

    @BindView(R.id.barchart)
    BarChart barChart;

    private void drawCharts(List<Journal> data) {


        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {

            String mymonth = String.format("%02d", i) + "/" + AppData.getInstance().getStatsyear();

            boolean bfound = false;
            for (Journal one : data) {
                if (one.getDesignation().equals(mymonth)) {
                    entries.add(new BarEntry((float) one.getMontant(), i));
                    bfound = true;
                    break;
                }


            }
            if (!bfound) {
                entries.add(new BarEntry(0.0f, i));
            }

            labels.add(OptimTools.getShortMonthName(i));
        }


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);


        YAxis left = barChart.getAxisLeft();
        left.setDrawLabels(false); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(true); // no grid lines
        left.setDrawZeroLine(false); // draw a zero line
        barChart.getAxisRight().setEnabled(false); // no right axis


        BarDataSet bardataset = new BarDataSet(entries, getString(R.string.total_benefice));

        BarData vec = new BarData(labels, bardataset);
        bardataset.setColors(new int[]{R.color.colorPrimaryDark}, getActivity());
        vec.setValueTextSize(11.0f);
        barChart.setData(vec);
        barChart.setDescription(getString(R.string.benefice_par_mois));
        barChart.animateY(1000);

        int currmonth = Calendar.getInstance().get(Calendar.MONTH);
        barChart.setScaleYEnabled(false);

        barChart.setVisibleXRange(6,12);
        barChart.zoom(1.5f,0.0f,0,0);
        barChart.moveViewToX(currmonth + 1);

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
        getEtatAnnuel();
    }
}
