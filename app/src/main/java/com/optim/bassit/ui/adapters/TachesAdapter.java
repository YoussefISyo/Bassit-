package com.optim.bassit.ui.adapters;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.optim.bassit.R;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Tache;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TachesAdapter extends RecyclerView.Adapter<TachesAdapter.ViewHolder> {

    private List<Tache> mData;
    private ItemClickListener mListener;
    Context mcontext;
    public TachesAdapter(ItemClickListener listener, Context mcontext) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
        this.mcontext=mcontext;
    }

    public void fill(List<Tache> taches) {
        mData.clear();
        mData.addAll(taches);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taches_item, parent, false);
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
        private Tache tache;

        ViewHolder(View itemView) {
            super(itemView);
        }



        public void bind(Tache tache) {
            this.tache = tache;
            itemView.setOnClickListener(view -> mListener.onItemClick(tache));

            TextView t_client = itemView.findViewById(R.id.t_client_name);
            TextView t_title = itemView.findViewById(R.id.t_title);
//            TextView t_cat = itemView.findViewById(R.id.t_cat);
            TextView t_date = itemView.findViewById(R.id.t_date);
  //          TextView t_new = itemView.findViewById(R.id.t_new);
            TextView t_montant = itemView.findViewById(R.id.t_montant);
            CircleImageView avatar = itemView.findViewById(R.id.img_avatar);
            ImageView plan_img = itemView.findViewById(R.id.plan_img);

   //         t_new.setVisibility(View.VISIBLE);
            t_montant.setVisibility(View.GONE);
            if (tache.getStatus() == 2) {
//                t_new.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
//                t_new.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
//                t_new.setText(R.string.text_success_w);
                t_montant.setText(tache.getMontant());
                t_montant.setVisibility(View.VISIBLE);


            } else if (tache.getStatus() == 99) {
//                t_new.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.red));
//                t_new.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
//                t_new.setText(R.string.text_fail_w);


            } else {
//                t_new.setText(R.string.text_new);
//                t_new.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.yellow));
//                t_new.setTextColor(itemView.getContext().getResources().getColor(R.color.black));

                if (tache.getBseen() == 1){}
//                    t_new.setVisibility(View.INVISIBLE);
            }

            plan_img.setVisibility(View.VISIBLE);

            if (tache.getClient_id() == CurrentUser.getInstance().getmCustomer().getId()) {
                t_client.setText(tache.getFullServiceBName());
                OptimTools.getPicasso(tache.getServicePinLink()).into(avatar);
                switch (tache.getSplan()) {
                    case 101:
                        plan_img.setImageDrawable(itemView.getContext().getDrawable(R.drawable.bronze));
                        break;
                    case 102:
                        plan_img.setImageDrawable(itemView.getContext().getDrawable(R.drawable.silver));
                        break;
                    case 103:
                        plan_img.setImageDrawable(itemView.getContext().getDrawable(R.drawable.gold));
                        break;
                    case 104:
                        plan_img.setImageDrawable(itemView.getContext().getDrawable(R.drawable.diamond));
                        break;
                    default:
                        plan_img.setVisibility(View.GONE);
                        break;
                }

            } else {
                t_client.setText(tache.getFullClientBName());
                OptimTools.getPicasso(tache.getClientPinLink()).into(avatar);
                switch (tache.getPlan()) {
                    case 101:
                        plan_img.setImageDrawable(itemView.getContext().getDrawable(R.drawable.bronze));
                        break;
                    case 102:
                        plan_img.setImageDrawable(itemView.getContext().getDrawable(R.drawable.silver));
                        break;
                    case 103:
                        plan_img.setImageDrawable(itemView.getContext().getDrawable(R.drawable.gold));
                        break;
                    case 104:
                        plan_img.setImageDrawable(itemView.getContext().getDrawable(R.drawable.diamond));
                        break;
                    default:
                        plan_img.setVisibility(View.GONE);
                        break;
                }
            }


           if(tache.getTypec().equals("offer")){
//              t_cat.setTextColor(Color.BLUE);
             /*  Drawable d = mcontext.getResources().getDrawable(R.drawable.round_bordered_red);
               t_cat.setBackground(d);*/
//               t_cat.setText(R.string.fromoffer);
                //t_cat.setVisibility();
                t_title.setText(tache.getTitlec());

            }

                else{
             /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   t_cat.setTextColor(mcontext.getResources().getColor(R.color.colorPrimary));
               }
               ;*/
//               t_cat.setTextColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));

               // Drawable d = mcontext.getResources().getDrawable(R.drawable.round_dark);
            //   t_cat.setBackground();
//               t_cat.setBackgroundColor(Color.WHITE);
//                t_cat.setText(tache.getCategorie() + " > " + tache.getSouscategorie());
                t_title.setText(tache.getTitle());
            }

            t_date.setText(tache.getStampHumain());


        }

    }


    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onItemClick(Tache homefeed);
    }
}
