package com.optim.bassit.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.R;
import com.optim.bassit.models.Dette;
import com.optim.bassit.utils.OptimTools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetteAdapter extends RecyclerView.Adapter<DetteAdapter.ViewHolder> {

    private final int type;
    private List<Dette> mData;
    private ItemClickListener mListener;

    public DetteAdapter(ItemClickListener listener, int type) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
        this.type = type;
    }

    public void fill(List<Dette> dettes) {
        mData.clear();
        mData.addAll(dettes);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dettes_item, parent, false);
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        private Dette dette;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Dette dette) {
            this.dette = dette;
            itemView.setOnClickListener(view -> mListener.onItemClick(dette));

            OptimTools.getPicasso(dette.getFullImage()).into((CircleImageView) itemView.findViewById(R.id.img_avatar));// ;
            ((TextView) itemView.findViewById(R.id.t_client_name)).setText(dette.getFullname());
            ((TextView) itemView.findViewById(R.id.t_service_title)).setText(dette.getService());
            ((TextView) itemView.findViewById(R.id.t_stamp)).setText(dette.getFullStamp());

            if (type == 0) {
                ((TextView) itemView.findViewById(R.id.t_montant)).setText(itemView.getContext().getString(R.string.montant) + " : " + new DecimalFormat("#").format(dette.getMontant()) + " " + itemView.getContext().getString(R.string.text_da));
                ((TextView) itemView.findViewById(R.id.t_payed)).setText(itemView.getContext().getString(R.string.paye) + " : " + new DecimalFormat("#").format(dette.getPaye()) + " " + itemView.getContext().getString(R.string.text_da));
                ((TextView) itemView.findViewById(R.id.t_reste)).setText(new DecimalFormat("#").format(dette.getReste()) + " " + itemView.getContext().getString(R.string.text_da));
            } else if (type == 1) {
                ((TextView) itemView.findViewById(R.id.t_montant)).setText(itemView.getContext().getString(R.string.montant) + " : " + new DecimalFormat("#").format(dette.getMontant()) + " " + itemView.getContext().getString(R.string.text_da));
                ((TextView) itemView.findViewById(R.id.t_payed)).setText(itemView.getContext().getString(R.string.charges) + " : " + new DecimalFormat("#").format(dette.getCharge()) + " " + itemView.getContext().getString(R.string.text_da));
                ((TextView) itemView.findViewById(R.id.t_reste)).setText(new DecimalFormat("#").format((dette.getMontant() - dette.getCharge())) + " " + itemView.getContext().getString(R.string.text_da));

                ((TextView) itemView.findViewById(R.id.t_reste)).setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));
            }
        }

    }


    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onItemClick(Dette dette);
    }
}
