package com.optim.bassit.ui.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.optim.bassit.R;
import com.optim.bassit.base.BaseActivity;
import com.optim.bassit.di.modules.NetworkModule;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;


public class InterestAdapter extends BaseAdapter {

    private final Context context;
    private List<Categorie> mData;
    InterestClick mListener;

    public InterestAdapter(Context ctx, InterestClick m) {
        this.context = ctx;
        this.mData = new ArrayList<>();
        mListener = m;
    }


    public void fill(List<Categorie> interests) {
        mData.clear();
        mData.addAll(interests);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public View getView(int position, View vw, ViewGroup parent) {
        vw = LayoutInflater.from(context).inflate(R.layout.interest_item, parent, false);
        Categorie interest = (Categorie) getItem(position);
        vw.setTag(interest);
        vw.setOnClickListener(view -> {

            int has = interest.getHas() == 0 ? 1 : 0;
            mListener.doChange(interest.getId(), has, (b) -> {
                if (b) {
                    interest.setHas(has);
                    HandleCheck(view);
                }
            });
        });

        HandleCheck(vw);
        TextView txt = vw.findViewById(R.id.text);
        ImageView img = vw.findViewById(R.id.image);
        vw.findViewById(R.id.image);

        txt.setText(interest.getCat());


        OptimTools.getPicasso(NetworkModule.ICONS_URL + interest.getImage()).into(img);
        return vw;
    }

    private void HandleCheck(View view) {
        Categorie interest = (Categorie) view.getTag();
        LinearLayout li = view.findViewById(R.id.b_poi);
        if (interest.getHas() == 1)
            li.setBackground(context.getResources().getDrawable(R.drawable.round_button_yellow));
        else
            li.setBackground(context.getResources().getDrawable(R.drawable.round_bordered_yellow));
    }

    public interface InterestClick {
        void doChange(int cat_id, int has, BaseActivity.handleBooleanResponse hand);
    }
}