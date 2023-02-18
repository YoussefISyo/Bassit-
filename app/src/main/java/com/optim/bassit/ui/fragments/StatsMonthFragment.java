package com.optim.bassit.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.optim.bassit.R;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.Dette;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StatsMonthFragment extends Fragment {

    private int month;


    Dette mDette;

    public static StatsMonthFragment newInstance(int month) {
        StatsMonthFragment fragment = new StatsMonthFragment();
        Bundle args = new Bundle();
        args.putInt("month", month);
        fragment.setArguments(args);
        return fragment;
    }

    public StatsMonthFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        month = getArguments().getInt("month", 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_stats_month, container, false);
        ButterKnife.bind(this, vw);

        ((TextView) vw.findViewById(R.id.t_month)).setText(OptimTools.getMonthName(month));
        ((TextView) vw.findViewById(R.id.t_year)).setText(AppData.getInstance().getStatsyear() + "");

        fillOne(vw);

        return vw;
    }

    static List<Dette> staticData;
    static List<Dette> staticPiedata;

    public void fillData(List<Dette> data, List<Dette> piedata) {

        staticData = data;
        staticPiedata = piedata;
        if (null != getView())
            fillOne(getView());
    }

    @BindView(R.id.piechart)
    PieChart pieChart;


    private void drawCharts(List<Dette> data) {

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int i = 0;
        for (Dette one : data) {
            entries.add(new Entry((float) one.getMontant(), i));
            labels.add(one.getService());
            i++;
        }


        PieDataSet pieDataSet = new PieDataSet(entries, getString(R.string.benefice_par_service_3));

        PieData vec = new PieData(labels, pieDataSet);

        vec.setValueTextSize(11.0f);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(vec);
        pieChart.setRotationEnabled(false);
        pieChart.setDescription(getString(R.string.benefice_par_service2));
        pieChart.animateXY(1000, 1000);


    }


    private void fillOne(View vw) {


        if (staticData == null)
            return;

        String mymonth = String.format("%02d", month) + "/" + AppData.getInstance().getStatsyear();

        for (Dette one : staticData) {
            if (one.getFullname().equals(mymonth)) {
                ((TextView) vw.findViewById(R.id.t_tache)).setText(String.format("%1$,.2f", one.getMontant()));
                ((TextView) vw.findViewById(R.id.t_revenus)).setText(String.format("%1$,.2f", one.getPaye()));
                ((TextView) vw.findViewById(R.id.t_charges)).setText(String.format("%1$,.2f", one.getCharge()));
                ((TextView) vw.findViewById(R.id.t_benefice)).setText(String.format("%1$,.2f", one.getMontant() + one.getPaye() + one.getCharge()));
            }
        }
        if (staticPiedata == null) {
            drawCharts(new ArrayList<>());
            return;
        }

         List<Dette> many = new ArrayList<>();
        for (Dette one : staticPiedata) {
            if (one.getFullname().equals(mymonth)) {
                many.add(one);
            }
        }
        drawCharts(many);

    }
}
