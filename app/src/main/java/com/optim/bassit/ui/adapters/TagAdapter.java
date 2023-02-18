package com.optim.bassit.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.optim.bassit.R;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private List<String> mData;
    boolean hasDelete;
    private ItemClickListener mListener;

    public TagAdapter(boolean hasDelete, ItemClickListener listener) {
        this.mData = new ArrayList<>();
        this.hasDelete = hasDelete;
        mListener = listener;
    }


    public void fill(List<String> media) {
        mData.clear();
        for (String str : media) {
            if (str == null || str.matches(""))
                continue;

            String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
            if(mData.contains(cap))
                continue;

            mData.add(cap);
        }

        notifyDataSetChanged();
    }

    public void addOne(String tag) {
        mData.add(tag);
        notifyDataSetChanged();
    }

    public void removeOne(String tag) {
        mData.remove(tag);
        notifyDataSetChanged();
    }

    public List<String> getList() {
        return mData;
    }

    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (hasDelete)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_tag_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagAdapter.ViewHolder holder, int position) {

        holder.title.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.t_tag);

            if (hasDelete) {
                ImageView b_delete = itemView.findViewById(R.id.iv_delete);
                b_delete.setOnClickListener(view -> mListener.onItemClick(title.getText().toString()));
            }

        }
    }

    public interface ItemClickListener {
        void onItemClick(String tag);
    }
}
