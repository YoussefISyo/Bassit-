package com.optim.bassit.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.optim.bassit.R;
import com.optim.bassit.data.CurrentUser;
import com.optim.bassit.models.Ads;
import com.optim.bassit.models.HomeadsFeed;
import com.optim.bassit.ui.fragments.AdsFragment;
import com.optim.bassit.utils.OptimTools;
import com.skyhope.showmoretextview.ShowMoreTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

public class HomeadsAdapter extends RecyclerView.Adapter<HomeadsAdapter.ViewHolder> {

    private List<HomeadsFeed> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mListener;
    private ViewGroup viewGroup;
    Context context;

    // data is passed into the constructor
    public HomeadsAdapter(ItemClickListener listener, Context context) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homeads_item, viewGroup, false);

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

    public void fill(int page, List<HomeadsFeed> homeFeeds, List<Ads> ads) {
        if (page == 1)
            mData.clear();
        for (HomeadsFeed feed : homeFeeds) {
            mData.add(feed);
            if (mData.size() % 5 == 0)
                handleAds(ads);
        }

        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void handleAds(List<Ads> ads) {
        try {

        /*    HomeadsFeed feed = new HomeadsFeed();
            int pos = mData.size() / 5 - 1;
            feed.setTitle(ads.get(pos).getTarget());
            feed.setIsg(ads.get(pos).getType());
            feed.setId(-1);
            feed.setImage(ads.get(pos).getFullImage());
            mData.add(feed);*/


            HomeadsFeed feed = new HomeadsFeed();
            int pos = mData.size() / 5 - 1;
            feed.setTitle(ads.get(pos).getTarget());
            feed.setIsg(ads.get(pos).getType());
            feed.setId(-1);
            feed.setImage(ads.get(pos).getFullImage());
            mData.add(feed);
        } catch (Exception ex) {

        }
    }

    // convenience method for getting data at click position
    public HomeadsFeed getItem(int id) {
        return mData.get(id);
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private HomeadsFeed homefeed;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(HomeadsFeed home) {
            homefeed = home;

            if (home.getId() >= 0) {
                TextView username = itemView.findViewById(R.id.t_username);
                TextView categorie = itemView.findViewById(R.id.t_categorie);
                ShowMoreTextView content = itemView.findViewById(R.id.t_content);
                TextView title = itemView.findViewById(R.id.t_title);
                TextView t_city = itemView.findViewById(R.id.t_city);
                ImageView img_official_account = itemView.findViewById(R.id.img_official_account);
                TextView type = itemView.findViewById(R.id.msg);
                TextView urgence = itemView.findViewById(R.id.txt_urgence);
                TextView txt_status = itemView.findViewById(R.id.txt_status);
                TextView dateStart = itemView.findViewById(R.id.tDateStart);
                TextView unity = itemView.findViewById(R.id.edt_unity);
                TextView quantity = itemView.findViewById(R.id.edt_quantity);
                ImageView img_active_user = itemView.findViewById(R.id.img_active_user);
                TextView txt_active = itemView.findViewById(R.id.txt_active);

                if (homefeed.getStatus() == 0){
                    img_active_user.setColorFilter(ContextCompat.getColor(context, R.color.red));
                    txt_active.setText(R.string.inactive);
                }else{
                    img_active_user.setColorFilter(ContextCompat.getColor(context, R.color.greenSeafoam));
                    txt_active.setText(R.string.active);
                }

                if (homefeed.getUrgence() == 0){
                    urgence.setVisibility(GONE);
                    txt_status.setVisibility(GONE);
                }else{
                    urgence.setVisibility(View.VISIBLE);
                    txt_status.setVisibility(View.VISIBLE);
                }

                if (homefeed.getDate_start() == null){
                    try {
                        Date datestart = new SimpleDateFormat("yyyy-MM-dd").parse(homefeed.getDatereq());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(datestart);
                        dateStart.setText(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "  - " + homefeed.getDurationreq());
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else{
                    dateStart.setText(homefeed.getDate_start() + "  - " + homefeed.getDurationreq());
                }

                if (homefeed.getUnity() == null){
                    unity.setText("Non spécifié");
                }else{
                    unity.setText(homefeed.getUnity());
                }

                quantity.setText(String.valueOf(homefeed.getQuantity()));

                if (homefeed.getOfficial_account() == 1){
                    img_official_account.setVisibility(View.VISIBLE);
                }else{
                    img_official_account.setVisibility(GONE);
                }

                //     ImageView plan_img = itemView.findViewById(R.id.plan_img);
                if(homefeed.getTypereq()==null || homefeed.getTypereq().trim().isEmpty())type.setVisibility(View.GONE);
                type.setText(homefeed.getTypereq());
                if(!Locale.getDefault().getLanguage().equals("en"))
                    type.setText(homefeed.getTypereq());
                else  type.setText(homefeed.getType_en());
                title.setText(homefeed.getTitle());
              t_city.setText(homefeed.getAdrs());
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
                categorie.setText(homefeed.getDatereq());


                ImageView img = itemView.findViewById(R.id.profile_img);
                OptimTools.getPicasso(homefeed.getPinLink()).into(img);
                if(homefeed.getUser_id().equals("6569"))
                {
                    itemView.findViewById(R.id.admin).setVisibility(View.VISIBLE);

                }

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
                LinearLayout brem = itemView.findViewById(R.id.b_rem);
                CardView card = itemView.findViewById(R.id.card);
                bmessage.setVisibility(View.GONE);
               // bmessage.setOnClickListener(view -> mListener.onMessageClick(home));
                card.setOnClickListener(view -> mListener.onMessageClick(home));
               // bmessage.setVisibility(View.VISIBLE);
                if (CurrentUser.getInstance().isLogin() &&  homefeed.getUser_id().equals(CurrentUser.getInstance().getmCustomer().getId()+"")) {
                   // bmessage.setVisibility(View.GONE);
                    brem.setOnClickListener(view -> mListener.onremoveClick(home));
                    brem.setVisibility(View.VISIBLE);
                    brem.setVisibility(View.GONE);
                }else {
                    brem.setVisibility(View.GONE);


                }
                if(homefeed.getNego().equals("0")){
                    bmessage.setVisibility(View.GONE);
                }

            } else {
                ImageView img2 = itemView.findViewById(R.id.image);
                OptimTools.getPicasso(homefeed.getImage()).into(img2);
                img2.setOnClickListener((a) -> {
                    mListener.onAdsClick(homefeed);
                });
            }

        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onMessageClick(HomeadsFeed HomeadsFeed);
        void onremoveClick(HomeadsFeed HomeadsFeed);
        void onAdsClick(HomeadsFeed HomeadsFeed);

    }
}
