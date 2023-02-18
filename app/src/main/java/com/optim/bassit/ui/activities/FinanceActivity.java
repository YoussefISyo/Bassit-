package com.optim.bassit.ui.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.optim.bassit.App;
import com.optim.bassit.data.AppData;
import com.optim.bassit.ui.adapters.FinancePagerAdapter;
import com.optim.bassit.ui.fragments.DetteFragment;
import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.data.AppApi;
import com.optim.bassit.ui.adapters.ConsommationAdapter;
import com.optim.bassit.utils.LockableViewPager;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FinanceActivity extends BaseActivity {

    ConsommationAdapter adapter;

    @BindView(R.id.b_transfer_back)
    ImageButton Back;
    @BindView(R.id.t_filter)
    TextView t_filter;
    @Inject
    AppApi appApi;

    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    private DetteFragment dette_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        ButterKnife.bind(this);
        App.getApp(this.getApplication()).getComponent().inject(this);


        Back.setOnClickListener(v -> finish());



        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.dettes)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.benefice)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.charge_revenu)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.livre_financier)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapterpager = new FinancePagerAdapter
                (getSupportFragmentManager());
        LockableViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(adapterpager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        t_filter.setText(AppData.getInstance().fullMonthYear());

    }

    @OnClick(R.id.b_filter)
    public void doFilter() {
        final Calendar cldr = Calendar.getInstance();

        int mnth = cldr.get(Calendar.MONTH);
        int y = cldr.get(Calendar.YEAR);

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(this,
                (selectedMonth, selectedYear) -> {
                    AppData.getInstance().setMonth(selectedMonth);
                    AppData.getInstance().setYear(selectedYear);
                    t_filter.setText(AppData.getInstance().fullMonthYear());
                    ((ImageButton) findViewById(R.id.b_filter)).setImageDrawable(getResources().getDrawable(R.drawable.ic_date));

                    List<FilterListener> lol = new ArrayList<>();
                    for (FilterListener ll : listeners) {

                        if (ll == null || ((Fragment) ll) == null || ((Fragment) ll).getActivity() == null) {
                            lol.add(ll);
                            continue;
                        }
                        ll.doRefreshOnMonthChanged();
                    }
                    for (FilterListener ll : lol) {
                        unregisterFilter(ll);

                    }
                }, y, mnth);

        builder.setActivatedMonth(mnth)
                .setMinYear(2019)
                .setActivatedYear(y)
                .setMaxYear(y)
                .setTitle(getString(R.string.filtre_par_mois_finance))
                .setOnMonthChangedListener(selectedMonth -> {
                })
                .setOnYearChangedListener(selectedYear -> {
                })
                .build()
                .show();


    }

    FinancePagerAdapter adapterpager;

    public void setBreload() {
        adapterpager.setIsDirty();
    }

    static List<FilterListener> listeners = new ArrayList<>();

    public static void registerFilter(FilterListener listener) {
        listeners.add(listener);
    }

    public static void unregisterFilter(FilterListener listener) {
        try {
            listeners.remove(listener);
        } catch (Exception ex) {

        }
    }

    public interface FilterListener {
        void doRefreshOnMonthChanged();
    }
}


