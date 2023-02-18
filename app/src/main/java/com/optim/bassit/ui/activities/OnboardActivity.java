package com.optim.bassit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.ui.adapters.ViewPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class OnboardActivity extends BaseActivity {

    private ViewPager viewPager;
    private RelativeLayout relativeLayout;
    private ViewPagerAdapter adapter;
    private DotsIndicator dotsIndicator;
    private ImageView imgNext;
    private RelativeLayout txtFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        init();

//        relativeLayout.setOnClickListener(v -> {
//            if (imgNext.getVisibility() == View.VISIBLE){
//                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//            }
//            else if (txtFinish.getVisibility() == View.VISIBLE){
//                startActivity(new Intent(OnboardActivity.this, LoginActivity.class));
//                finish();
//
//            }
//        });

        imgNext.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1));

        txtFinish.setOnClickListener(v -> {
            startActivity(new Intent(OnboardActivity.this, LoginActivity.class));
            finish();
        });

    }

    private void init() {
        viewPager = findViewById(R.id.view_pager);
        dotsIndicator = findViewById(R.id.dots_indicators);
        adapter = new ViewPagerAdapter(this);
        relativeLayout = findViewById(R.id.layoutContainer);
        imgNext = findViewById(R.id.imgtNextSlide);
        txtFinish = findViewById(R.id.txtFinishSlide);
        viewPager.addOnPageChangeListener(listener);
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(viewPager);

        imgNext.setVisibility(View.VISIBLE);
        txtFinish.setVisibility(View.GONE);

    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 2){
                imgNext.setVisibility(View.GONE);
                txtFinish.setVisibility(View.VISIBLE);
            }
            else if (position == 1){
                imgNext.setVisibility(View.VISIBLE);
                txtFinish.setVisibility(View.GONE);
            }
            else if (position == 0){
                imgNext.setVisibility(View.VISIBLE);
                txtFinish.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
