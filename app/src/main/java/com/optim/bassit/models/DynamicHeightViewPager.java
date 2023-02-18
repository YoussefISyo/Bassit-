package com.optim.bassit.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;

import com.optim.bassit.R;

@SuppressLint("AppCompatCustomView")
public class DynamicHeightViewPager extends ViewPager {

    private float mHeightRatioPager;

    public DynamicHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DynamicHeightViewPager, 0, 0);
        try {
            mHeightRatioPager = ta.getFloat(R.styleable.DynamicHeightViewPager_height_ratio_pager, 0.0f);
        } finally {
            ta.recycle();
        }
    }

    public DynamicHeightViewPager(Context context) {
        super(context);
    }

    public void setHeightRatioPager(float ratio) {
        if (ratio != mHeightRatioPager) {
            mHeightRatioPager = ratio;
            requestLayout();
        }
    }

    public double getHeightRatioPager() {
        return mHeightRatioPager;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatioPager > 0.0f) {
            // set the view pager size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatioPager);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
