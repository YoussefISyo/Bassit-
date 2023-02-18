package com.optim.bassit.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.optim.bassit.R;
import com.optim.bassit.models.Ads;
import com.optim.bassit.models.DynamicHeightImageView;
import com.optim.bassit.models.HomeFeed;
import com.optim.bassit.ui.activities.RechercheActivity;
import com.optim.bassit.utils.OptimTools;

import java.util.ArrayList;
import java.util.List;


public class RechercheResultAdapter extends RecyclerView.Adapter<RechercheResultAdapter.ViewHolder> {

    private List<HomeFeed> mData;
    private ItemClickListener mListener;

    public RechercheResultAdapter(ItemClickListener listener) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (mData.get(i).getId() < 0)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recherche_home_item_ads, viewGroup, false);
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recherche_home_item, viewGroup, false);

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
    int pos=0;
    public void fill(Integer page, List<HomeFeed> feeds) {

        boolean b=true;

        if (page == 1){
            mData.clear();
            pos=0;
        }

        for (HomeFeed feed : feeds) {
            mData.add(feed);
            if (mData.size() % 5 == 0){
                if(b && RechercheActivity.listSearchAds.size()<pos){
                    handleAds(RechercheActivity.listSearchAds,pos);
                    pos++;
                    b=false;
                }else{
                    HomeFeed ad = new HomeFeed();
                    ad.setId(-2);
                    mData.add(ad);
                    b=true;
                }


            }

        }


        notifyDataSetChanged();
    }

    private void handleAds(List<Ads> ads,int p) {
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

    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private HomeFeed HomeFeed;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(HomeFeed resul) {
            this.HomeFeed = resul;

 if(resul.getId() == -2) {
                AdView mAdView = itemView.findViewById(R.id.adView);
             AdRequest adRequest = new AdRequest.Builder().build();
               /*AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
                        .build();*/
              mAdView.loadAd(adRequest);
//                mAdView.setAdSize(AdSize.BANNER);
/*try{
    mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");


}catch (Exception e){}*/

     itemView.findViewById(R.id.rl).setVisibility(View.GONE);
             }else  if (resul.getId() < 0) {
                AdView mAdView = itemView.findViewById(R.id.adView);
                mAdView.setVisibility(View.GONE);
                DynamicHeightImageView img = itemView.findViewById(R.id.img);
                itemView.setOnClickListener(view -> mListener.onAdsClick(resul));
                OptimTools.getPicasso(resul.getImage()).into(img);
            } else {
                itemView.setOnClickListener(view -> mListener.onItemClick(resul));
                TextView t_title = itemView.findViewById(R.id.t_title);
                TextView t_categorie = itemView.findViewById(R.id.t_categorie);
                TextView t_name = itemView.findViewById(R.id.t_name);
                ImageView img = itemView.findViewById(R.id.img);

                t_name.setText(resul.getFullName());
                if (resul.getTherole() == 33)
                    t_categorie.setText(R.string.text_afrili);
                else
                    t_categorie.setText(resul.getCategorie() + " > " + resul.getSouscategorie());

                t_title.setText(resul.getTitle());
                OptimTools.getPicasso(resul.getServiceImageLink(300)).into(img);


            }
        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onItemClick(HomeFeed resul);

        void onAdsClick(HomeFeed homefeed);
    }
}