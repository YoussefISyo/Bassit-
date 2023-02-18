package com.optim.bassit.ui.adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.optim.bassit.R;
import com.optim.bassit.models.Chat;
import com.optim.bassit.utils.OptimTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private final int iclient;
    private final String chatavatar;
    private final BottomSheetBehavior sheet;
    private List<Chat> mData;
    private ItemClickListener mListener;

    public ConversationAdapter(ItemClickListener listener, int iclient, String chatavatar, BottomSheetBehavior sheet) {
        this.mListener = listener;
        this.iclient = iclient;
        this.chatavatar = chatavatar;
        this.mData = new ArrayList<>();
        this.sheet = sheet;
    }


    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        Chat chat = mData.get(i);
        if (chat.getIsclient() == iclient || chat.getIsme() == 1)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bubble_me_item, viewGroup, false);
        else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bubble_him_item, viewGroup, false);
        }
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

    public void fill(List<Chat> chats) {
        mData.clear();
        for (Chat ch : chats) {
            if (ch.getMessage() != null)
                mData.add(ch);
        }

        notifyDataSetChanged();
    }

    public void addOne(Chat chat) {
        mData.add(chat);
        notifyDataSetChanged();
    }

    public void removeByTemp(String ss) {
        for (Chat ch :
                mData) {
            if (ch.getChk() == null)
                continue;
            if (ch.getChk().equals(ss)) {
                mData.remove(ch);
                notifyDataSetChanged();
                return;
            }
        }
    }

    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Chat chat;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Chat chat) {
            this.chat = chat;
            itemView.setOnClickListener(view -> {
                sheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mListener.onItemClick(chat);
            });

            TextView tTime = itemView.findViewById(R.id.t_time);
            TextView tMessage = itemView.findViewById(R.id.t_message);
            tMessage.setText(chat.getMessage());

            tTime.setText(chat.getStampHumain());
            tMessage.setVisibility(View.GONE);
            if (chat.getType() == 4) {
                TextView tMoney = itemView.findViewById(R.id.t_money);
                tMoney.setText(chat.getMessage());
                tMoney.setVisibility(View.VISIBLE);

            } else if (chat.getType() == 2) {
                LinearLayout tPos = itemView.findViewById(R.id.t_pos);
                tPos.setVisibility(View.VISIBLE);
            } else if (chat.getType() == 3) {
                RelativeLayout tDate = itemView.findViewById(R.id.layout_date);
                TextView t_date = itemView.findViewById(R.id.t_date);
                t_date.setText(chat.getMessage().replace(" ", "\n"));
                tDate.setVisibility(View.VISIBLE);
            } else if (chat.getType() == 1) {
                CardView card = itemView.findViewById(R.id.img_card);
                ImageView img = itemView.findViewById(R.id.img);
                if (chat.getMessage().startsWith("chat"))
                    OptimTools.getPicasso(chat.getChatImage()).into(img);
                else {
                    File imgFile = new File(chat.getMessage());

                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        img.setImageBitmap(myBitmap);
                    }
                }

                card.setVisibility(View.VISIBLE);
            } else if (chat.getType() == 5) {
                tMessage.setVisibility(View.VISIBLE);
                String[] ii = chat.getMessage().split(";");
                tMessage.setAllCaps(true);
                tMessage.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.transfert_bg));
                tMessage.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                tMessage.setTextSize(17);
                if (ii.length == 3) {
                    tMessage.setText("Transféré de " + ii[1]);
                } else
                    tMessage.setText("Error");
            } else if (chat.getType() == 6) {
                tMessage.setVisibility(View.VISIBLE);

                tMessage.setAllCaps(true);
                tMessage.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.round_button_black));
                tMessage.setTextColor(itemView.getContext().getResources().getColor(R.color.yellow));
                tMessage.setTextSize(17);
                tMessage.setText(chat.getMessage());
            } else {
                tMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onItemClick(Chat Chat);
    }
}