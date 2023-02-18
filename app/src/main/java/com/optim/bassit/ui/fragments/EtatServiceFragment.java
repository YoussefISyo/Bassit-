package com.optim.bassit.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.optim.bassit.App;
import com.optim.bassit.R;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.Dette;
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

public class EtatServiceFragment extends Fragment implements DetteAdapter.ItemClickListener, StatistiquesActivity.FilterListener {


    @Inject
    AppApi appApi;

    public EtatServiceFragment() {
    }

    public static EtatServiceFragment newInstance() {
        EtatServiceFragment fragment = new EtatServiceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etat_service, container, false);
        App.getApp(getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this, view);

        getEtatService();

        return view;
    }

    private void getEtatService() {
        getDataDrawPie();
        getDataDrawLines();
    }

    private void getDataDrawLines() {
        appApi.getEtatServiceMensuel(AppData.getInstance().getStatsyear()).enqueue(new Callback<List<Dette>>() {
            @Override
            public void onResponse(Call<List<Dette>> call, Response<List<Dette>> response) {
                if (response.isSuccessful()) {
                    drawLines(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Dette>> call, Throwable t) {
            }
        });
    }

    private void drawLines(List<Dette> data) {

        int currmonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        int curryear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> services = new ArrayList<>();
        List<ILineDataSet> allDS = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<>();

        for (Dette one : data) {
            if (services.contains(one.getService()))
                continue;
            services.add(one.getService());
        }

        for (String service : services) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {

                if (curryear == AppData.getInstance().getStatsyear() && i > currmonth)
                    continue;
                String mymonth = String.format("%02d", i) + "/" + AppData.getInstance().getStatsyear();

                boolean bfound = false;
                for (Dette one : data) {
                    if (one.getFullname().equals(mymonth) && one.getService().equals(service)) {
                        entries.add(new Entry((float) one.getMontant(), i - 1));
                        bfound = true;
                        break;
                    }


                }
                if (!bfound) {
                    entries.add(new BarEntry(0.0f, i - 1));
                }


            }

            LineDataSet lineDS = new LineDataSet(entries, service);
            lineDS.setColors(new int[]{ColorTemplate.COLORFUL_COLORS[allDS.size() % 5]});
            lineDS.setDrawCircleHole(false);
            lineDS.setDrawCircles(false);
            lineDS.setDrawCubic(true);
            lineDS.setLineWidth(2);
            lineDS.setCubicIntensity(0.05f);
            allDS.add(lineDS);

        }
        for (int i = 1; i <= 12; i++)
            if (curryear == AppData.getInstance().getStatsyear() && i > currmonth)
                continue;
            else
                labels.add(OptimTools.getShortMonthName(i));

        XAxis xAxis = linechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        YAxis left = linechart.getAxisLeft();
        left.setDrawLabels(false); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(true); // no grid lines
        left.setDrawZeroLine(false); // draw a zero line
        linechart.getAxisRight().setEnabled(false); // no right axis

        LineData vec = new LineData(labels, allDS);

        vec.setValueTextSize(11.0f);
        linechart.setData(vec);
        linechart.setDescription(getString(R.string.benefice_par_service));
        linechart.animateY(1000);

        //int currmonth = Calendar.getInstance().get(Calendar.MONTH);
        linechart.setScaleYEnabled(false);

        linechart.setVisibleXRange(6, 12);
        //     linechart.zoom(1.5f,0.0f,0,0);
        //    linechart.moveViewToX(currmonth + 1);
    }

    private void getDataDrawPie() {
        appApi.getEtatService(AppData.getInstance().getStatsyear()).enqueue(new Callback<List<Dette>>() {
            @Override
            public void onResponse(Call<List<Dette>> call, Response<List<Dette>> response) {
                if (response.isSuccessful()) {
                    drawCharts(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<Dette>> call, Throwable t) {
            }
        });
    }

    @BindView(R.id.piechart)
    PieChart pieChart;
    @BindView(R.id.linechart)
    LineChart linechart;

    private void drawCharts(List<Dette> data) {


        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int i = 0;
        for (Dette one : data) {
            entries.add(new Entry((float) one.getMontant(), i));
            labels.add(one.getService());
            i++;
        }


        PieDataSet pieDataSet = new PieDataSet(entries, getString(R.string.benefice_service));

        PieData vec = new PieData(labels, pieDataSet);
        //bardataset.setColors(new int[]{R.color.colorPrimaryDark}, getActivity());
        vec.setValueTextSize(11.0f);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setRotationEnabled(false);
        pieChart.setData(vec);
        pieChart.setDescription(getString(R.string.benefice_service_2));
        pieChart.animateXY(1000, 1000);


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
        getEtatService();
    }
}
