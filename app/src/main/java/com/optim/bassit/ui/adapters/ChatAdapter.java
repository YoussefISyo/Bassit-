package com.optim.bassit.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.optim.bassit.R;
import com.optim.bassit.models.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> mData;
    private ItemClickListener mListener;

    public ChatAdapter(ItemClickListener listener) {
        this.mListener = listener;
        this.mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item, viewGroup, false);
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

    public void fill(List<Chat> homeFeeds) {
        mData.clear();
        mData.addAll(homeFeeds);
        notifyDataSetChanged();
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Chat chat;

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Chat chat) {
            this.chat = chat;
            itemView.setOnClickListener(view -> mListener.onItemClick(chat));

            TextView txvTitle = itemView.findViewById(R.id.txvTitle);
            TextView txvContent = itemView.findViewById(R.id.txvContent);
/*
            txvTitle.setText(Customer.getAvatar(chat.));
            txvContent.setText(this.chat.getStampHumain());
            CircleImageView img = itemView.findViewById(R.id.img);

            if (this.chat.getAvatar() != null) {
                OptimTools.getPicasso(this.chat.getAvatarLink()).into(img);
            } else {
                img.setImageResource(R.drawable.ic_user);
            }*/

        /*    ImageView inew = itemView.findViewById(R.id.i_new);
            if (this.chat.getBseen() == 0) {
                inew.setVisibility(View.VISIBLE);
            } else {
                inew.setVisibility(View.GONE);
            }*/


        }
    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void onItemClick(Chat Chat);
    }
}