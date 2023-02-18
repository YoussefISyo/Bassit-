package com.optim.bassit.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.R;
import com.optim.bassit.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<Album> mData = new ArrayList<>();
    public PhotoAdapter photoAdapter;
    public RecyclerView lst;
    ItemClickListener mListener;

    public AlbumAdapter(ItemClickListener listener) {
        this.mListener = listener;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_item_app, viewGroup, false);
        return new ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mData.get(position));
        holder.setIsRecyclable(false);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void fill(List<Album> albums) {
        mData.clear();
        mData.addAll(albums);
        notifyDataSetChanged();
    }


    // convenience method for getting data at click position
    public Album getItem(int id) {
        return mData.get(id);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }



        public void bind(Album album) {

            setIsRecyclable(false);
            TextView t_title = itemView.findViewById(R.id.t_title);
            TextView b_add_img = itemView.findViewById(R.id.b_add_img);
            ImageView img_edit = itemView.findViewById(R.id.img_edit);
            TextView t_preview_title = itemView.findViewById(R.id.t_preview_title);
            LinearLayout photo_title = itemView.findViewById(R.id.photo_title);


            LinearLayout b_edit_album_name = itemView.findViewById(R.id.b_edit_album_name);
            lst = itemView.findViewById(R.id.lst);
            lst.getRecycledViewPool().setMaxRecycledViews(1,0);
            t_title.setText(album.getTitle());
            t_preview_title.setText(album.getTitle());
            photoAdapter = new PhotoAdapter(itemView.getContext());
            photoAdapter.fill(album);
            lst.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            lst.setAdapter(photoAdapter);
            b_add_img.setOnClickListener(view -> mListener.addPhoto(album));
            b_edit_album_name.setOnClickListener(view -> mListener.renameAlbum(album));


            if(album.isIspreview())
            {
                photo_title.setVisibility(View.GONE);
                t_preview_title.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(5, 10, 5, 5);
                photo_title.setLayoutParams(params);


            }
        }


    }

    //****************** INTERFACE ***********************
    public interface ItemClickListener {
        void addPhoto(Album album);
        void renameAlbum(Album album);
    }
}
