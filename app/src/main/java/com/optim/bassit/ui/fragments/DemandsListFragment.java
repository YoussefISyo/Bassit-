package com.optim.bassit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.optim.bassit.R;

public class DemandsListFragment extends Fragment {

    private View view;
    private Toolbar toolbar;

    private static final int NUM_PAGES = 3;
    //The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
    public static ViewPager2 viewPager;
    // The pager adapter, which provides the pages to the view pager widget.
    private FragmentStateAdapter pagerAdapter;
    // Arrey of strings FOR TABS TITLES
    private String[] titles;
// tab titles

    public DemandsListFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_demandslist, container, false);

        toolbar = view.findViewById(R.id.mToolbarDemands);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        viewPager = view.findViewById(R.id.viewpagerdemands);
        pagerAdapter = new MyPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        //inflating tab layout
        TabLayout tabLayout = ( TabLayout) view.findViewById(R.id.tab_layout_trips);

        titles = new String[]{getString(R.string.waiting), getString(R.string.active_tab) , getString(R.string.finished)};

        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(titles[position]))).attach();

        return view;
    }

    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(DemandsListFragment fa) {
            super(fa);
        }


        @Override
        public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return WaitingDemands.newInstance("En Attente");
                }
                case 1: {
                    return ActiveDemands.newInstance("Active");
                }
                case 2: {
                    return FinishedDemands.newInstance("Terminé");
                }
                default:
                    return WaitingDemands.newInstance("En Attente");
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}
