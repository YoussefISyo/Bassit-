package com.optim.bassit.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.optim.bassit.models.Dette;
import com.optim.bassit.ui.fragments.StatsMonthFragment;

import java.util.ArrayList;
import java.util.List;

public class EtatMensuelPagerAdapter extends FragmentStatePagerAdapter {

    public EtatMensuelPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    List<StatsMonthFragment> all = new ArrayList<>();


    @Override
    public Fragment getItem(int position) {

        if (all.size() == 0) {
            for (int i = 1; i <= 12; i++) {
                all.add(StatsMonthFragment.newInstance(i));
            }
        }

        return all.get(position);
    }

    @Override
    public int getCount() {
        return 12;
    }


    public void fill(List<Dette> data, List<Dette> piedata) {
        if (all.size() == 0)
            return;

        for (StatsMonthFragment one : all) {
            one.fillData(data, piedata);
        }
    }
}
