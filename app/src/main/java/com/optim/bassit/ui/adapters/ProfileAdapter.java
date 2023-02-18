package com.optim.bassit.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.optim.bassit.R;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<HomeFeed> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ProfileAdapter(ItemClickListener listener) {
        this.mClickListener = listener;
        this.mData = new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from((parent.getContext())).inflate(R.layout.service_item, parent, false);
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

    public void fill(List<HomeFeed> services) {
        mData.clear();
        mData.addAll(services);
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private HomeFeed homeFeed;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(HomeFeed homeFeed) {
            this.homeFeed = homeFeed;
            itemView.setOnClickListener(view -> mClickListener.onItemClick(homeFeed));

            ImageView photo = itemView.findViewById(R.id.photo);
            TextView title = itemView.findViewById(R.id.t_title);
            TextView rate = itemView.findViewById(R.id.t_rate);

            OptimTools.getPicasso(this.homeFeed.getServiceImageLink(800)).into(photo);
            title.setText(this.homeFeed.getTitle());
            rate.setText("0");

        }
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(HomeFeed service);
    }
}
