package com.optim.bassit.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.optim.bassit.R;
import com.optim.bassit.models.Customer;

import java.util.ArrayList;
import java.util.List;

public class TransfererAdapter extends RecyclerView.Adapter<TransfererAdapter.ViewHolder> {

    private List<Customer> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public TransfererAdapter(ItemClickListener listener) {
        this.mData = new ArrayList<>();
        this.mClickListener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from((parent.getContext())).inflate(R.layout.transferer_item, parent, false);
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
        private Customer customer;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Customer customer) {
            this.customer = customer;
            itemView.setOnClickListener(view -> mClickListener.onItemClick(customer));

            TextView username = itemView.findViewById(R.id.t_username);
            TextView adresse = itemView.findViewById(R.id.t_adresse);

            username.setText(this.customer.getUserName());
            adresse.setText(this.customer.getAddress());

            if(customer.isSelected())
                itemView.findViewById(R.id.cv_tache).setBackgroundColor(10);

        }
    }

    public void setItemSelected(Customer customer) {
        for (Customer cust :mData) {
            cust.setSelected(false);
        }
        customer.setSelected(!customer.isSelected());
        notifyDataSetChanged();
    }

    // convenience method for getting data at click position
    public Customer getItem(int id) {
        return mData.get(id);
    }

    public void fill(List<Customer> customers) {
        mData.clear();
        mData.addAll(customers);
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(Customer customer);
    }
}
