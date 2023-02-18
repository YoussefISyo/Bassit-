package com.optim.bassit.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.optim.bassit.R;
import com.optim.bassit.models.Activite;

import java.util.ArrayList;
import java.util.List;

public class ActiviteAdapter extends RecyclerView.Adapter<ActiviteAdapter.ViewHolder> {

    private List<Activite> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ActiviteAdapter(ItemClickListener listener) {
        this.mClickListener = listener;
        this.mData = new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.activite_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    public void fill(List<Activite> activites) {
        mData.clear();
        mData.addAll(activites);
        notifyDataSetChanged();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Activite activite;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Activite activite) {
            this.activite = activite;
            itemView.setOnClickListener(view -> mClickListener.onItemClick(activite));

            TextView client = itemView.findViewById(R.id.t_client);
            TextView montant = itemView.findViewById(R.id.t_montant);
            TextView payed = itemView.findViewById(R.id.t_payed);
            TextView charge = itemView.findViewById(R.id.t_charge);
            TextView reste = itemView.findViewById(R.id.t_reste);
            TextView date = itemView.findViewById(R.id.t_date);

            client.setText(activite.getClient());
            montant.setText(String.format("%1$,.2f", activite.getMontant()));
            payed.setText(String.format("%1$,.2f", activite.getPayed()));
            charge.setText(String.format("%1$,.2f", activite.getCharge()));
            reste.setText(String.format("%1$,.2f", activite.getMontant() - activite.getPayed()));
            date.setText(activite.getDate());
        }


    }

    // convenience method for getting data at click position
    public Activite getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(Activite activite);
    }
}
