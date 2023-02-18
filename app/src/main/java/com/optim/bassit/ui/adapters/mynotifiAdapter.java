package com.optim.bassit.ui.adapters;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.R;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.notifi;
import com.optim.bassit.models.repreq;
import com.optim.bassit.utils.OptimTools;
import com.skyhope.showmoretextview.ShowMoreTextView;

import java.util.ArrayList;
import java.util.List;

public class mynotifiAdapter extends RecyclerView.Adapter<mynotifiAdapter.ViewHolder> {

    private List<notifi> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mListener;
    private ViewGroup viewGroup;
   // boolean bme=false;
    // data is passed into the constructor
    public mynotifiAdapter(ItemClickListener listener) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mynotifi_item, viewGroup, false);

        this.viewGroup = viewGroup;
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

    public void fill(int page, List<notifi> l) {
       // this.bme=bme;
        if (page == 1)
            mData.clear();
        for (notifi feed : l) {
            mData.add(feed);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    // convenience method for getting data at click position
    public notifi getItem(int id) {
        return mData.get(id);
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private notifi notifi;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(notifi home) {
            notifi = home;
           // ImageView img2 = itemView.findViewById(R.id.image);
            if (home.getId() >= 0) {
                TextView username = itemView.findViewById(R.id.t_username);
                TextView categorie = itemView.findViewById(R.id.t_categorie);
                ShowMoreTextView content = itemView.findViewById(R.id.t_content);
                TextView title = itemView.findViewById(R.id.t_title);
           ///     TextView t_city = itemView.findViewById(R.id.t_city);
                TextView type = itemView.findViewById(R.id.msg);
           //     ImageView plan_img = itemView.findViewById(R.id.plan_img);
            //    type.setText(notifi.getPrix());
                title.setText(notifi.getTitle());
             //   t_city.setText(notifi.getDuration());
                username.setText(notifi.getFullName());
               // if (homefeed.getTherole() == 33)
              //      categorie.setText("Divers");
             //   else
               //     categorie.setText(homefeed.getCategorie() + " > " + homefeed.getSouscategorie());

                if(notifi.getOpr_type().equals("chat"))type.setText(itemView.getContext().getString(R.string.new_message));
                else if(notifi.getOpr_type().equals("repreq"))type.setText(itemView.getContext().getString(R.string.new_repreq));
                else if(notifi.getOpr_type().equals("req"))type.setText(itemView.getContext().getString(R.string.new_req));
                else if(notifi.getOpr_type().equals("service"))type.setText(itemView.getContext().getString(R.string.new_service));
                else if(notifi.getOpr_type().equals("bonus"))type.setText(itemView.getContext().getString(R.string.new_bonus));
                else if(notifi.getOpr_type().equals("demande service"))type.setText(R.string.new_demand_service);

                content.setText(notifi.getDes());
                content.setShowingLine(4);

                content.addShowMoreText(itemView.getContext().getString(R.string.showmore));
                content.addShowLessText(itemView.getContext().getString(R.string.showless));
                content.setShowMoreColor(Color.RED); // or other color
                content.setShowLessTextColor(Color.RED);
                categorie.setText(notifi.getDate_created());


                ImageView img = itemView.findViewById(R.id.profile_img);
                OptimTools.getPicasso(notifi.getPinLink()).into(img);
                itemView.setOnClickListener(view -> mListener.onMessageClick(home));
              //  if(notifi.getVu().equals("0"))  itemView.setBackgroundColor(Color.GRAY);

             //   String link = homefeed.getServiceImageLink(800);
             //   OptimTools.getPicasso(link).into(img2);
             //   ImageView iwalk = itemView.findViewById(R.id.i_walk);
            //    plan_img.setVisibility(View.VISIBLE);
             /*   switch (homefeed.getPlan()) {
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
                }*/

                //itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_item, viewGroup, false);
              /*  RecyclerView recyclerView = itemView.findViewById(R.id.rvTags);
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(itemView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                TagAdapter tagAdapter = new TagAdapter(false, null);
                recyclerView.setAdapter(tagAdapter);*/

            //    iwalk.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));
            //    t_city.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));

              /*  if (homefeed.getTags() != null) {
                    List<String> tags = new ArrayList<>(Arrays.asList(homefeed.getTags().split(";")));
                    tagAdapter.fill(tags);
                }*/

           /*     LinearLayout bmessage = itemView.findViewById(R.id.b_message);
                LinearLayout bmod = itemView.findViewById(R.id.b_messag);
                LinearLayout brem = itemView.findViewById(R.id.b_rem);
                bmessage.setOnClickListener(view -> mListener.onMessageClick(home));
                brem.setOnClickListener(view -> mListener.onremoveClick(home));
                bmod.setOnClickListener(view -> mListener.onModifiClick(home));
                //else
               if (CurrentUser.getInstance().isLogin() &&  notifi.getUser_id().equals(CurrentUser.getInstance().getmCustomer().getId()+"") && !bme) {
                   bmessage.setVisibility(View.GONE);
                   bmod.setVisibility(View.VISIBLE);
                   brem.setVisibility(View.VISIBLE);
               }else if(bme) {

                    bmessage.setVisibility(View.VISIBLE);
                 //   bmessage.setVisibility(View.GONE);
                    brem.setVisibility(View.GONE);
                    bmod.setVisibility(View.GONE);
              }else {
                   bmessage.setVisibility(View.GONE);
                   brem.setVisibility(View.GONE);
                   bmod.setVisibility(View.GONE);
                   if (Build.VERSION.SDK_INT >= 11) {
                       type.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                   }
                   float radius = type.getTextSize() / 3;
                   BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                   type.getPaint().setMaskFilter(filter);
                   type.setVisibility(View.GONE);
               }*/
             /*   if(bme){
                    brem.setVisibility(View.GONE);

                   // bmessage.setVisibility(View.VISIBLE);
                }else   bmessage.setVisibility(View.GONE);*/
            } else {
              /*  OptimTools.getPicasso(homefeed.getImage()).into(img2);
                img2.setOnClickListener((a) -> {
                    mListener.onAdsClick(homefeed);
                });*/
            }

        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onMessageClick(notifi notifi);
    }
}
