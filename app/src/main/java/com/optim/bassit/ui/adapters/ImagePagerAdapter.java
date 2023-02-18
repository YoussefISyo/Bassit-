package com.optim.bassit.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.optim.bassit.models.Photo;
import com.optim.bassit.ui.fragments.ImageFragment;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    List<ImageFragment> all = new ArrayList<>();


    @Override
    public Fragment getItem(int position) {
        return all.get(position);
    }

    @Override
    public int getCount() {
        return all.size();
    }


    public void fillData(List<Photo> data) {
        all.clear();
        for (Photo s : data) {
            all.add(ImageFragment.newInstance(s));
        }

        notifyDataSetChanged();
    }
}