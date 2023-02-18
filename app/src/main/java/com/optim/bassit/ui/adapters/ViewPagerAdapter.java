package com.optim.bassit.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.optim.bassit.R;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    private int images[] = {
            R.drawable.onboard_one,
            R.drawable.onboard_two,
            R.drawable.onboard_three
    };

    private String texts[] = {
            "L'application BASSIT est une solution intelligente et un portrail de communication entre l'artisan et le client",
            "Montrez vos services via l'application et recevez également des demandes de travail via celle-ci",
            "L'inscription sur la plateforme ne prend que quelque secondes",
    };

    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_pager, container, false);

        //Init views
        ImageView imgSlide= v.findViewById(R.id.imgSlide);
        TextView txtDesc = v.findViewById(R.id.txtSlide);

        imgSlide.setImageResource(images[position]);
        txtDesc.setText(texts[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
