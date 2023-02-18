package com.optim.bassit.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.R;
import com.optim.bassit.models.Avis;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;


public class OldReviewAdapter extends RecyclerView.Adapter<OldReviewAdapter.ViewHolder> {

    private List<Avis> mData;
    private ItemClickListener mListener;

    public OldReviewAdapter(ItemClickListener listener) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.evaluation_item, viewGroup, false);
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

    public void fill(Integer page, List<Avis> pros) {

        if (page <= 1)
            mData.clear();

        mData.addAll(pros);
        notifyDataSetChanged();
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Avis avis;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Avis resul) {
            this.avis = resul;
            itemView.setOnClickListener(view -> mListener.onItemClick(resul));

            TextView username = itemView.findViewById(R.id.t_username);
            TextView content = itemView.findViewById(R.id.t_comment);
            RatingBar ratingBar = itemView.findViewById(R.id.rating_bar);
            ImageView img = itemView.findViewById(R.id.profile_img);
            TextView date = itemView.findViewById(R.id.t_time);

            username.setText(avis.getFullName());
            content.setText(avis.getClient_comment());
            ratingBar.setRating(avis.getClient_avis());
            date.setText(avis.getClosed_at());
            OptimTools.getPicasso(avis.getPinLink()).into(img);
        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onItemClick(Avis avis);
    }
}