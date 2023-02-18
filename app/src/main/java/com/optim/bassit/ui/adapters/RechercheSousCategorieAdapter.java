package com.optim.bassit.ui.adapters;
 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.R;
import com.optim.bassit.models.SousCategorie;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;
 

public class RechercheSousCategorieAdapter extends RecyclerView.Adapter<RechercheSousCategorieAdapter.ViewHolder> {

    private List<SousCategorie> mData;
    private ItemClickListener mListener;

    public RechercheSousCategorieAdapter(ItemClickListener listener) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recherche_souscategorie_item, viewGroup, false);
        return new ViewHolder(view);
    }

    public int selectedSousCategorie() {
        for (SousCategorie sousCategorie : mData) {
            if (sousCategorie.getSelected() == 1)
                return sousCategorie.getId();
        }
        return -1;
    }

    public void toggleSelected(int id) {
        for (SousCategorie cc : mData) {
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void fill(List<SousCategorie> sousCategories) {
        mData.clear();
        if (sousCategories == null)
            return;
        mData.addAll(sousCategories);
        notifyDataSetChanged();
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private SousCategorie sousCategorie;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(SousCategorie sousCategorie) {
            this.sousCategorie = sousCategorie;
            itemView.setOnClickListener(view -> mListener.onItemClick(sousCategorie));

            TextView text = itemView.findViewById(R.id.text);
            ImageView img = itemView.findViewById(R.id.img);
            CardView card = itemView.findViewById(R.id.card);

            text.setText(sousCategorie.getDesignation());
            OptimTools.getPicasso(sousCategorie.getIcon()).into(img);
            if (sousCategorie.getSelected() == 1)
                card.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorAccent));
            else
                card.setCardBackgroundColor(itemView.getResources().getColor(R.color.darkgray));
        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onItemClick(SousCategorie cat);
    }
}