package com.optim.bassit.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.R;
import com.optim.bassit.data.AppData;
import com.optim.bassit.models.Journal;
import com.optim.bassit.utils.OptimTools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ConsommationAdapter extends RecyclerView.Adapter<ConsommationAdapter.ViewHolder> {

    private List<Journal> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int type;

    // data is passed into the constructor
    public ConsommationAdapter(ItemClickListener listener, int type) {
        this.type = type;
        this.mData = new ArrayList<>();
        this.mClickListener = listener;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from((parent.getContext())).inflate(R.layout.consommation_item, parent, false);
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


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Journal journal;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Journal journal) {
            this.journal = journal;
            itemView.setOnClickListener(view -> mClickListener.onItemClick(journal));
            itemView.setOnLongClickListener(view -> mClickListener.onLongItemClick(journal));

            TextView t_designation = itemView.findViewById(R.id.t_designation);
            TextView t_etat = itemView.findViewById(R.id.t_etat);

            TextView t_montant = itemView.findViewById(R.id.t_montant);
            TextView t_type = itemView.findViewById(R.id.t_type);
            TextView t_stamp = itemView.findViewById(R.id.t_stamp);

            t_designation.setText(AppData.TR(this.journal.getDesignation()));
            t_etat.setVisibility(View.VISIBLE);

            t_etat.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
            if (type != 101) {

                if (journal.getEtat() == -1)
                    t_etat.setVisibility(View.INVISIBLE);
                else if (journal.getEtat() == 0) {
                    t_etat.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.yellow));
                    t_etat.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                } else if (journal.getEtat() == 1)
                    t_etat.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                else if (journal.getEtat() == 2)
                    t_etat.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.red));
                t_etat.setText(journal.getFullEtat(itemView.getContext()));
            } else {
                if (journal.getEtat() == -1)
                    t_etat.setVisibility(View.INVISIBLE);
                else if (journal.getEtat() == 0 || journal.getEtat() == -6) {
                    t_etat.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.yellow));
                    t_etat.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                } else if (journal.getEtat() == 2 || journal.getEtat() == -5)
                    t_etat.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                else if (journal.getEtat() == 99)
                    t_etat.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.red));

                if (journal.getEtat() == 0 || journal.getEtat() == -6)
                    t_etat.setText(R.string.text_attent);
                else if (journal.getEtat() == 2)
                    t_etat.setText(R.string.succes);
                else if (journal.getEtat() == 99)
                    t_etat.setText(R.string.fail);
                else if (journal.getEtat() == -5)
                    t_etat.setText(R.string.text_send);
            }

            if (journal.getMontant() <= 0)
                t_montant.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
            else
                t_montant.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));


            if (type == 0)
                t_montant.setText(new DecimalFormat("#").format(journal.getMontant()) + " " + itemView.getContext().getString(R.string.text_pts));
            else
                t_montant.setText(new DecimalFormat("#").format(journal.getMontant()) + " " + itemView.getContext().getString(R.string.text_da));


            if (type == 10 || type == 11)
                t_stamp.setText(OptimTools.dateToFRString(OptimTools.toDateFromSQL(journal.getStamp().substring(0, 10))));
            else
                t_stamp.setText(OptimTools.dateTimeToFRString(OptimTools.toDateTime(journal.getStamp())));

            t_type.setText(AppData.TR(journal.getType(), "€"));
        }
    }


    public Journal getItem(int id) {
        return mData.get(id);
    }

    public void fill(List<Journal> journals) {
        mData.clear();
        if (journals == null)
            return;
        mData.addAll(journals);
        notifyDataSetChanged();
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(Journal journal);

        boolean onLongItemClick(Journal journal);
    }
}
