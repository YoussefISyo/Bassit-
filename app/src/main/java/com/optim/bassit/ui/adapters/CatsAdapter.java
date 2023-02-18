package com.optim.bassit.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.R;
import com.optim.bassit.models.Categorie;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;


public class CatsAdapter extends RecyclerView.Adapter<CatsAdapter.ViewHolder> {

    private List<Categorie> mData;

    private ItemClickListener mListener;

    public CatsAdapter(ItemClickListener listener) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_cats_item, viewGroup, false);
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

    public void fill(List<Categorie> cat) {
        mData.clear();
        if (cat == null)
            return;
        mData.addAll(cat);
        notifyDataSetChanged();
    }

    public int selectedCat() {
        for (Categorie cat :
                mData) {
            if (cat.getSelected() == 1)
                return cat.getId();
        }
        return -1;
    }

    public void toggleSelected(int id) {
        for (Categorie cc : mData) {
            if (cc.getId() == id) {
                if (cc.getSelected() == 1)
                    cc.setSelected(-1);
                else
                    cc.setSelected(1);
                notifyItemChanged(mData.indexOf(cc));
            } else {
                if (cc.getSelected() == 1) {
                    cc.setSelected(-1);
                    notifyItemChanged(mData.indexOf(cc));
                }
            }
        }
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Categorie cat;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Categorie cat) {
            this.cat = cat;
            itemView.setOnClickListener(view -> mListener.onItemClick(cat));

            TextView text = itemView.findViewById(R.id.text);
            ImageView img = itemView.findViewById(R.id.img);

            text.setText(cat.getName());
            OptimTools.getPicasso(cat.getIcon()).into(img);

            text.setTextColor(itemView.getResources().getColor(R.color.darkgray));

            if (cat.getSelected() == 1) {
                text.setTextColor(itemView.getResources().getColor(R.color.colorAccent));
            }
        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onItemClick(Categorie cat);
    }
}