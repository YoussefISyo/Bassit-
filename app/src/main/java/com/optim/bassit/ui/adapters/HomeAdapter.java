package com.optim.bassit.ui.adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.optim.bassit.R;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Ads;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomeFeed> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mListener;
    private ViewGroup viewGroup;
    Context context;

    // data is passed into the constructor
    public HomeAdapter(ItemClickListener listener, Context context) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (mData.get(i).getId() < 0)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_ads_item, viewGroup, false);
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_item, viewGroup, false);

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
    int pos=0;
    public void fill(int page, List<HomeFeed> homeFeeds, List<Ads> ads) {
        boolean b=false;

        if (page == 1){
            mData.clear();
            pos=0;
        }

        for (HomeFeed feed : homeFeeds) {
            mData.add(feed);
            if (mData.size() % 5 == 0){
                handleAds(ads,pos);
                pos++;
              /* if(b){
                    HomeFeed ad = new HomeFeed();
                    ad.setId(-2);
                    mData.add(ad);
                    b=false;
                }else{
                    handleAds(ads,pos);
                    pos++;
                    b=true;
                }*/


            }

        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void handleAds(List<Ads> ads ,int p) {
        try {

            HomeFeed feed = new HomeFeed();
            int pos =p;// mData.size() / 5 - 1;
            feed.setTitle(ads.get(pos).getTarget());
            feed.setIsg(ads.get(pos).getType());
            feed.setId(-1);
            feed.setImage(ads.get(pos).getFullImage());
            mData.add(feed);

        } catch (Exception ex) {

        }
    }

    // convenience method for getting data at click position
    public HomeFeed getItem(int id) {
        return mData.get(id);
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private HomeFeed homefeed;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(HomeFeed home) {
            homefeed = home;
            ImageView img2 = itemView.findViewById(R.id.image);
            if (home.getId() >= 0) {
                TextView username = itemView.findViewById(R.id.t_username);
                TextView categorie = itemView.findViewById(R.id.t_categorie);
                TextView content = itemView.findViewById(R.id.t_content);
                TextView title = itemView.findViewById(R.id.t_title);
                TextView rating = itemView.findViewById(R.id.rating);
               // RatingBar ratingBar = itemView.findViewById(R.id.ratingBar);
                ImageView img_official_account = itemView.findViewById(R.id.img_official_account);
                TextView from = itemView.findViewById(R.id.costfrom);
                TextView t_city = itemView.findViewById(R.id.t_city);
                ImageView plan_img = itemView.findViewById(R.id.plan_img);
                ImageView img_active_user = itemView.findViewById(R.id.img_active_user);
                TextView txt_active = itemView.findViewById(R.id.txt_active);

                title.setText(homefeed.getTitle());
                String num="0";
                if(homefeed.getNum_rating()!=null)
                    num=homefeed.getNum_rating();
                if(homefeed.getAvg_rating()!=null)
                rating.setText(homefeed.getAvg_rating()+" ("+num+")");
               // ratingBar.setRating(Float.parseFloat(homefeed.getAvg_rating()));
                else
                 //   ratingBar.setRating(0);
                    rating.setText("0 (0)");

                from.setText(homefeed.getMin_price());
                t_city.setText(homefeed.getTheDistance(itemView.getContext()));
                String.format(String.valueOf(Locale.ENGLISH),t_city);
                username.setText(homefeed.getFullName());

                if (homefeed.getStatus() == 0){
                    img_active_user.setColorFilter(ContextCompat.getColor(context, R.color.red));
                    txt_active.setText(R.string.inactive);
                }else{
                    img_active_user.setColorFilter(ContextCompat.getColor(context, R.color.greenSeafoam));
                    txt_active.setText(R.string.active);
                }

                if (homefeed.getTherole() == 33)
                    categorie.setText("Divers");
                else {
                 //   if(!Locale.getDefault().getLanguage().equals("en"))
                    categorie.setText(homefeed.getCategorie() + " > " + homefeed.getSouscategorie());
                  //  else categorie.setText(homefeed.getCategorien() + " > " + homefeed.getSouscategorien());
                }
                content.setText(homefeed.getDescription());

                RelativeLayout customer_layout = itemView.findViewById(R.id.customer_layout);
                customer_layout.setOnClickListener(view -> mListener.onShowProfile(home));


                CardView card = itemView.findViewById(R.id.card);
                card.setOnClickListener(view -> mListener.onCardClick(home));

                ImageView img = itemView.findViewById(R.id.profile_img);
                OptimTools.getPicasso(homefeed.getPinLink()).into(img);


                String link = homefeed.getServiceImageLink(800);
                OptimTools.getPicasso(link).into(img2);
                ImageView iwalk = itemView.findViewById(R.id.i_walk);
                plan_img.setVisibility(View.VISIBLE);
                switch (homefeed.getPlan()) {
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

                if (homefeed.getOfficial_account() == 1){
                    img_official_account.setVisibility(View.VISIBLE);
                }else{
                    img_official_account.setVisibility(View.GONE);
                }

                //itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_item, viewGroup, false);
                RecyclerView recyclerView = itemView.findViewById(R.id.rvTags);
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(itemView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                TagAdapter tagAdapter = new TagAdapter(false, null);
                recyclerView.setAdapter(tagAdapter);

                iwalk.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));
                t_city.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));

                if (homefeed.getTags() != null) {
                    List<String> tags = new ArrayList<>(Arrays.asList(homefeed.getTags().split(";")));
                    tagAdapter.fill(tags);
                }

                LinearLayout bmessage = itemView.findViewById(R.id.b_message);
                if (CurrentUser.getInstance().isLogin() && CurrentUser.getInstance().getmCustomer().getId() == homefeed.getUser_id())
                    bmessage.setVisibility(View.GONE);
                else {
                    bmessage.setOnClickListener(view -> mListener.onMessageClick(home));
                    bmessage.setVisibility(View.VISIBLE);

                }

            } /*else if(home.getId() == -2) {
                AdView mAdView = itemView.findViewById(R.id.adView);
              AdRequest adRequest = new AdRequest.Builder().build();
               /* AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
                        .build();*/
            //   mAdView.loadAd(adRequest);
//                mAdView.setAdSize(AdSize.BANNER);
/*try{
    mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");


}catch (Exception e){}*/

            //    img2.setVisibility(View.GONE);
          //  }*/
            else {

                AdView mAdView = itemView.findViewById(R.id.adView);
                mAdView.setVisibility(View.GONE);
                OptimTools.getPicasso(homefeed.getImage()).into(img2);
                img2.setOnClickListener((a) -> {
                    mListener.onAdsClick(homefeed);
                });
            }

        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onMessageClick(HomeFeed homefeed);

        void onShowProfile(HomeFeed homefeed);

        void onCardClick(HomeFeed homefeed);

        void onAdsClick(HomeFeed homefeed);

    }
}
