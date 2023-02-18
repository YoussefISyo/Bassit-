package com.optim.bassit.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.optim.bassit.R;
import com.optim.bassit.models.Avis;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;

public class AvisAdapter extends RecyclerView.Adapter<AvisAdapter.ViewHolder> {

    private List<Avis> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public AvisAdapter() {
        this.mData = new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.evaluation_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void fill(List<Avis> homeFeeds) {
        mData.clear();
        mData.addAll(homeFeeds);
        notifyDataSetChanged();
    }

    // convenience method for getting data at click position
    public Avis getItem(int id) {
        return mData.get(id);
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Avis avis;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Avis avis) {
            this.avis = avis;

            TextView username = itemView.findViewById(R.id.t_username);
            TextView content = itemView.findViewById(R.id.t_comment);
            RatingBar ratingBar = itemView.findViewById(R.id.rating_bar);
            ImageView img = itemView.findViewById(R.id.profile_img);
            TextView date = itemView.findViewById(R.id.t_time);

            username.setText(avis.getFullName());
            content.setText(avis.getClient_comment());
            ratingBar.setRating(avis.getAvis());
            date.setText(avis.getClosed_at());
            OptimTools.getPicasso(avis.getPinLink()).into(img);
        }
    }
}
