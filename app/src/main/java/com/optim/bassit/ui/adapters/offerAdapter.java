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
import com.optim.bassit.models.Ads;
import com.optim.bassit.models.repreq;
import com.optim.bassit.utils.OptimTools;
import com.skyhope.showmoretextview.ShowMoreTextView;

import java.util.ArrayList;
import java.util.List;

public class offerAdapter extends RecyclerView.Adapter<offerAdapter.ViewHolder> {

    private List<repreq> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mListener;
    private ViewGroup viewGroup;
    boolean bme=false;
    // data is passed into the constructor
    public offerAdapter(ItemClickListener listener) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offer_item, viewGroup, false);

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

    public void fill(int page, List<repreq> homeFeeds, boolean bme) {
        this.bme=bme;
        if (page == 1)
            mData.clear();
        for (repreq feed : homeFeeds) {
            mData.add(feed);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    // convenience method for getting data at click position
    public repreq getItem(int id) {
        return mData.get(id);
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private repreq homefeed;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(repreq home) {
            homefeed = home;
           // ImageView img2 = itemView.findViewById(R.id.image);
            if (home.getId() >= 0) {
                TextView username = itemView.findViewById(R.id.t_username);
                TextView categorie = itemView.findViewById(R.id.t_categorie);
                ShowMoreTextView content = itemView.findViewById(R.id.t_content);
                TextView title = itemView.findViewById(R.id.t_title);
                TextView t_city = itemView.findViewById(R.id.t_city);
                TextView type = itemView.findViewById(R.id.msg);
           //     ImageView plan_img = itemView.findViewById(R.id.plan_img);
                type.setText(homefeed.getPrix());
               // title.setText(homefeed.getTitle());
                t_city.setText(homefeed.getDuration());
                username.setText(homefeed.getFullName());
               // if (homefeed.getTherole() == 33)
              //      categorie.setText("Divers");
             //   else
               //     categorie.setText(homefeed.getCategorie() + " > " + homefeed.getSouscategorie());
                content.setText(homefeed.getDes());
                content.setShowingLine(4);

                content.addShowMoreText(itemView.getContext().getString(R.string.showmore));
                content.addShowLessText(itemView.getContext().getString(R.string.showless));
                content.setShowMoreColor(Color.RED); // or other color
                content.setShowLessTextColor(Color.RED);
                categorie.setText(homefeed.getDaterep());


                ImageView img = itemView.findViewById(R.id.profile_img);
                OptimTools.getPicasso(homefeed.getPinLink()).into(img);


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

                LinearLayout bmessage = itemView.findViewById(R.id.b_message);
                LinearLayout btn_decline = itemView.findViewById(R.id.btn_decline);
                LinearLayout btn_accept = itemView.findViewById(R.id.btn_accept);
                LinearLayout bmod = itemView.findViewById(R.id.b_messag);
                LinearLayout brem = itemView.findViewById(R.id.b_rem);
                bmessage.setOnClickListener(view -> mListener.onMessageClick(home));
                brem.setOnClickListener(view -> mListener.onremoveClick(home));
                bmod.setOnClickListener(view -> mListener.onModifiClick(home));
                btn_accept.setOnClickListener(view -> mListener.onAcceptClick(home));
                btn_decline.setOnClickListener(view -> mListener.onDeclineClick(home));

                //else
               if (CurrentUser.getInstance().isLogin() &&  homefeed.getUser_id().equals(CurrentUser.getInstance().getmCustomer().getId()+"") && !bme) {
                   bmessage.setVisibility(View.GONE);
                   btn_accept.setVisibility(View.GONE);
                   btn_decline.setVisibility(View.GONE);
                   bmod.setVisibility(View.VISIBLE);
                   brem.setVisibility(View.VISIBLE);
               }else if(bme) {

                   btn_accept.setVisibility(View.VISIBLE);
                   btn_decline.setVisibility(View.VISIBLE);
                    //bmessage.setVisibility(View.VISIBLE);
                    bmessage.setVisibility(View.GONE);
                    brem.setVisibility(View.GONE);
                    bmod.setVisibility(View.GONE);
              }else {
                   bmessage.setVisibility(View.GONE);
                   btn_accept.setVisibility(View.GONE);
                   btn_decline.setVisibility(View.GONE);
                   brem.setVisibility(View.GONE);
                   bmod.setVisibility(View.GONE);
                   if (Build.VERSION.SDK_INT >= 11) {
                       type.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                   }
                   float radius = type.getTextSize() / 3;
                   BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                   type.getPaint().setMaskFilter(filter);
                   type.setVisibility(View.GONE);
               }
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
        void onMessageClick(repreq HomeadsFeed);
        void onModifiClick(repreq HomeadsFeed);
        void onremoveClick(repreq HomeadsFeed);
        void onAcceptClick(repreq HomeadsFeed);
        void onDeclineClick(repreq HomeadsFeed);
    }
}
