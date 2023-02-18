package com.optim.bassit.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optim.bassit.R;
import com.optim.bassit.models.Album;
import com.optim.bassit.models.Photo;
import com.optim.bassit.ui.activities.AlbumsAddEditActivity;
import com.optim.bassit.utils.OptimTools;

import java.io.File;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {


    private Album album;
AlbumAdapter.ItemClickListener mListener;
    private final Context ctx;
    public PhotoAdapter(Context ctx) {
        this.ctx =ctx;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_photo_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(album.getPhotos().get(position));
        holder.setIsRecyclable(false);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return album.getPhotos().size();
    }

    public void fill(Album album) {
        this.album = album;
        notifyDataSetChanged();
    }

    // convenience method for getting data at click position
    public Photo getItem(int id) {
        return album.getPhotos().get(id);
    }


    //****************** VIEW HOLDER ***********************
    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(Photo photo) {

            setIsRecyclable(false);
            ImageView img = itemView.findViewById(R.id.img);
            RelativeLayout album_relative = itemView.findViewById(R.id.album_relative);
            ImageView b_delete = itemView.findViewById(R.id.b_delete);

            if (photo.getFullUrl(400).startsWith("https://"))
                OptimTools.getPicasso(photo.getFullUrl(400)).into(img);
            else {
                File imgFile = new File(photo.getFullUrl(400));

                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img.setImageBitmap(myBitmap);
                }
            }
            img.setOnClickListener(view -> previewPhoto(photo));
            b_delete.setOnClickListener(view -> {

                ((AlbumsAddEditActivity) itemView.getContext()).AlertYesNo(R.string.confirmer_spp_photo, itemView.getContext().getString(R.string.text_delete), itemView.getContext().getString(R.string.text_cancel), () -> {
                    album.getPhotos().remove(photo);
                    notifyDataSetChanged();
                });

            });


            if (album.isIspreview()) {
                b_delete.setVisibility(View.GONE);
                img.getLayoutParams().height = (int) itemView.getContext().getResources().getDimension(R.dimen.imageview_height);
                img.getLayoutParams().width = (int) itemView.getContext().getResources().getDimension(R.dimen.imageview_width);

                album_relative.getLayoutParams().height = (int) itemView.getContext().getResources().getDimension(R.dimen.imageview_height);
                album_relative.getLayoutParams().width = (int) itemView.getContext().getResources().getDimension(R.dimen.imageview_width);
            }
        }

        private void previewPhoto(Photo photo) {
            ArrayList<Photo> all = new ArrayList<>();
            all.addAll(album.getPhotos());
            OptimTools.previewMultiplePhotos(all,album.getPhotos().indexOf(photo), ctx);
        }
    }


}
