package com.optim.bassit.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.optim.bassit.R;
import com.optim.bassit.models.Montant;

import java.util.ArrayList;
import java.util.List;

import static com.optim.bassit.utils.OptimTools.dateToFRString;
import static com.optim.bassit.utils.OptimTools.toDateTime;

public class MontantsAdapter extends RecyclerView.Adapter<MontantsAdapter.ViewHolder>{

    private List<Montant> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MontantsAdapter(ItemClickListener listener) {
        this.mClickListener = listener;
        this.mData = new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.montants_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MontantsAdapter.ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void fill(List<Montant> montants) {
        mData.clear();
        mData.addAll(montants);
        notifyDataSetChanged();
    }

    // convenience method for getting data at click position
    public Montant getItem(int id) {
        return mData.get(id);
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Montant montant;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Montant motant) {
            montant = motant;

            itemView.setOnClickListener(view -> mClickListener.onItemClick(montant));
            TextView designation = itemView.findViewById(R.id.t_deignation);
            TextView type = itemView.findViewById(R.id.t_type);
            TextView t_montant = itemView.findViewById(R.id.t_montant);
            TextView date = itemView.findViewById(R.id.t_date);

            designation.setText(montant.getDesignation());
            type.setText(montant.getType());
            t_montant.setText((String.format("%1$,.2f", montant.getMontant() )));
            date.setText(dateToFRString(toDateTime(montant.getDate())));

        }
    }



    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(Montant montant);
    }
}
