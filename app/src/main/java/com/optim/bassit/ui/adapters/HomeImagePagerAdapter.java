package com.optim.bassit.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.optim.bassit.models.Ads;
import com.optim.bassit.ui.fragments.HomeImageFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeImagePagerAdapter extends FragmentStatePagerAdapter {

    public HomeImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    List<HomeImageFragment> all = new ArrayList<>();


    @Override
    public Fragment getItem(int position) {

        return all.get(position);
    }

    @Override
    public int getCount() {
        return all.size();
    }


    public void fillData(List<Ads> data) {
        all.clear();
        for (Ads s : data) {
            if (s.getPlace() != 0)
                continue;
            all.add(HomeImageFragment.newInstance(s));
        }

        notifyDataSetChanged();
    }
}