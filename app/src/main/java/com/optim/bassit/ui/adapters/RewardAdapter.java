package com.optim.bassit.ui.adapters;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.R;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Reward;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.ViewHolder> {

    private List<Reward> mData;

    private ItemClickListener mListener;

    public RewardAdapter(ItemClickListener listener) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reward_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void fill(List<Reward> rewards) {
        mData.clear();
        if (rewards == null)
            return;
        mData.addAll(rewards);
        notifyDataSetChanged();
    }

    public void fillMontant(List<Reward> rewards) {
        mData.clear();
        if (rewards == null)
            return;
        for (Reward rew : rewards) {
            if (rew.getMoneyunit() == 1)
                mData.add(rew);
        }
        notifyDataSetChanged();
    }

    public void fillPoints(List<Reward> rewards) {
        mData.clear();
        if (rewards == null)
            return;
        for (Reward rew : rewards) {
            if (rew.getMoneyunit() == 0)
                mData.add(rew);
        }

        notifyDataSetChanged();
    }

    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Reward reward;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Reward reward) {
            this.reward = reward;
            itemView.setOnClickListener(view -> mListener.onItemClick(reward));

            TextView text = itemView.findViewById(R.id.t_name);
            TextView t_points = itemView.findViewById(R.id.t_points);
            TextView t_old = itemView.findViewById(R.id.t_old);
            ImageView img = itemView.findViewById(R.id.img);
            Button b_activer = itemView.findViewById(R.id.b_activer);
            Button t_encours = itemView.findViewById(R.id.t_encours);

            if(Locale.getDefault().getLanguage().equals("en"))
                text.setText(reward.getD_en());
            else  if(Locale.getDefault().getLanguage().equals("ar"))
                text.setText(reward.getD_ar());
            else
                text.setText(reward.getDesignation());
            if (reward.getMoneyunit() == 0) {
                t_old.setVisibility(View.GONE);
                b_activer.setOnClickListener(view -> new AlertDialog.Builder(itemView.getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(itemView.getContext().getString(R.string.text_confirm_activation_plan) + reward.getDesignation() + " "  + itemView.getContext().getString(R.string.text_a) + " " + reward.getPoints() + itemView.getContext().getString(R.string.text_pts))
                        .setPositiveButton(R.string.text_ok, (d, i) -> {
                            mListener.doPlan(reward);
                        })
                        .setNegativeButton(R.string.text_cancel, (d, i) -> {
                            d.dismiss();
                        }).show());//(v -> mListener.doPlan(reward));
                t_points.setText(reward.getPoints() + " " + itemView.getContext().getString(R.string.text_pts));
                if (CurrentUser.getInstance().getmCustomer().getPlan() == reward.getValeur()) {
                    t_encours.setVisibility(View.VISIBLE);
                    b_activer.setVisibility(View.GONE);
                } else if (CurrentUser.getInstance().getmCustomer().getPlan() > reward.getValeur()) {
                    t_encours.setVisibility(View.GONE);
                    b_activer.setVisibility(View.GONE);
                } else {
                    t_encours.setVisibility(View.GONE);
                    b_activer.setVisibility(View.VISIBLE);
                }
                if (CurrentUser.getInstance().getmCustomer().getPoints() >= Integer.valueOf(reward.getPoints()) && b_activer.getVisibility() == View.VISIBLE) {
                    b_activer.setEnabled(true);
                    b_activer.setBackground(itemView.getContext().getDrawable(R.drawable.round_button));
                    t_points.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    b_activer.setEnabled(false);
                    b_activer.setBackground(itemView.getContext().getDrawable(R.drawable.round_button_white));
                    t_points.setTextColor(itemView.getContext().getResources().getColor(R.color.color_gray));
                }


            } else {

                b_activer.setOnClickListener(view -> new AlertDialog.Builder(itemView.getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(reward.getPoints() + itemView.getContext().getString(R.string.text_buy_stuff) + reward.getValeur()
                                + itemView.getContext().getString(R.string.text_pts) + " " +
                                itemView.getContext().getString(R.string.text_a) + " " + reward.getPoints() + " " + itemView.getContext().getString(R.string.text_da))
                        .setPositiveButton(R.string.text_ok, (d, i) -> {
                            mListener.doBuy(reward);
                        })
                        .setNegativeButton(R.string.text_cancel, (d, i) -> {
                            d.dismiss();
                        }).show());//mListener.doBuy(reward));
                t_points.setText(reward.getPoints() + " " + itemView.getContext().getString(R.string.text_da));
                b_activer.setText(R.string.text_acheter);
                t_old.setText(reward.getOldprix()+ " " + itemView.getContext().getString(R.string.text_da));
                if(reward.getOldprix()==null || reward.getOldprix().equals("") || reward.getOldprix().equals("0"))
                    t_old.setVisibility(View.GONE);
                else {
                    t_old.setVisibility(View.VISIBLE);
                    t_points.setText(itemView.getContext().getString(R.string.khsm) +" "+reward.getPorso()+"% "+reward.getPoints() + " " + itemView.getContext().getString(R.string.text_da));
                }
                t_old.setPaintFlags(t_old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                if (CurrentUser.getInstance().getmCustomer().getCredit() >= Integer.valueOf(reward.getPoints())) {
                    b_activer.setEnabled(true);
                    b_activer.setBackground(itemView.getContext().getDrawable(R.drawable.round_button));
                    t_points.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    b_activer.setEnabled(false);
                    b_activer.setBackground(itemView.getContext().getDrawable(R.drawable.round_button_white));
                    t_points.setTextColor(itemView.getContext().getResources().getColor(R.color.color_gray));
                }
            }

            if (reward.getSysimage() == 1) {
                try {
                    int id = itemView.getContext().getResources().getIdentifier(reward.getImage(), "drawable", itemView.getContext().getPackageName());
                    if (reward.getImage().startsWith("ic"))
                        img.setColorFilter(itemView.getContext().getResources().getColor(R.color.yellow));
                    img.setImageResource(id);
                } catch (Exception ex) {
                }

            } else
                OptimTools.getPicasso(reward.getFullImage()).into(img);
        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onItemClick(Reward reward);

        void doBuy(Reward reward);

        void doPlan(Reward reward);
    }
}